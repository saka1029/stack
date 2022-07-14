package saka1029.stack;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestContext {

	@Test
	public void testOf() {
		Context context = Context.of(5);
		assertEquals(0, context.sp);
	}
	
	@Test
	public void testPushPopPeek() {
		Context context = Context.of(5);
		context.push(Int.of(1));
		assertEquals(1, context.sp);
		assertEquals(Int.of(1), context.peek());
		assertEquals(Int.of(1), context.pop());
		assertEquals(0, context.sp);
	}
	
	@Test
	public void testToString() {
		Context context = Context.of(5);
		assertEquals("[]", context.toString());
		context.push(Int.of(1));
		assertEquals("[1]", context.toString());
	}

}
