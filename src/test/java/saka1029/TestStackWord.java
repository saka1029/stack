package saka1029;

import static org.junit.Assert.*;
import static saka1029.Stack.*;

import java.util.function.Consumer;

import org.junit.Test;

public class TestStackWord {

    static Value eval(Context c, Value... block) {
        Value v = list(block);
        v.evlis(c);
        return c.pop();
    }

    @SafeVarargs
    static <T> void prog(T t, Consumer<T>... cs) {
        for (Consumer<T> c : cs)
            c.accept(t);
    }

    @Test
    public void test() {
        Context context = new Context(10);
        assertEquals(i(3), eval(context, i(1), i(2), word("+", c -> prog(c.pop(), r -> c.push(c.pop().plus(r))))));
        assertEquals(i(-1), eval(context, i(1), i(2), word("-", c -> prog(c.pop(), r -> c.push(c.pop().minus(r))))));
        assertEquals(i(2), eval(context, i(1), i(2), word("*", c -> prog(c.pop(), r -> c.push(c.pop().mult(r))))));
        assertEquals(i(0), eval(context, i(1), i(2), word("/", c -> prog(c.pop(), r -> c.push(c.pop().div(r))))));
        assertEquals(i(1), eval(context, i(1), i(2), word("%", c -> prog(c.pop(), r -> c.push(c.pop().mod(r))))));
    }
}
