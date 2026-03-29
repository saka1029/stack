package test.saka1029.stack;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static saka1029.stack.Stack.run;
import static saka1029.stack.Stack.eval;

import saka1029.stack.Context;
import saka1029.stack.Int;
import saka1029.stack.Stack;

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
 * 手続きから復帰するときは引数の数、bp、戻り値の数をもとに
 * スタック上の不要な要素(上記の例では「X(中間結果)」など)を自動的に削除する。
 */
public class TestBp {

    @Test
    public void testArgs1Result1() {
        Context c = Stack.context();
        run(c, """
            '(@1                        {引数は1個}
                $0 0 <=                 {if $0 <= 0}
                '1                      {then 1}
                '($0 1 - fact $0 *)     {else fact($0 - 1) * $0}
                if                      {end if}
            ^1)                         {戻り値は1個}
            'fact define
            """);
        assertEquals(eval(c, "1"), eval(c, "0 fact"));
        assertEquals(eval(c, "1"), eval(c, "1 fact"));
        assertEquals(eval(c, "2"), eval(c, "2 fact"));
        assertEquals(eval(c, "6"), eval(c, "3 fact"));
        assertEquals(eval(c, "24"), eval(c, "4 fact"));
    }

    @Test
    public void testArgs2Result2() {
        Context c = Stack.context();
        // A B addition --> A B (A + B)
        run(c, """
            '(@2                {引数は2個}
                $0              {戻り値1}
                $1              {戻り値2}
                $0 $1 +         {戻り値3}
            ^3)                 {戻り値は3個}
            'addition define
            """);
        run(c, "7 8 addition");
        assertEquals(Int.of(15), c.pop());
        assertEquals(Int.of(8), c.pop());
        assertEquals(Int.of(7), c.pop());
        assertEquals(0, c.sp);
    }

    @Test
    public void testArgs3Result2() {
        Context c = Stack.context();
        run(c, """
            8 9 10
            (@3                 {引数は3個}
                $0 $1 +         {戻り値1}
                $1 $2 +         {戻り値2}
            ^2)                 {戻り値は2個}
            """);
        assertEquals(Int.of(19), c.pop());
        assertEquals(Int.of(17), c.pop());
        assertEquals(0, c.sp);
    }

    /**
     * 引数の更新
     * 引数の数は3個
     * ($0, $1, $2) = (8, 9, 10)
     * $0 = $0 + $1, $1 = $1 + $2; 
     * return $1, $2;
     */
    @Test
    public void testSetArg() {
        Context c = Stack.context();
        run(c, """
            8 9 10
            (@3                 {引数は3個}
                $0 $1 + set$0   {$0 = $0 + $1}
                $1 $2 + set$1   {$1 = $1 + $2}
                $0 $1           {戻り値1, 戻り値2}
            ^2)                 {戻り値は2個}
            """);
        assertEquals(Int.of(19), c.pop());
        assertEquals(Int.of(17), c.pop());
        assertEquals(0, c.sp);
    }

    /**
     * ローカル変数の初期化
     * 引数の数は3個
     * ($0, $1, $2) = (8, 9, 10)
     * local %0 = $0 + $1, %1 = $1 + $2; 
     * return %0, %1;
     */
    @Test
    public void testLocal() {
        Context c = Stack.context();
        run(c, """
            8 9 10
            (@3             {引数は3個}
                $0 $1 +     {local %0 = $0 + $1}
                $1 $2 +     {local %1 = $1 + $2}
                %0 %1       {戻り値1, 戻り値2}
            ^2)             {戻り値は2個}
            """);
        assertEquals(Int.of(19), c.pop());
        assertEquals(Int.of(17), c.pop());
        assertEquals(0, c.sp);
    }

    /**
     * ローカル変数の設定
     * 引数の数は3個
     * ($0, $1, $2) = (8, 9, 10)
     * local %0 = 0, %1 = 0;
     * %0 = $0 + $1, %1 = $1 + $2; 
     * return %0, %1;
     */
    @Test
    public void testLocalUpdate() {
        Context c = Stack.context();
        // @3の後の「0 0」はローカル変数%0および%1の定義と初期化
        run(c, """
            8 9 10
            (@3                 {引数は3個}
                0               {local %0 = 0}
                0               {local %1 = 0}
                $0 $1 + set%0   {"%0 = $0 + $1}
                $1 $2 + set%1   {"%1 = $1 + $2}
                %0 %1           {戻り値1, 戻り値2}
            ^2)                 {戻り値は2個}
            """);
        assertEquals(Int.of(19), c.pop());
        assertEquals(Int.of(17), c.pop());
        assertEquals(0, c.sp);
    }

    @Test
    public void testFactorialByForByBp() {
        Context c = Stack.context();
        run(c, """
            '(@1                {引数は1個}
                1               {local %0 = 1}
                1 $0 1 range    {for i in (1..$0)}
                '(%0 * set%0)   {%0 = i * %0}
                for
                %0              {戻り値}
            ^1)                 {戻り値は1個}
            'fact define
            """);
        assertEquals(Int.of(1), eval(c, "0 fact"));
        assertEquals(Int.of(1), eval(c, "1 fact"));
        assertEquals(Int.of(2), eval(c, "2 fact"));
        assertEquals(Int.of(6), eval(c, "3 fact"));
        assertEquals(Int.of(24), eval(c, "4 fact"));
        assertEquals(Int.of(120), eval(c, "5 fact"));
    }
}
