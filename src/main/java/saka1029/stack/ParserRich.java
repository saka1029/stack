package saka1029.stack;

public class ParserRich {

    enum Type {
        LP, RP, COMMA, COLON, QUOTE, EQ, BECOME, EOF;
    }

    final int[] input;
    int index, ch;
    Type token;

    ParserRich(String input) {
        this.input = input.codePoints().toArray();
        this.index = 0;
    }

    int ch() {
        if (index < input.length)
            return ch = input[index++];
        index = index + 1;
        return ch = -1;
    }

    void spaces() {
        while (Character.isWhitespace(ch))
            ch();
    }

    Type token(Type type) {
        ch();
        return type;
    }

    Type symbolInt(int first) {

    }

    Type token() {
        spaces();
        switch (ch) {
            case -1: token = Type.EOF; break;
            case '(': token = token(Type.LP); break;
            case ')': token = token(Type.RP); break;
            case ',': token = token(Type.COMMA); break;
            case ':': token = token(Type.COLON); break;
            case '\'': token = token(Type.QUOTE); break;
            case '-':
                if (ch == '>') {

                }
            case '=':
                ch();
                if (ch != '=') {
                    token = Type.EQ;
                    break;
                } else
                    token = symbolInt('=');
        }
    }


}
