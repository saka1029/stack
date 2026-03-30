package saka1029.stack;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Parser {

    enum Token implements Value {
        EOF, QUOTE, LP, RP, AT, COLON, COMMA,
        SYMBOL, INT, TRUE, FALSE;
    }

    final Reader reader;
    int ch;
    Token token;
    String string;

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
            case -1, '(', ')', '\'', '@', ':', ',' -> false;
            default -> !Character.isWhitespace(ch);
        };
    }

    Token word() {
        StringBuilder sb = new StringBuilder();
        while (isSymbol(ch)) {
            sb.append((char) ch);
            ch();
        }
        string = sb.toString();
        if (INT_PAT.matcher(string).matches())
            return Token.INT;
        return switch (string) {
            case "true" -> Token.TRUE;
            case "false" -> Token.FALSE;
            default -> Token.SYMBOL;
        };
    }

    Token token(Token result) {
        ch();
        return result;
    }

    Token token() {
        spaces();
        return token = switch (ch) {
            case -1 -> Token.EOF;
            case '\'' -> token(Token.QUOTE);
            case '(' -> token(Token.LP);
            case ')' -> token(Token.RP);
            case '@' -> token(Token.AT);
            case ':' -> token(Token.COLON);
            case ',' -> token(Token.COMMA);
            default -> word();
        };
    }

    Symbol symbol() {
        String v = string;
        token();
        return Symbol.of(v);
    }

    Int integer() {
        String v = string;
        token();
        return Int.of(Integer.parseInt(v));
    }

    Frame frame(java.util.List<Instruction> list, Frame frame) {
        if (frame != null)
            throw error("nested frame");
        java.util.List<Symbol> arguments = new ArrayList<>();
        while (token == Token.SYMBOL && string != "->")
            arguments.add(symbol());
        if (string != "->")
            throw error("'->' expected");
        token();    // skip '->'
        java.util.List<Symbol> locals = new ArrayList<>();
        while (token == Token.COMMA) {
            token(); // skip ','
            if (token == Token.SYMBOL)
                locals.add(symbol());
            else
                throw error("symbol expected");
            if (token == Token.EOF || token == Token.COLON || token == Token.COMMA)
                throw error("element expected");
            while (token != Token.EOF && token != Token.COLON)
                list.add(element(frame));
        }
        return new Frame(arguments, locals);
    }

    List list(Frame frame) {
        token(); // skip '('
        java.util.List<Instruction> list = new ArrayList<>();
        if (token == Token.COLON)
            frame = frame(list, frame);
        while (token != Token.EOF && token != Token.RP)
            list.add(element(frame));
        if (token != Token.RP)
            throw error("')' expected");
        token(); // skip ')'
        return Cons.list(list);
    }

    Instruction element(Frame frame) {
        switch (token) {
            case EOF: return null;
            case SYMBOL: return symbol();
            case TRUE:
                token();
                return Bool.TRUE;
            case FALSE:
                token();
                return Bool.FALSE;
            case INT: return integer();
            case QUOTE:
                token();
                return Quote.of(element(frame));
            case AT:
                token();
                if (token == Token.SYMBOL)
                    return new StoreGlobal(symbol());
                else
                    throw error("symbol expected after '@'");
            case LP:
                return list(frame);
            default:
                throw error("unexpected element '%s'", token);
        }

    }

    List expression() {
        java.util.List<Instruction> list = new ArrayList<>();
        Instruction e;
        while ((e = element(null)) != null)
            list.add(e);
        return Cons.list(list);
    }
    
    public List read() {
        List list = expression();
        System.out.println(list);
        return list;
    }
}
