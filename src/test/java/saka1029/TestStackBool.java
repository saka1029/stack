package saka1029;

import static org.junit.Assert.*;
import static saka1029.Stack.*;

import org.junit.Test;

public class TestStackBool {

    @Test
    public void testConstant() {
        assertTrue(TRUE.value);
        assertFalse(FALSE.value);
    }

    @Test
    public void testNot() {
        assertTrue(TRUE.not() == FALSE);
        assertTrue(FALSE.not() == TRUE);
    }

    @Test
    public void testAnd() {
        assertTrue(TRUE.and(TRUE) == TRUE);
        assertTrue(TRUE.and(FALSE) == FALSE);
        assertTrue(FALSE.and(TRUE) == FALSE);
        assertTrue(FALSE.and(FALSE) == FALSE);
    }

    @Test
    public void testOr() {
        assertTrue(TRUE.or(TRUE) == TRUE);
        assertTrue(TRUE.or(FALSE) == TRUE);
        assertTrue(FALSE.or(TRUE) == TRUE);
        assertTrue(FALSE.or(FALSE) == FALSE);
    }

    @Test
    public void testXor() {
        assertTrue(TRUE.xor(TRUE) == FALSE);
        assertTrue(TRUE.xor(FALSE) == TRUE);
        assertTrue(FALSE.xor(TRUE) == TRUE);
        assertTrue(FALSE.xor(FALSE) == FALSE);
    }

    @Test
    public void testIf() {
        Context context = new Context(10);
        context.put("if", c -> {
            System.out.println("stack=" + c);
            Value other = c.pop(), then = c.pop(), cond = c.pop();
            System.out.println("cond=" + cond + " then=" + then + " other=" + other);
            if (cond == TRUE)
                then.eval(c);
            else
                other.eval(c);
            System.out.println("stack=" + c);
        });
        list(TRUE, i(0), i(1), context.get("if")).eval(context);
        assertEquals(i(0), context.pop());
        list(TRUE, list(i(0)), list(i(1)), context.get("if")).eval(context);
        assertEquals(i(0), context.pop());
    }

}
