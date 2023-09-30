package saka1029.stack;

import java.io.IOException;
import java.io.Reader;
import java.util.regex.Pattern;

public class ElementReader {
    final Context context;
    final Reader reader;
    int ch;

    ElementReader(Context context, Reader reader) {
        this.context = context;
        this.reader = reader;
        get();
    }

    public static ElementReader of(Context context, Reader reader) {
        return new ElementReader(context, reader);
    }

    int get() {
        try {
            return ch = reader.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void spaces() {
        while (true) {
            while (Character.isWhitespace(ch))
                get();
            if (ch != '{')
                return;
            get(); // skip '{'
            while (ch != -1 && ch != '}')
                get();
            if (ch != '}')
                error("'}' expected");
            get(); // skip '}'
        }
    }

    static RuntimeException error(String format, Object... args) {
        return new RuntimeException(format.formatted(args));
    }

    List list() {
        get(); // skip '('
        spaces();
        java.util.List<Element> list = new java.util.ArrayList<>();
        while (ch != -1 && ch != ')' && ch != '.') {
            list.add(read());
            spaces();
        }
        switch (ch) {
            case ')':
                get(); // skip ')'
                return List.of(list, List.NIL);
            case '.':
                get(); // skip '.'
                Element tail = read();
                spaces();
                if (ch != ')')
                    throw error("')' expected");
                get(); // skip ')'
                return List.of(list, tail);
            default:
                throw error("')' or '.' expected");
        }
    }

    static final Pattern INT_PAT = Pattern.compile("[+-]?\\d+");

    static boolean isWord(int ch) {
        return switch (ch) {
            case '(', ')', '.', -1 -> false;
            default -> !Character.isWhitespace(ch);
        };
    }

    Element word() {
        StringBuilder sb = new StringBuilder();
        while (isWord(ch)) {
            sb.append((char) ch);
            get();
        }
        String word = sb.toString();
        if (word.length() != 1 && word.startsWith("/"))
            return Str.of(word.substring(1));
        else if (INT_PAT.matcher(word).matches())
            return Int.of(Integer.parseInt(word));
        else
            return context.word(word);
    }

    public Element read() {
        spaces();
        switch (ch) {
            case -1:
                return null;
            case '(':
                return list();
            case ')':
            case '.':
                throw error("Unexpected '%c'", (char) ch);
            default:
                return word();
        }
    }
}
