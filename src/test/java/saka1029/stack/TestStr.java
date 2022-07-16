package saka1029.stack;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

public class TestStr {

	@Test
	public void testSize() {
		assertEquals(Int.of(3), Str.of("魚の𩸽").size());
		assertEquals(Int.of(0), Str.of("").size());
	}

	@Test
	public void testAt() {
		assertEquals(Int.of('の'), Str.of("魚の𩸽").at(Int.of(1)));
	}
	
	@Test
	public void testIterator() {
		Iterator<Value> it = Str.of("魚の𩸽").iterator();
		assertTrue(it.hasNext());
		assertEquals(Int.of('魚'), it.next());
		assertEquals(Int.of('の'), it.next());
		assertEquals(Int.of("𩸽".codePointAt(0)), it.next());
		assertFalse(it.hasNext());
	}

}
