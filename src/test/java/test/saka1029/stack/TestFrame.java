package test.saka1029.stack;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import saka1029.stack.Context;
import static saka1029.stack.Stack.*;
import saka1029.stack.List;
import saka1029.stack.Parser;

public class TestFrame {

    @Test
    public void testFrame() {
        Context c = context();
        String source = "'( : n -> r : n 0 <= 1 '(n 1 - fact n *) if) @fact";
        List list = Parser.parse(source).read();
        System.out.println(list);
        run(c, source);
        assertEquals(eval(c, "1"), eval(c, "0 fact"));
        assertEquals(eval(c, "1"), eval(c, "1 fact"));
        assertEquals(eval(c, "2"), eval(c, "2 fact"));
        assertEquals(eval(c, "6"), eval(c, "3 fact"));
        assertEquals(eval(c, "24"), eval(c, "4 fact"));
        assertEquals(eval(c, "120"), eval(c, "5 fact"));
    }

    @Test
    public void testLocals() {
        Context c = context();
        String source = "'( : n -> r, f 1 : 1 n 1 range '(f * @f) for f) @fact";
        List list = Parser.parse(source).read();
        System.out.println(list);
        run(c, source);
        assertEquals(eval(c, "1"), eval(c, "0 fact"));
        assertEquals(eval(c, "1"), eval(c, "1 fact"));
        assertEquals(eval(c, "2"), eval(c, "2 fact"));
        assertEquals(eval(c, "6"), eval(c, "3 fact"));
        assertEquals(eval(c, "24"), eval(c, "4 fact"));
        assertEquals(eval(c, "120"), eval(c, "5 fact"));

    }

}
