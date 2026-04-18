package test.saka1029.stack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import saka1029.stack.Context;
import saka1029.stack.Int;
import saka1029.stack.Stack;

public class TestContext {

    @Test
    public void testDup() {
        Context c = Stack.context();
        c.push(Int.of(1));
        c.push(Int.of(2));
        c.dup();
        assertEquals(Int.of(2), c.pop());
        assertEquals(Int.of(2), c.pop());
        assertEquals(Int.of(1), c.pop());
        assertEquals(0, c.sp);
    }

    @Test
    public void testDup1() {
        Context c = Stack.context();
        c.push(Int.of(1));
        c.push(Int.of(2));
        c.dup(1);
        assertEquals(Int.of(1), c.pop());
        assertEquals(Int.of(2), c.pop());
        assertEquals(Int.of(1), c.pop());
        assertEquals(0, c.sp);
    }

    @Test
    public void testDup2() {
        Context c = Stack.context();
        c.push(Int.of(1));
        c.push(Int.of(2));
        c.push(Int.of(3));
        c.dup(2);
        assertEquals(Int.of(1), c.pop());
        assertEquals(Int.of(3), c.pop());
        assertEquals(Int.of(2), c.pop());
        assertEquals(Int.of(1), c.pop());
        assertEquals(0, c.sp);
    }

    @Test
    public void testDrop() {
        Context c = Stack.context();
        c.push(Int.of(1));
        c.push(Int.of(2));
        c.push(Int.of(3));
        c.drop();
        assertEquals(Int.of(2), c.pop());
        assertEquals(Int.of(1), c.pop());
        assertEquals(0, c.sp);
    }

    @Test
    public void testDrop1() {
        Context c = Stack.context();
        c.push(Int.of(1));
        c.push(Int.of(2));
        c.push(Int.of(3));
        c.drop(1);
        assertEquals(Int.of(2), c.pop());
        assertEquals(Int.of(1), c.pop());
        assertEquals(0, c.sp);
    }

    @Test
    public void testDrop2() {
        Context c = Stack.context();
        c.push(Int.of(1));
        c.push(Int.of(2));
        c.push(Int.of(3));
        c.drop(2);
        assertEquals(Int.of(1), c.pop());
        assertEquals(0, c.sp);
    }

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
        assertEquals(0, c.sp);
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
        assertEquals(0, c.sp);
    }

    @Test
    public void testRet1() {
        Context c = Stack.context();
        c.push(Int.of(1));
        c.push(Int.of(2));
        c.push(Int.of(3));
        c.ret(1);
        assertEquals(Int.of(3), c.pop());
        assertEquals(Int.of(1), c.pop());
        assertEquals(0, c.sp);
    }

    @Test
    public void testRet2() {
        Context c = Stack.context();
        c.push(Int.of(1));
        c.push(Int.of(2));
        c.push(Int.of(3));
        c.push(Int.of(4));
        c.ret(2);
        assertEquals(Int.of(4), c.pop());
        assertEquals(Int.of(1), c.pop());
        assertEquals(0, c.sp);
    }

    @Test
    public void testRun() {
        Context c = Stack.context();
        try {
            Stack.run(c, "7 0 /");
            fail();
        } catch (RuntimeException e) {
            assertEquals("Fail '/' in Context(sp=2, bp=0 stack=[7 0])", e.getMessage());
            assertEquals(ArithmeticException.class, e.getCause().getClass());
        }
    }

    @Test
    public void testTrace() {
        Context c = Stack.context();
        java.util.List<String> traces = new ArrayList<>();
        c.output = s -> traces.add(s);
        Stack.run(c, "tron 1 2 + troff 7 1 -");
        assertEquals(List.of(
            "tron sp=0 bp=0 []",
            "1 sp=1 bp=0 [1]",
            "2 sp=2 bp=0 [1 2]",
            "+ sp=1 bp=0 [3]"), traces);
        assertEquals(Int.of(6), c.pop());
    }
}
