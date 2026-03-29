package saka1029.stack;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Parser {

    enum Token implements Value {
        EOF, QUOTE, LP, RP, AT;
    }

    final Reader reader;
    int ch;
    Instruction token;

    Parser(Reader reader) {
        this.reader = reader;
        ch();
        token();
    }

    public static Parser parse(Reader reader) {
        return new Parser(reader);
    }

    public static Parser parse(String text) {
        // StringReaderはclose()しない点に注意する。
        return parse(new StringReader(text));
    }

    int ch() {
        try {
            return ch = reader.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void spaces() {
        while (true) {
            while (Character.isWhitespace(ch))
                ch();
            // コメントスキップ
            if (ch == '{') {
                while (ch != -1 && ch != '}')
                    ch();
                if (ch == '}')
                    ch();
            } else
                break;
        }
    }

    RuntimeException error(String format, Object... args) {
        return new RuntimeException(format.formatted(args));
    }

    static final Pattern INT_PAT = Pattern.compile("[+-]?\\d+");

    boolean isSymbol(int ch) {
        return switch (ch) {
            case -1, '(', ')', '\'', '@' -> false;
            default -> !Character.isWhitespace(ch);
        };
    }

    Instruction word() {
        StringBuilder sb = new StringBuilder();
        while (isSymbol(ch)) {
            sb.append((char) ch);
            ch();
        }
        String s = sb.toString();
        if (INT_PAT.matcher(s).matches())
            return Int.of(Integer.parseInt(s));
        return switch (s) {
            case "true" -> Bool.TRUE;
            case "false" -> Bool.FALSE;
            default -> Symbol.of(s);
        };
    }

    Instruction token(Instruction result) {
        ch();
        return result;
    }

    Instruction token() {
        spaces();
        return token = switch (ch) {
            case -1 -> Token.EOF;
            case '\'' -> token(Token.QUOTE);
            case '(' -> token(Token.LP);
            case ')' -> token(Token.RP);
            case '@' -> token(Token.AT);
            default -> word();
        };
    }

    List list() {
        token(); // skip '('
        java.util.List<Instruction> list = new ArrayList<>();
        while (token != Token.EOF && token != Token.RP)
            list.add(element());
        if (token != Token.RP)
            throw error("')' expected");
        token(); // skip ')'
        return Cons.list(list);
    }

    static final Symbol AT = Symbol.of("@");

    Instruction element() {
        if (token.equals(Token.EOF)) {
            return null;
        } else if (token.equals(Token.QUOTE)) {
            token(); // skip '\''
            return Quote.of(element());
        } else if (token.equals(Token.LP)) {
            return list();
        } else if (token.equals(Token.RP)) {
            throw error("unexpected ')'");
        } else if (token.equals(Token.AT)) {
            token(); // skip '@'
            Instruction next = element();
            if (next instanceof Symbol symbol)
                return new StoreGlobal(symbol);
            else
                throw error("symbol expected after '@'");
        } else { // ID or INTEGER
            Instruction word = token;
            token();
            return word;
        }
    }
    
    public List read() {
        java.util.List<Instruction> list = new ArrayList<>();
        Instruction e;
        while ((e = element()) != null)
            list.add(e);
        return Cons.list(list);
    }
}
