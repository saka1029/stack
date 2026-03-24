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
 * (@i ... $j ... ^k)
 * はi個の引数を受取りk個の値を返す。
 * <dl>
 *      <dt>@i</dt>
 *          <dd>手続きの先頭で受け取る引数の数iを宣言する。
 *          このときbpをバックアップして再設定する。</dd>
 *      <dt>$j</dt>  
 *          <dd>j番目の引数をスタックにプッシュする。</dd>
 *      <dt>^k</dt>
 *          <dd>手続きの最後で、返す値の数kを宣言する。
 *          ^kが実行された時点でスタックのトップにあるk個の値が戻り値となる。
 *          このときスタックにあったi個の引数はドロップする。
 *          bpはバックアップから回復する。</dd>
 * </dl>
 * 手続き(@1 ... ^2)に対して引数Aを指定して呼び出した場合の
 * Contextの状態を以下に示す。
 * <dl>
 *      <dt>手続きを呼び出した時点</dt>
 *      <dd>sp=1, bp=0, stack=[A(引数)]</dd>
 *      <dt>@1を実行した時点</dt>
 *      <dd>sp=3, bp=2, stack=[A(引数), 1(引数の数), 0(旧bp)]</dd>
 *      <dt>^2を実行する前の時点</dt>
 *      <dd>sp=6, bp=2, stack=[A(引数), 1(引数の数), 0(旧bp), X(中間結果), R(1つ目の戻り値), S(2つ目の戻り値)]</dd>
 *      <dt>^2を実行した後の時点</dt>
 *      <dd>sp=2, bp=0, stack=[R(1つ目の戻り値), S(2つ目の戻り値)]</dd>
 * </dl>
 */
public class TestBp {

    @Test
    public void testArgs1Result1() {
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
