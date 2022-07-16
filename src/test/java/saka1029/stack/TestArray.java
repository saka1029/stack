package saka1029.stack;


import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

public class TestArray {

	@Test
	public void testSize() {
		assertEquals(Int.of(3), Array.of(Int.of(0), Int.of(1), Int.of(2)).size());
		assertEquals(Int.of(0), Array.of().size());
	}

	@Test
	public void testAt() {
		assertEquals(Int.of(1), Array.of(Int.of(0), Int.of(1), Int.of(2)).at(Int.of(1)));
	}
	
	@Test
	public void testIterator() {
		Iterator<Value> it = Array.of(Int.of(0), Int.of(1), Int.of(2)).iterator();
		assertTrue(it.hasNext());
		assertEquals(Int.of(0), it.next());
		assertEquals(Int.of(1), it.next());
		assertEquals(Int.of(2), it.next());
		assertFalse(it.hasNext());
	}

}
