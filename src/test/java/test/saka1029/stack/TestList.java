package test.saka1029.stack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;

import saka1029.stack.Bool;
import saka1029.stack.Context;
import saka1029.stack.Element;
import saka1029.stack.Int;
import saka1029.stack.List;

public class TestList {

    @Test
    public void testOf() {
        assertEquals(List.of(Int.ZERO, Int.ONE, Int.TWO), List.of(java.util.List.of(Int.ZERO, Int.ONE, Int.TWO), List.NIL));
    }

    @Test
    public void testIterable() {
        List list = List.of(Int.ZERO, Int.ONE, Int.TWO);
        Iterator<Element> it = list.iterator();
        assertTrue(it.hasNext());
        assertEquals(Int.ZERO, it.next());
        assertTrue(it.hasNext());
        assertEquals(Int.ONE, it.next());
        assertTrue(it.hasNext());
        assertEquals(Int.TWO, it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void testNilIterable() {
        List list = List.NIL;
        Iterator<Element> it = list.iterator();
        assertFalse(it.hasNext());
    }
    
    @Test
    public void testExecuteAll() {
        Context context = Context.of();
        List.of(Int.ONE, Int.TWO, context.word("+")).executeAll(context);
        assertEquals(1, context.size());
        assertEquals(Int.THREE, context.pop());
    }
    
    /**
     * (1 2 +) execute -> 3
     */
    @Test
    public void testExecute() {
        Context context = Context.of();
        context.execute(List.of(Int.ONE, Int.TWO, context.word("+")));
        context.execute("execute");
        assertEquals(1, context.size());
        assertEquals(Int.THREE, context.pop());
    }
    
    /**
     * 1 2 true (+) (-) if -> 3
     */
    @Test
    public void testIfThen() {
        Context context = Context.of();
        context.execute(Int.ONE);
        context.execute(Int.TWO);
        context.execute(Bool.TRUE);
        context.execute(List.of(context.word("+")));
        context.execute(List.of(context.word("-")));
        context.execute("if");
        assertEquals(1, context.size());
        assertEquals(Int.THREE, context.pop());
    }
    
    /**
     * 1 2 0 (+) (-) if -> -1
     */
    @Test
    public void testIfElse() {
        Context context = Context.of();
        context.execute(Int.ONE);
        context.execute(Int.TWO);
        context.execute(Bool.FALSE);
        context.execute(List.of(context.word("+")));
        context.execute(List.of(context.word("-")));
        assertEquals("[1 2 false (+) (-)]", context.toString());
        context.execute("if");
        assertEquals(1, context.size());
        assertEquals(Int.MINUS_ONE, context.pop());
    }

    /**
     * 0 (1 2 3) (+) foreach -> 6
     */
    @Test
    public void testForEach() {
        Context context = Context.of();
        Int.ZERO.execute(context);
        context.execute(List.of(Int.ONE, Int.TWO, Int.THREE));
        context.execute(List.of(context.word("+")));
        context.execute("foreach");
        assertEquals(1, context.size());
        assertEquals(Int.of(6), context.pop());
    }
    
    /**
     * fact : (dup 0 <= (1) (dup 1 - fact *) if)
     * 0 fact -> 1
     * 1 fact -> 1
     * 3 fact -> 6
     * 4 fact -> 24
     */
    @Test
    public void testFact() {
        Context context = Context.of();
        List fact = List.of(context.word("dup"), Int.ZERO, context.word("<="),
            List.of(context.word("drop"), Int.ONE),
            List.of(context.word("dup"), Int.ONE, context.word("-"), context.word("fact"), context.word("*")), context.word("if"));
        assertEquals("(dup 0 <= (drop 1) (dup 1 - fact *) if)", fact.toString());
        context.instruction("fact", c -> fact.executeAll(c));

        context.execute(Int.ZERO);
        context.execute("fact");
        assertEquals(1, context.size());
        assertEquals(Int.ONE, context.pop());

        context.execute(Int.ONE);
        context.execute("fact");
        assertEquals(1, context.size());
        assertEquals(Int.ONE, context.pop());

        context.execute(Int.THREE);
        context.execute("fact");
        assertEquals(1, context.size());
        assertEquals(Int.of(6), context.pop());

        context.execute(Int.of(4));
        context.execute("fact");
        assertEquals(1, context.size());
        assertEquals(Int.of(24), context.pop());
    }
    
    @Test
    public void testUnpair() {
        Context c = Context.of();
        c.execute(List.of(Int.ONE, Int.TWO, Int.THREE));
        c.execute("unpair");
        assertEquals(List.of(Int.TWO, Int.THREE), c.pop());
        assertEquals(Int.ONE, c.pop());
    }
}
