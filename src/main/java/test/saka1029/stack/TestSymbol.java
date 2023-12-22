package test.saka1029.stack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import saka1029.stack.Context;
import saka1029.stack.Int;
import saka1029.stack.Symbol;

public class TestSymbol {

    @Test
    public void testEquals() {
        assertEquals(Symbol.of("foo"), Symbol.of("foo"));
        assertNotEquals(Symbol.of("foo"), Symbol.of("bar"));
        assertTrue(Symbol.of("foo") == Symbol.of("foo"));
        assertTrue(Symbol.of("foo") != Symbol.of("bar"));
    }
    
    @Test
    public void testToString() {
        assertEquals("foo", Symbol.of("foo").toString());
    }
    
    @Test
    public void testExecute() {
        Context c = Context.of(Map.of(Symbol.of("three"), Int.of(3)));
        Symbol.of("three").execute(c);
        assertEquals(1, c.stack.size());
        assertEquals(Int.of(3), c.pop());
    }

}
