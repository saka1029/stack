package saka1029.stack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Reader {

    enum Token implements Value {
        EOF, QUOTE, LP, RP;
    }

    final java.io.Reader reader;
    int ch;
    Instruction token;

    Reader(java.io.Reader reader) {
        this.reader = reader;
        ch();
        token();
    }

    public static Reader of(java.io.Reader reader) {
        return new Reader(reader);
    }

    int ch() {
        try {
            return ch = reader.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void spaces() {
        while (Character.isWhitespace(ch))
            ch();
    }

    RuntimeException error(String format, Object... args) {
        return new RuntimeException(format.formatted(args));
    }

    static final Pattern INT_PAT = Pattern.compile("[+-]?\\d+");

    boolean isSymbol(int ch) {
        return switch (ch) {
            case -1, '(', ')' -> false;
            default -> !Character.isWhitespace(ch);
        };
    }

    Instruction symbolOrInt() {
        StringBuilder sb = new StringBuilder();
        while (isSymbol(ch)) {
            sb.append((char) ch);
            ch();
        }
        String s = sb.toString();
        if (INT_PAT.matcher(s).matches())
            return Int.of(Integer.parseInt(s));
        return Symbol.of(s);
    }
    
    Instruction advance(Instruction inst) {
        ch();
        return inst;
    }

    Instruction token() {
        spaces();
        return token = switch (ch) {
            case -1 -> Token.EOF;
            case '\'' -> advance(Token.QUOTE);
            case '(' -> advance(Token.LP);
            case ')' -> advance(Token.RP);
            default -> symbolOrInt();
        };
    }
    
    List list() {
        token(); // skip '('
        java.util.List<Instruction> list = new ArrayList<>();
        while (token != Token.EOF && token != Token.RP) {
            list.add(token);
            token();
        }
        if (token != Token.RP)
            throw error("')' expected");
        token(); // skip ')'
        return List.of(list);
    }

    public Instruction read() {
        if (token instanceof Token t) {
            switch (t) {
                case EOF:
                    return null;
                case QUOTE:
                    token();
                    return Quote.of(read());
                case LP:
                    return list();
                case RP:
                    throw error("unexpected ')'");
                default:
                    throw error("unknown token '%s'", t);
            }
        }
        Instruction symbolOrInt = token;
        token();
        return symbolOrInt;
    }
}
