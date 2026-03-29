package saka1029.stack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserRich {

    public enum Type {
        EOF, LP, RP, COMMA, COLON, QUOTE, AT,
        ID, INT;
    }

    final int[] input;
    int index, ch;
    Type token;
    public String string;

    ParserRich(String input) {
        this.input = input.codePoints().toArray();
        this.index = 0;
        ch();
    }

    public static ParserRich of(String input) {
        return new ParserRich(input);
    }

    int ch() {
        if (index < input.length)
            return ch = input[index++];
        index = input.length + 1;
        return ch = -1;
    }

    static Type oneChar(int ch) {
        return switch (ch) {
            case -1 -> Type.EOF;
            case '(' -> Type.LP;
            case ')' -> Type.RP;
            case ',' -> Type.COMMA;
            case ':' -> Type.COLON;
            case '\'' -> Type.QUOTE;
            case '@' -> Type.AT;
            default -> Type.ID;
        };
    }

    void spaces() {
        while (Character.isWhitespace(ch))
            ch();
    }

    static final Pattern INT = Pattern.compile("^\\d+$");

    public Type token() {
        spaces();
        Type type = oneChar(ch);
        if (type != Type.ID) {
            if (type != Type.EOF)
                ch();
            return type;
        }
        int start = index - 1;
        while (!Character.isWhitespace(ch) && oneChar(ch) == Type.ID)
            ch();
        string = new String(input, start, index - start - 1);
        Matcher m = INT.matcher(string);
        return m.matches() ?  Type.INT : Type.ID;
    }


}
