package test.saka1029.stack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import saka1029.stack.Context;
import saka1029.stack.Element;
import saka1029.stack.Int;
import saka1029.stack.List;
import saka1029.stack.Pair;

public class TestElement {

    @Test
    public void testPlus() {
        Context context = Context.of();
        context.execute(Int.ONE);
        assertEquals(1, context.size());
        assertEquals("[1]", context.toString());
        context.execute(Int.TWO);
        assertEquals(2, context.size());
        assertEquals("[1 2]", context.toString());
        context.execute("+");
        assertEquals(1, context.size());
        assertEquals("[3]", context.toString());
        Element result = context.pop();
        assertTrue(result instanceof Int);
        assertEquals(Int.THREE, result);
    }

    @Test
    public void testPair() {
        Context context = Context.of();
        context.execute(Int.ONE);
        assertEquals(1, context.size());
        assertEquals("[1]", context.toString());
        context.execute(Int.TWO);
        assertEquals(2, context.size());
        assertEquals("[1 2]", context.toString());
        context.execute("pair");
        assertEquals(1, context.size());
        assertEquals("[(1 . 2)]", context.toString());
        Element result = context.pop();
        assertEquals(0, context.size());
        assertTrue(result instanceof Pair);
        assertEquals(Pair.of(Int.ONE, Int.TWO), result);
        context.execute(Int.ONE);
        context.execute(List.NIL);
        assertEquals("[1 ()]", context.toString());
        context.execute("pair");
        assertEquals(1, context.size());
        assertEquals("(1)", context.pop().toString());
    }

    static Element LIST = c -> { Element r = c.pop(), l = c.pop(); c.push(List.of(l, r)); };

    @Test
    public void testList() {
        Context context = Context.of();
        context.execute(Int.ONE);
        assertEquals(1, context.size());
        assertEquals("[1]", context.toString());
        context.execute(Int.TWO);
        assertEquals(2, context.size());
        assertEquals("[1 2]", context.toString());
        LIST.execute(context);
        assertEquals(1, context.size());
        assertEquals("[(1 2)]", context.toString());
        Element result = context.pop();
        assertTrue(result instanceof Pair);
        assertEquals(List.of(Int.ONE, Int.TWO), result);
    }

}
