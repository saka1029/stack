package test.saka1029.stack;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static saka1029.stack.Stack.context;
import static saka1029.stack.Stack.run;
import static saka1029.stack.Stack.eval;

import saka1029.stack.Context;
import saka1029.stack.Int;

/**
 * bpを使った手続きの定義
 * <pre>
 * (&#64;i ... $j ... ^k)
 * &#64;i : 手続きの先頭で受け取る引数の数Iを宣言する。
 * $j : J番目の引数をスタックにプッシュする。
 * ^k : 手続きの最後で、返す値の数Kを宣言する。
 *      ^kが実行された時点でスタックのトップにあるk個の値が戻り値となる。
 *      このときスタックにあったi個の引数はドロップする。
 * </pre>
 */
public class TestBp {

    @Test
    public void testArgs1() {
        Context c = context();
        run(c, "'(@1 $0 0 <= '1 '($0 1 - fact $0 *) if ^1) 'fact define");
        assertEquals(eval(c, "1"), eval(c, "0 fact"));
        assertEquals(eval(c, "1"), eval(c, "1 fact"));
        assertEquals(eval(c, "2"), eval(c, "2 fact"));
        assertEquals(eval(c, "6"), eval(c, "3 fact"));
        assertEquals(eval(c, "24"), eval(c, "4 fact"));
    }

    @Test
    public void testArgs2Result2() {
        Context c = context();
        // A B addition --> A B (A + B)
        run(c, "'(@2 $0 $1 $0 $1 + ^3) 'addition define");
        run(c, "7 8 addition");
        assertEquals(Int.of(15), c.pop());
        assertEquals(Int.of(8), c.pop());
        assertEquals(Int.of(7), c.pop());
        assertEquals(0, c.sp);
    }

    @Test
    public void testArgs3Result2() {
        Context c = context();
        run(c, "8 9 10 (@3 $0 $1 + $1 $2 + ^2)");
        assertEquals(Int.of(19), c.pop());
        assertEquals(Int.of(17), c.pop());
        assertEquals(0, c.sp);
    }

}
