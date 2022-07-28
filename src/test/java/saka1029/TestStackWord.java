package saka1029;

import static org.junit.Assert.*;
import static saka1029.Stack.*;

import java.util.function.BinaryOperator;
import java.util.function.Consumer;

import org.junit.Test;

public class TestStackWord {

    static Value eval(Context c, Value... block) {
        Value v = list(block);
        v.eval(c);
        return c.pop();
    }

    @SafeVarargs
    static <T> void prog(T t, Consumer<T>... cs) {
        for (Consumer<T> c : cs)
            c.accept(t);
    }
    
    static Loadable binary(BinaryOperator<Value> op) {
        return c -> {
            Value r = c.pop(), l = c.pop();
            c.push(op.apply(l, r));
        };
    }

    @Test
    public void testArithmetic() {
        Context context = new Context(10);
        assertEquals(i(3), eval(context, i(1), i(2), word("+", binary((a, b) -> a.plus(b)))));
        assertEquals(i(-1), eval(context, i(1), i(2), word("-", binary((a, b) -> a.minus(b)))));
        assertEquals(i(2), eval(context, i(1), i(2), word("*", binary((a, b) -> a.mult(b)))));
        assertEquals(i(0), eval(context, i(1), i(2), word("/", binary((a, b) -> a.div(b)))));
        assertEquals(i(1), eval(context, i(1), i(2), word("%", binary((a, b) -> a.mod(b)))));
    }
    
    @Test
    public void testCons() {
        Word plus = word("+", binary((a, b) -> a.plus(b)));
        Word cons = word("cons", binary((a, b) -> a.cons(b)));
        Word eval = word("eval", c -> { c.pop().eval(c); c.push(c.pop()); });
        List p = list(i(1), i(2), list(plus), cons, cons, eval);
        Context context = new Context(10);
        p.eval(context);
        System.out.println(p + " -> " + context);
        assertEquals(i(3), context.pop());
    }
}
