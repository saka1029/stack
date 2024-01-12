package test.saka1029.stack;

import org.junit.Test;

import saka1029.stack.Context;
import saka1029.stack.Generator;
import saka1029.stack.Stack;
import saka1029.stack.Symbol;

public class TestGenerator {

    @Test
    public void test() {
        Context c = Stack.context();
        c.variable(Symbol.of("generator"), x -> x.push(Generator.of(x, c.pop())));
    }

}
