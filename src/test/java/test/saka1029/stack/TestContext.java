package test.saka1029.stack;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import saka1029.stack.Context;
import saka1029.stack.Int;
import saka1029.stack.Stack;

public class TestContext {

    @Test
    public void testRot() {
        Context c = Stack.context();
        c.push(Int.of(1));
        c.push(Int.of(2));
        c.push(Int.of(3));
        c.rot();
        assertEquals(Int.of(1), c.pop());
        assertEquals(Int.of(3), c.pop());
        assertEquals(Int.of(2), c.pop());
        assertEquals(0, c.size());
    }

    @Test
    public void testRrot() {
        Context c = Stack.context();
        c.push(Int.of(1));
        c.push(Int.of(2));
        c.push(Int.of(3));
        c.rrot();
        assertEquals(Int.of(2), c.pop());
        assertEquals(Int.of(1), c.pop());
        assertEquals(Int.of(3), c.pop());
        assertEquals(0, c.size());
    }
}
