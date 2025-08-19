import java.util.*;

public class InterpreterVisitor implements MyLangParserVisitor {
    private Map<String, String> variables = new HashMap<>();
    private Scanner scanner = new Scanner(System.in);

    

    public Object visit(SimpleNode node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTProgram node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(ASTStatement node, Object data) {
        return node.childrenAccept(this, data);
    }
    @Override
public Object visit(ASTBlock node, Object data) {
    return node.childrenAccept(this, data);
}

    

    @Override
public Object visit(ASTStringDeclaration node, Object data) {
    // Получаем имя переменной
    ASTVar varNode = (ASTVar) node.jjtGetChild(0);
    String varName = varNode.getToken().image;

    // Получаем значение выражения (вторая часть)
    String value = (String) node.jjtGetChild(1).jjtAccept(this, data);

    // Сохраняем в глобальную карту
    MyLangParser.variables.put(varName, value);

    return null;
}

    public Object visit(ASTAssignment node, Object data) {
    String varName = resolveVarName(node.jjtGetChild(0));
    String value = (String) node.jjtGetChild(1).jjtAccept(this, data);

    if (!MyLangParser.variables.containsKey(varName)) {
        throw new RuntimeException("Variable '" + varName + "' not declared");
    }

    MyLangParser.variables.put(varName, value);
    return null;
}

  @Override
public Object visit(ASTIfStatement node, Object data) {
    boolean condition = (Boolean) node.jjtGetChild(0).jjtAccept(this, data);

    // Условие: первый дочерний узел — Condition
    // Второй — then-блок
    // Третий (если есть) — else-блок
    if (condition) {
        node.jjtGetChild(1).jjtAccept(this, data); // then block
    } else if (node.jjtGetNumChildren() > 2) {
        node.jjtGetChild(2).jjtAccept(this, data); // else block
    }
    return null;
}


    public Object visit(ASTCondition node, Object data) {
        String left = (String) node.jjtGetChild(0).jjtAccept(this, data);
        String op = (String) node.jjtGetChild(1).jjtAccept(this, data);
        String right = (String) node.jjtGetChild(2).jjtAccept(this, data);

        switch (op) {
            case "==": return left.equals(right);
            case "!=": return !left.equals(right);
            case "<": return left.compareTo(right) < 0;
            case ">": return left.compareTo(right) > 0;
            case "<=": return left.compareTo(right) <= 0;
            case ">=": return left.compareTo(right) >= 0;
            default: throw new RuntimeException("Unknown operator: " + op);
        }
    }

    public Object visit(ASTPrintStatement node, Object data) {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
        Object val = node.jjtGetChild(i).jjtAccept(this, data);
        if (val != null) System.out.print(val.toString());
    }
    System.out.println(); // перенос строки
    return null;
}

   @Override
public Object visit(ASTLoopStatement node, Object data) {
    Node countNode = node.jjtGetChild(0);

    Object result = countNode.jjtAccept(this, data);
if (result == null) {
    throw new RuntimeException("Loop count evaluation returned null");
}
String text = result.toString();
int count = Integer.parseInt(text);

    // Тело цикла — это дочерние узлы начиная с 1
    for (int i = 0; i < count; i++) {
        for (int j = 1; j < node.jjtGetNumChildren(); j++) {
            node.jjtGetChild(j).jjtAccept(this, data);
        }
    }

    return null;
}

    public Object visit(ASTInputStatement node, Object data) {
    ASTVar varNode = (ASTVar) node.jjtGetChild(0);
    String varName = varNode.getToken().image;

    // Не выбрасываем исключение, а создаем переменную при необходимости
    if (!MyLangParser.variables.containsKey(varName)) {
        MyLangParser.declaredVariables.add(varName); // если используешь declaredVariables
    }

    System.out.print("Enter value for " + varName + ": ");
    String input = scanner.nextLine();

    MyLangParser.variables.put(varName, input);
    return null;
}

    public Object visit(ASTExpression node, Object data) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
        Object part = node.jjtGetChild(i).jjtAccept(this, data);
        if (part != null) sb.append(part.toString());
    }
    return sb.toString(); // без кавычек
}

   @Override
public Object visit(ASTPrintableValue node, Object data) {
    Token token = node.getToken();
    if (token == null) throw new RuntimeException("PrintableValue has no token");

    String raw = token.image;

    // Вернуть значение переменной
    if (token.kind == MyLangParserConstants.VAR) {
        if (!MyLangParser.variables.containsKey(raw)) {
            throw new RuntimeException("Undefined variable: " + raw);
        }
        return MyLangParser.variables.get(raw);
    }

    // Вернуть строку без кавычек
    if (token.kind == MyLangParserConstants.STRING) {
        return raw.substring(1, raw.length() - 1);
    }

    // Вернуть числовое значение как строку
    if (token.kind == MyLangParserConstants.DIGIT) {
        return raw;
    }

    throw new RuntimeException("Unknown token type in PrintableValue: " + token.kind);
}


    @Override
public Object visit(ASTCompOperator node, Object data) {
    Token token = node.getToken();  // безопасно получить токен
    if (token == null) throw new RuntimeException("Comparison operator missing");
    return token.image;
}

   @Override
public Object visit(ASTPrintable node, Object data) {
    int n = node.jjtGetNumChildren();
    if (n == 0) return ""; // <== избегаем падения
    return node.jjtGetChild(0).jjtAccept(this, data);
}

public Object visit(ASTStringLiteral node, Object data) {
    String raw = node.getToken().image;
    return raw.substring(1, raw.length() - 1); // убираем кавычки
}

@Override
public Object visit(ASTVar node, Object data) {
    String name = node.getToken().image;
    if (!MyLangParser.variables.containsKey(name)) {
        throw new RuntimeException("Undefined variable: " + name);
    }
    return MyLangParser.variables.get(name);
}
public String resolveVarName(Node node) {
    if (node instanceof ASTVar) {
        return ((ASTVar) node).getToken().image;
    }
    throw new RuntimeException("Expected a variable node");
}

}
