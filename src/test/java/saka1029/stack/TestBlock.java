package saka1029.stack;

import static org.junit.Assert.*;

import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import org.junit.Test;

public class TestBlock {

	static Evaluable operator(UnaryOperator<Value> op) {
		return c -> c.push(op.apply(c.pop()));
	}

	static Evaluable operator(BinaryOperator<Value> op) {
		return c -> {
			Value right = c.pop(), left = c.pop();
			c.push(op.apply(left, right));
		};
	}

	@Test
	public void testEvlis() {
		Context context = Context.of(5);
		Evaluable plus = operator((a, b) -> a.plus(b));
		Block block = Block.of(Int.of(1), Int.of(2), plus);
		block.evlis(context);
		assertEquals(1, context.sp);
		assertEquals(Int.of(3), context.pop());
	}
}
