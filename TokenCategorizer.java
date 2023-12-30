import java.util.regex.Pattern;

public class TokenCategorizer {

    private static final String RE_Keywords = "\\b(auto|break|case|char|const|continue|default|do|double|else|enum|extern|float|for|goto|if|int|long|register|return|short|signed|sizeof|static|struct|switch|typedef|union|unsigned|void|volatile|while|string|class|struct|include)\\b";
    private static final String RE_Operators = "(\\+\\+|--|==|!=|<=|>=|&&|\\|\\||[+\\-*/%<>=])";
    private static final String RE_Numerals = "\\b(\\d+)\\b";
    private static final String RE_Special_Characters = "[\\[@&~!#$\\^\\|\\{\\}\\]:;<>?,\\.']|\\(\\)|\\(|\\)|\\{|\\}|\\[\\]|\"";
    private static final String RE_Identifiers = "\\b[a-zA-Z_]+[a-zA-Z0-9_]*\\b";
    private static final String RE_Headers = "#include\\s+<\\w+\\.h>";

    private static int currentTokenIndex = 0;
    private static String[] tokens;

    public static String categorizeToken(String token) {
        if (Pattern.matches(RE_Keywords, token)) {
            return "Keyword";
        } else if (Pattern.matches(RE_Operators, token)) {
            return "Operator";
        } else if (Pattern.matches(RE_Numerals, token)) {
            return "Numeral";
        } else if (Pattern.matches(RE_Special_Characters, token)) {
            return "Special Character/Symbol";
        } else if (Pattern.matches(RE_Identifiers, token)) {
            return "Identifier";
        } else if (Pattern.matches(RE_Headers, token)) {
            return "Header";
        } else {
            return "Unknown Value";
        }
    }

    private static void parseAssignment() {
        parseType();
        parseIdentifier();
        match("=");
        parseExpression();
        match(";");
    }

    private static void parseType() {
        match("int"); // For simplicity, assume only 'int' type in this example
    }

    private static void parseIdentifier() {
        if (Pattern.matches(RE_Identifiers, tokens[currentTokenIndex])) {
            currentTokenIndex++;
        } else {
            syntaxError("Expected an identifier");
        }
    }

    private static void parseExpression() {
        parseTerm();
        while (tokens[currentTokenIndex].equals("+")) {
            currentTokenIndex++;
            parseTerm();
        }
    }

    private static void parseTerm() {
        parseFactor();
        while (tokens[currentTokenIndex].equals("*")) {
            currentTokenIndex++;
            parseFactor();
        }
    }

    private static void parseFactor() {
        if (Pattern.matches(RE_Identifiers, tokens[currentTokenIndex]) || Pattern.matches(RE_Numerals, tokens[currentTokenIndex])) {
            currentTokenIndex++;
        } else if (tokens[currentTokenIndex].equals("(")) {
            currentTokenIndex++;
            parseExpression();
            match(")");
        } else {
            syntaxError("Expected an identifier, numeral, or '('");
        }
    }

    private static void match(String expected) {
        if (currentTokenIndex < tokens.length && tokens[currentTokenIndex].equals(expected)) {
            currentTokenIndex++;
        } else {
            syntaxError("Expected '" + expected + "'");
        }
    }

    private static void syntaxError(String message) {
        System.out.println("Syntax Error: " + message);
        System.exit(1);
    }

    public static void main(String[] args) {
        String input_program = "int a= 10; int b= 20; int sum= a + b;";
        String[] input_program_tokens = input_program.split("\\s+|(?<=[=<>!{};,#])|(?=[=<>!{};,#])");

        currentTokenIndex = 0;
        tokens = input_program_tokens;

        while (currentTokenIndex < tokens.length) {
            System.out.println(tokens[currentTokenIndex] + "  Is A  > " + categorizeToken(tokens[currentTokenIndex]));
            currentTokenIndex++;
        }

        System.out.println("\nPerforming Syntax Analysis:");
        currentTokenIndex = 0;
        parseAssignment();
        System.out.println("Syntax Analysis Passed");
    }
}