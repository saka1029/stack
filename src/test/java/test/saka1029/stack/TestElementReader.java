package test.saka1029.stack;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;

import org.junit.Test;

import saka1029.stack.Bool;
import saka1029.stack.Context;
import saka1029.stack.Element;
import saka1029.stack.ElementReader;
import saka1029.stack.Int;
import saka1029.stack.List;
import saka1029.stack.Str;

public class TestElementReader {

    @Test
    public void testReadInt() {
        String text = "  2  -1 +33";
        Context context = Context.of();
        ElementReader reader = ElementReader.of(context, new StringReader(text));
        assertEquals(Int.TWO, reader.read());
        assertEquals(Int.MINUS_ONE, reader.read());
        assertEquals(Int.of(33), reader.read());
        assertEquals(null, reader.read());
    }

    @Test
    public void testReadWord() {
        String text = "  word next + - ++ / ";
        Context context = Context.of();
        ElementReader reader = ElementReader.of(context, new StringReader(text));
        assertEquals(context.word("word"), reader.read());
        assertEquals(context.word("next"), reader.read());
        assertEquals(context.word("+"), reader.read());
        assertEquals(context.word("-"), reader.read());
        assertEquals(context.word("++"), reader.read());
        assertEquals(context.word("/"), reader.read());
        assertEquals(null, reader.read());
    }

    @Test
    public void testReadIdentifier() {
        String text = "  /word /next ";
        Context context = Context.of();
        ElementReader reader = ElementReader.of(context, new StringReader(text));
        assertEquals(Str.of("word"), reader.read());
        assertEquals(Str.of("next"), reader.read());
        assertEquals(null, reader.read());
    }

    @Test
    public void testReadBool() {
        String text = "  true false ";
        Context context = Context.of();
        ElementReader reader = ElementReader.of(context, new StringReader(text));
        context.execute(reader.read());
        context.execute(reader.read());
        assertEquals(null, reader.read());
        assertEquals(2, context.size());
        assertEquals(Bool.FALSE, context.pop());
        assertEquals(Bool.TRUE, context.pop());
    }

    @Test
    public void testReadList() {
        String text = " (1 a) () ";
        Context context = Context.of();
        ElementReader reader = ElementReader.of(context, new StringReader(text));
        assertEquals(List.of(Int.ONE, context.word("a")), reader.read());
        assertEquals(List.NIL, reader.read());
        assertEquals(null, reader.read());
    }

    @Test
    public void testDefine() {
        String text = "/plus (+) define 1 2 plus";
        Context context = Context.of();
        ElementReader reader = ElementReader.of(context, new StringReader(text));
        context.execute(reader.read());
        context.execute(reader.read());
        context.execute(reader.read());
        context.execute(reader.read());
        context.execute(reader.read());
        context.execute(reader.read());
        assertEquals(null, reader.read());
        assertEquals(1, context.size());
        assertEquals(Int.THREE, context.pop());
    }

    static void run(Context context, String text) {
        ElementReader reader = ElementReader.of(context, new StringReader(text));
        Element e;
        while ((e = reader.read()) != null)
            context.execute(e);
    }

    static Element eval(Context context, String text) {
        int s = context.size();
        run(context, text);
        assertEquals(s + 1, context.size());
        return context.pop();
    }

    @Test
    public void testRun() {
        Context context = Context.of();
        assertEquals(Int.THREE, eval(context, "/plus (+) define 1 2 plus"));
    }

