package saka1029;

import static org.junit.Assert.*;
import static saka1029.Stack.*;

import java.util.Iterator;

import org.junit.Test;

public class TestStackStr {

    @Test
    public void testEquals() {
        assertEquals(s("𩸽"), s("𩸽"));
    }

    @Test
    public void testSize() {
        assertEquals(i(3), s("abc").size());
        assertEquals(i(3), s("魚は𩸽").size());
    }

    @Test
    public void testIterator() {
        Iterator<Value> it = s("abc").iterator();
        assertTrue(it.hasNext());
        assertEquals(i('a'), it.next());
        assertEquals(i('b'), it.next());
        assertEquals(i('c'), it.next());
        assertFalse(it.hasNext());
    }
    
    @Test
    public void testIteratorCodePoints() {
        Iterator<Value> it = s("魚は𩸽").iterator();
        assertTrue(it.hasNext());
        assertEquals(i('魚'), it.next());
        assertEquals(i('は'), it.next());
        assertEquals(i(Character.codePointAt("𩸽", 0)), it.next());
        assertFalse(it.hasNext());
    }
}
