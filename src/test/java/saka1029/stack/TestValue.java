package saka1029.stack;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestValue {

	@Test
	public void testPlus() {
		assertEquals(Int.of(3), Int.of(1).plus(Int.of(2)));
		assertEquals(Str.of("ab"), Str.of("a").plus(Str.of("b")));
//		assertEquals(Str.of("1b"), Int.of(1).plus(Str.of("b")));
//		assertEquals(Str.of("a2"), Str.of("a").plus(Int.of(2)));
	}
	
}
