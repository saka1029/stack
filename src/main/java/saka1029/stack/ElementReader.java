package saka1029.stack;

import java.io.IOException;
import java.io.Reader;

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
        while (Character.isWhitespace(ch))
            get();
    }
    
    static boolean isWord(int ch) {
        return switch (ch) {
            case -1, '(', ')', '/', '.' -> false;
            default -> !Character.isWhitespace(ch);
        };
    }
    
    Element identifier() {
        get(); // skip '/'
        spaces();
        StringBuilder sb = new StringBuilder();
        while (isWord(ch)) {
            sb.append((char)ch);
            get();
        }
        return Str.of(sb.toString());
    }

    Element integer(int first) {
        StringBuilder sb = new StringBuilder();
        if (first != 0)
            sb.append((char)first);
        while (Character.isDigit(ch)) {
            sb.append((char)ch);
            get();
        }
        return Int.of(Integer.parseInt(sb.toString()));
    }

    Element word(int first) {
        StringBuilder sb = new StringBuilder();
        if (first != 0)
            sb.append((char)first);
        while (isWord(ch)) {
            sb.append((char)ch);
            get();
        }
        return context.word(sb.toString());
    }
    
    Element list() {
        get(); // skip '('
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
					throw new RuntimeException("')' expected");
				get(); // skip ')'
				return List.of(list, tail);
			default:
				throw new RuntimeException("')' or '.' expected");
        }
    }

    public Element read() {
        spaces();
        int f = 0;
        switch (ch) {
            case -1:
                return null;
            case '/':
                return identifier();
            case '(':
                return list();
            case ')':
                throw new RuntimeException("Unexpected ')'");
            case '-':
            case '+':
                f = ch;
                get();
                /* thru */
            default:
                return Character.isDigit(ch) ? integer(f) : word(f);
        }
    }
}