    /**
     * CASE 1
     * 
     * <pre>
     * () (1 2 3 4) append
     * swap   : (1 2 3 4) ()
     * drop   : (1 2 3 4)
     * </pre>
     * 
     * CASE 2
     * 
     * <pre>
     * (1 2) (3 4) append
     * over   : (1 2) (3 4) (1 2)
     * tail   : (1 2) (3 4) (2)
     * swap   : (1 2) (2) (3 4)
     * append : (1 2) (2 3 4)
     * swap   : (2 3 4) (1 2)
     * head   : (2 3 4) 1
     * swap   : 1 (2 3 4)
     * pair   : (1 2 3 4)
     * </pre>
     */
    @Test
    public void testIf() {
        Context c = Context.of();
        assertEquals(Int.of(24), eval(c, "/fact (dup 0 <= (drop 1) (dup 1 - fact *) if) define 4 fact"));
        assertEquals(eval(c, "0"), eval(c,
            "/append (over () == (swap drop) (over tail swap append swap head swap pair) if) define 0"));
        assertEquals(eval(c, "(1 2 3 4)"), eval(c, "() (1 2 3 4) append"));
        assertEquals(eval(c, "(1 2 3 4)"), eval(c, "(1 2) (3 4) append"));
        assertEquals(eval(c, "(1 2 3 4)"), eval(c, "(1 2 3) (4) append"));
        assertEquals(eval(c, "(1 2 3 4)"), eval(c, "(1 2 3 4) () append"));
    }

    @Test
    public void testForEach() {
        Context c = Context.of();
        assertEquals(eval(c, "10"), eval(c, "0 (1 2 3 4) (+) foreach"));
        assertEquals(eval(c, "((((() . 1) . 2) . 3) . 4)"), eval(c, "() (1 2 3 4) (pair) foreach"));
        assertEquals(eval(c, "(4 3 2 1)"), eval(c, "() (1 2 3 4) (swap pair) foreach"));
        assertEquals(eval(c, "(4 3 2 1)"), eval(c, "/reverse (() swap (swap pair) foreach) define (1 2 3 4) reverse"));
    }

    /**
     * CASE 1
     * 
     * <pre>
     * () (1 2 3 4) append
     * swap   : (1 2 3 4) ()
     * drop   : (1 2 3 4)
     * </pre>
     * 
     * CASE 2
     * 
     * <pre>
     * (1 2) (3 4) append
     * swap   : (3 4) (1 2)
     * unpair : (3 4) 1 (2)
     * rot    : 1 (2) (3 4)
     * append : 1 (2 3 4)
     * pair   : (1 2 3 4)
     * </pre>
     */
    @Test
    public void testRot() {
        Context c = Context.of();
        assertEquals(eval(c, "0"), eval(c,
            "/append (swap dup () == (drop) (unpair rot append pair) if) define 0"));
        assertEquals(eval(c, "(1 2 3 4)"), eval(c, "() (1 2 3 4) append"));
        assertEquals(eval(c, "(1 2 3 4)"), eval(c, "(1 2) (3 4) append"));
        assertEquals(eval(c, "(1 2 3 4)"), eval(c, "(1 2 3) (4) append"));
        assertEquals(eval(c, "(1 2 3 4)"), eval(c, "(1 2 3 4) () append"));
    }

    /**
     * (3 4) (1 2) rappend -> (1 2) (3 4)
     * 
     * CASE 1
     * <pre>
     * (1 2 3 4) () append
     * drop   : (1 2 3 4)
     * </pre>
     * 
     * CASE 2
     * <pre>
     * (3 4) (1 2) append
     * unpair : (3 4) 1 (2)
     * rot    : 1 (2) (3 4)
     * append : 1 (2 3 4)
     * pair   : (1 2 3 4)
     * </pre>
     */
    @Test
    public void testRappend() {
        Context c = Context.of();
        assertEquals(eval(c, "0"), eval(c,
            "/rappend (dup () == (drop) (unpair rot append pair) if) define\n"
            + "/append (swap rappend) define\n"
            + "0"));
        assertEquals(eval(c, "(1 2 3 4)"), eval(c, "() (1 2 3 4) append"));
        assertEquals(eval(c, "(1 2 3 4)"), eval(c, "(1 2) (3 4) append"));
        assertEquals(eval(c, "(1 2 3 4)"), eval(c, "(1 2 3) (4) append"));
        assertEquals(eval(c, "(1 2 3 4)"), eval(c, "(1 2 3 4) () append"));
    }
}
