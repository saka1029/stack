package saka1029.stack;

import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Pattern;

public class Reader {

    enum Token implements Value {
        EOF, QUOTE, LP, RP;
    }

    final java.io.Reader reader;
    int ch;
    
    Reader(java.io.Reader reader) {
        this.reader = reader;
        getCh();
    }
    
    public static Reader of(String source) {
        return new Reader(new StringReader(source));
    }
    
    int getCh() {
        try {
            return ch = reader.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    void spaces() {
        while (Character.isWhitespace(ch))
            getCh();
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
            sb.append((char)ch);
            getCh();
        }
        String s = sb.toString();
        if (INT_PAT.matcher(s).matches())
            return Int.of(Integer.parseInt(s));
        return Symbol.of(s);
    }

    Instruction skip(Instruction i) {
        getCh();
        return i;
    }

    Instruction get() {
        spaces();
        return switch (ch) {
            case -1 -> Token.EOF;
            case '\'' -> skip(Token.QUOTE);
            case '(' -> skip(Token.LP);
            case ')' -> skip(Token.RP);
            default -> symbolOrInt();
        }
    }
}
