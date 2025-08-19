import java.util.*;

public class CodeGenerationVisitor implements MyLangParserVisitor {
    private int indentLevel = 0;

    private String indent(String code) {
        StringBuilder sb = new StringBuilder();
        String[] lines = code.split("\n");
        for (String line : lines) {
            sb.append("    ".repeat(indentLevel)).append(line).append("\n");
        }
        return sb.toString();
    }

    @Override
    public Object visit(ASTProgram node, Object data) {
        StringBuilder code = new StringBuilder();
        code.append("# Auto-generated Python code\n\n");
        code.append("import sys\n\n");
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            String stmt = (String) node.jjtGetChild(i).jjtAccept(this, data);
            if (stmt != null) code.append(stmt);
        }
        return code.toString();
    }

    @Override
    public Object visit(ASTStringDeclaration node, Object data) {
        String varName = ((ASTVar) node.jjtGetChild(0)).getToken().image;
        String value = (String) node.jjtGetChild(1).jjtAccept(this, data);
        return indent(varName + " = " + value + "\n");
    }

    @Override
    public Object visit(ASTAssignment node, Object data) {
        String varName = ((ASTVar) node.jjtGetChild(0)).getToken().image;
        String value = (String) node.jjtGetChild(1).jjtAccept(this, data);
        return indent(varName + " = " + value + "\n");
    }

    @Override
    public Object visit(ASTIfStatement node, Object data) {
        StringBuilder code = new StringBuilder();
        String condition = (String) node.jjtGetChild(0).jjtAccept(this, data);

        code.append(indent("if " + condition + ":\n"));
        indentLevel++;
        String thenBlock = (String) node.jjtGetChild(1).jjtAccept(this, data);
        code.append(thenBlock);
        indentLevel--;

        if (node.jjtGetNumChildren() > 2) {
            code.append(indent("else:\n"));
            indentLevel++;
            String elseBlock = (String) node.jjtGetChild(2).jjtAccept(this, data);
            code.append(elseBlock);
            indentLevel--;
        }

        return code.toString();
    }

    @Override
    public Object visit(ASTCondition node, Object data) {
        String left = (String) node.jjtGetChild(0).jjtAccept(this, data);
        String op = (String) node.jjtGetChild(1).jjtAccept(this, data);
        String right = (String) node.jjtGetChild(2).jjtAccept(this, data);
        return left + " " + op + " " + right;
    }

    @Override
    public Object visit(ASTPrintStatement node, Object data) {
        List<String> values = new ArrayList<>();
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            values.add((String) node.jjtGetChild(i).jjtAccept(this, data));
        }
        return indent("print(" + String.join(", ", values) + ")\n");
    }

    @Override
public Object visit(ASTLoopStatement node, Object data) {
    String loopCount = (String) node.jjtGetChild(0).jjtAccept(this, data);
    StringBuilder code = new StringBuilder();

    // Оборачиваем loopCount в int() — безопасно, если loopCount это переменная
    code.append(indent("for i in range(int(" + loopCount + ")):\n"));
    indentLevel++;
    for (int i = 1; i < node.jjtGetNumChildren(); i++) {
        code.append((String) node.jjtGetChild(i).jjtAccept(this, data));
    }
    indentLevel--;
    return code.toString();
}

    @Override
    public Object visit(ASTInputStatement node, Object data) {
        String varName = ((ASTVar) node.jjtGetChild(0)).getToken().image;
        return indent(varName + " = input(\"Enter value for " + varName + ": \")\n");
    }

    @Override
    public Object visit(ASTExpression node, Object data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            if (i > 0) sb.append(" + ");
            sb.append((String) node.jjtGetChild(i).jjtAccept(this, data));
        }
        return sb.toString();
    }

    @Override
    public Object visit(ASTPrintableValue node, Object data) {
        Token token = node.getToken();
        if (token.kind == MyLangParserConstants.STRING) {
            return token.image;
        } else {
            return token.image;
        }
    }

    @Override
    public Object visit(ASTPrintable node, Object data) {
        return node.jjtGetChild(0).jjtAccept(this, data);
    }

    @Override
    public Object visit(ASTVar node, Object data) {
        return node.getToken().image;
    }

    @Override
    public Object visit(ASTStringLiteral node, Object data) {
        return node.getToken().image;
    }

    @Override
    public Object visit(ASTCompOperator node, Object data) {
        return node.getToken().image;
    }

    @Override
    public Object visit(ASTBlock node, Object data) {
        StringBuilder block = new StringBuilder();
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            String stmt = (String) node.jjtGetChild(i).jjtAccept(this, data);
            if (stmt != null) block.append(stmt);
        }
        return block.toString();
    }

    @Override
    public Object visit(ASTStatement node, Object data) {
        return node.jjtGetChild(0).jjtAccept(this, data);
    }
    @Override
public Object visit(SimpleNode node, Object data) {
    // По умолчанию обходим всех детей
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
        node.jjtGetChild(i).jjtAccept(this, data);
    }
    return null;
}
}
