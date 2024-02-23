package test.saka1029.stack;

import static org.junit.Assert.assertEquals;
import static saka1029.stack.Stack.eval;
import java.util.logging.Logger;
import org.junit.Test;
import saka1029.Common;
import saka1029.stack.Context;
import saka1029.stack.Stack;

public class TestArray {

    static final Logger logger = Common.logger(TestArray.class);

    @Test
    public void testAtPut() {
        Context c = Stack.context();
        assertEquals(eval(c, "'(() () () () ())"), eval(c, "'() 5 array"));
        assertEquals(eval(c, "'(false false false false false)"), eval(c, "false 5 array"));
        assertEquals(eval(c, "3"), eval(c, "3 (1 5 1 range to-array) at"));
        assertEquals(eval(c, "'(1 2 0 4 5)"), eval(c, "(1 5 1 range to-array) 0 3 dup2 put"));
    }

    @Test
    public void testConsole() {
        logger.info("日本語です。");
        System.out.println("日本語です。");
    }
}
