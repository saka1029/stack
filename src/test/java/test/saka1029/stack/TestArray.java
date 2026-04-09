package test.saka1029.stack;

import static org.junit.Assert.assertEquals;
import static saka1029.stack.Stack.eval;
import static saka1029.stack.Stack.run;
import java.util.logging.Logger;
import org.junit.Test;
import saka1029.Common;
import saka1029.stack.Bool;
import saka1029.stack.Context;
import saka1029.stack.Stack;

public class TestArray {

    static final Logger logger = Common.logger(TestArray.class);

    @Test
    public void testConstructorSize() {
        Context c = Stack.context();
        assertEquals(eval(c, "'(() () () () ())"), eval(c, "'() 5 array"));
        assertEquals(eval(c, "'(false false false false false)"), eval(c, "false 5 array"));
    }

    @Test
    public void testSize() {
        Context c = Stack.context();
        assertEquals(eval(c, "5"), eval(c, "'() 5 array size"));
    }

    @Test
    public void testAt() {
        Context c = Stack.context();
        assertEquals(eval(c, "4"), eval(c, "3 (1 5 range to-array) at"));
    }

    @Test
    public void testPut() {
        Context c = Stack.context();
        assertEquals(eval(c, "'(1 2 3 0 5)"), eval(c, "(1 6 range to-array) 0 3 dup2 put"));
    }

    @Test
    public void testToArray() {
        Context c = Stack.context();
        assertEquals(eval(c, "'(1 2 3)"), eval(c, "'(1 2 3) to-array"));
        // array自身もto-arrayでコピーできる。
        assertEquals(eval(c, "'(1 2 3)"), eval(c, "'(1 2 3) to-array to-array"));
        // to-arrayがクローンを作ることの確認
        run(c, "'(1 2 3) to-array dup to-array 0 2 dup2 put");
        assertEquals(eval(c, "'(1 2 0)"), c.pop());
        assertEquals(eval(c, "'(1 2 3)"), c.pop());
    }

    @Test
    public void testMap() {
        Context c = Stack.context();
        assertEquals(eval(c, "'(2 4 6)"), eval(c, "'(1 2 3) to-array '(2 *) map"));
    }
}
