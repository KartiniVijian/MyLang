# MyLang 🌍💻  
*A culturally-inclusive programming language for beginners*  

**MyLang** is a custom-built, beginner-friendly programming language designed to make coding more inclusive and intuitive for non-native English speakers. Unlike most mainstream languages that rely solely on English-based keywords, MyLang uses culturally relevant keywords from **Malay, Kazakh, and Chinese** (e.g., `wenzi` for strings, `cetak` for print, `eger` for if, `ulang` for loop). This lowers the barrier to entry for learners from diverse linguistic backgrounds while preserving programming fundamentals.  

MyLang was implemented using **JavaCC** for grammar parsing and an **Abstract Syntax Tree (AST)** for code execution and translation. Programs written in MyLang can be:  
- **Interpreted directly**, or  
- **Translated into Java or Python** for execution.  

This makes MyLang both an educational tool and a bridge to industry-standard languages.  

---

## ✨ Features
- Localized syntax for inclusivity and accessibility  
- Core programming constructs: variables, assignments, conditionals, loops, and printing  
- Interpreter for real-time execution  
- Code generation into **Java** and **Python**  
- Modular design that supports future extensions (e.g., functions, numerical types, IDE integration)  

---

## 🎯 Objectives
- Reduce linguistic barriers in programming education  
- Support novice learners with an intuitive, culturally relevant syntax  
- Prond Python for real-world applicability  

---

## 🔑 Example
**MyLang code:**
```plaintext
wenzi password = "lifeissogood";

eger (password == "lifeissogood") {
   cetak("Access Granted");
   wenzi message = "Welcome";
   message = message + "HAHA";
   cetak(message);
} nemese {
   cetak("Access Denied");
   cetak("Try Again");
}
