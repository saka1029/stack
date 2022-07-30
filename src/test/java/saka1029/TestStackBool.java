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

}
