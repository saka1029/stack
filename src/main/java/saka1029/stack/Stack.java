package saka1029.stack;

import java.util.ArrayList;
import java.util.logging.Logger;

import saka1029.Common;

public class Stack {

	static final Logger logger = Common.logger(Stack.class);

	private Stack() {
	}

	public static Context context() {
	    Context c = Context.of();
		standard(c);
		return c;
	}

	public static List read(String source) {
		return Parser.parse(source).read();
	}

	public static void run(Context context, String source) {
		context.run(read(source));
	}

	public static Instruction eval(Context context, String source) {
		return context.eval(read(source));
	}

	static boolean b(Instruction instruction) {
		return ((Bool) instruction).value;
	}

	static Bool b(boolean value) {
		return Bool.of(value);
	}

	static int i(Instruction instruction) {
		return ((Int) instruction).value;
	}

	static Int i(int value) {
		return Int.of(value);
	}

	static Comparable comp(Instruction instruction) {
		return (Comparable) instruction;
	}

	static List l(Instruction instruction) {
		return (List) instruction;
	}

	static Cons c(Instruction instruction) {
		return (Cons) instruction;
	}

	static Symbol s(Instruction instruction) {
		return (Symbol) instruction;
	}
	
	static Array a(Instruction instruction) {
	    return (Array)instruction;
	}
	
	static Symbol symbol(String name) {
	    return Symbol.of(name);
	}

	static Quote quote(Instruction instruction) {
	    return Quote.of(instruction);
	}
	
	static List quickList(Instruction... instructions) {
	    int size = instructions.length;
	    return new List() {
            @Override
            public Sequence sequence() {
                return new Sequence() {
                    int i = 0;
                    @Override
                    public Instruction next() {
                        return i < size ? instructions[i++] : null;
                    }
                };
            }
	    };
	}

	static void put(Context context, String name, Instruction instruction) {
		context.variable(name, instruction);
	}

	static void standard(Context context) {
		context.variable("dup", Context::dup);
		context.variable("dup0", Context::dup);
		context.variable("dup1", c -> c.dup(1));
		context.variable("dup2", c -> c.dup(2));
		context.variable("dup3", c -> c.dup(3));
		context.variable("drop", Context::drop);
		context.variable("drop2", c -> c.drop(2));
		context.variable("drop3", c -> c.drop(3));
		context.variable("swap", Context::swap);
		context.variable("rot", Context::rot);
		context.variable("rrot", Context::rrot);
		context.variable("ret1", c -> c.ret(1));
		context.variable("ret2", c -> c.ret(2));
		context.variable("ret3", c -> c.ret(3));
//		context.variable("true", Bool.TRUE);
//		context.variable("false", Bool.FALSE);
		context.variable("==", c -> c.push(b(c.pop().equals(c.pop()))));
		context.variable("!=", c -> c.push(b(!c.pop().equals(c.pop()))));
		context.variable("<", c -> c.push(b(comp(c.pop()).compareTo(c.pop()) > 0)));
		context.variable("<=", c -> c.push(b(comp(c.pop()).compareTo(c.pop()) >= 0)));
		context.variable(">", c -> c.push(b(comp(c.pop()).compareTo(c.pop()) < 0)));
		context.variable(">=", c -> c.push(b(comp(c.pop()).compareTo(c.pop()) <= 0)));
		context.variable("+", c -> c.push(i(i(c.pop()) + i(c.pop()))));
		context.variable("-", c -> c.push(i(-i(c.pop()) + i(c.pop()))));
		context.variable("*", c -> c.push(i(i(c.pop()) * i(c.pop()))));
//		context.variable(vars, "isqrt", c -> c.push(i((int)Math.sqrt(i(c.pop())))));
		context.variable("and", c -> c.push(b(b(c.pop()) & b(c.pop()))));
		context.variable("or", c -> c.push(b(b(c.pop()) | b(c.pop()))));
		context.variable("not", c -> c.push(b(!b(c.pop()))));
		context.variable("/", c -> {
			int r = i(c.pop());
			c.push(i(i(c.pop()) / r));
		});
		context.variable("%", c -> {
			int r = i(c.pop());
			c.push(i(i(c.pop()) % r));
		});
		context.variable("null?", c -> c.push(b(c.pop().equals(List.NIL))));
		context.variable("car", c -> c.push(c(c.pop()).car));
		context.variable("cdr", c -> c.push(c(c.pop()).cdr));
		context.variable("uncons", c -> {
			Cons cons = c(c.pop());
			c.push(cons.car);
			c.push(cons.cdr);
		});
		context.variable("cons", c -> {
			List cdr = l(c.pop());
			Instruction car = c.pop();
			c.push(Cons.of(car, cdr));
		});
		context.variable("rcons", c -> c.push(Cons.of(c.pop(), l(c.pop()))));
		context.variable("reverse", c -> {
			List list = l(c.pop());
			List result = List.NIL;
			for (Instruction i : list)
				result = Cons.of(i, result);
			c.push(result);
		});
		context.variable("print", c -> c.print(c.pop()));
		context.variable("println", c -> c.println(c.pop()));
		context.variable("stack", c -> System.out.println(c));
		context.variable("execute", c -> c.execute(c.pop()));
		context.variable("if", c -> {
			Instruction orElse = c.pop(), then = c.pop();
			if (b(c.pop()))
				c.execute(then);
			else
				c.execute(orElse);
		});
		context.variable("for", c -> {
			Instruction closure = c.pop();
			Sequence it = l(c.pop()).sequence();
			c.instruction(() -> {
				Instruction i = it.next();
				return i == null ? null : Cons.list(i, closure);
			});
		});
		/*
		 * COND BODY while はCONDをexecuteして結果がtrueである間、BODYを実行する。
		 * 実装はリスト (COND 'BODY 'END if) を繰り返し返すSequenceをinstructionsにプッシュする。
		 * ENDはSequence内の終了フラグをセットするInstructionである。
		 * 終了フラグがtrueの場合、Sequenceはリストではなくnullを返す。
		 */
		context.variable("while", c -> {
		    Instruction body = c.pop(), cond = c.pop();
		    c.instruction(new Sequence() {
		        boolean done = false;
		        List w = quickList(cond, quote(body), quote(x -> done = true), symbol("if"));
                @Override
                public Instruction next() {
                    return done ? null : w;
                }
		    });
		});
		/*
		 * 名前に反してConsによるListを返す。
		 */
		context.variable("list", c -> {
			List list = l(c.pop());
			java.util.List<Instruction> a = new ArrayList<>();
			for (Instruction i : list)
				a.add(i);
			c.push(Cons.list(a));
		});
//		/*
//		 * このmapの実装はクロージャーを別コンテキストで評価する点に注意する。
//		 * クロージャー内ではmapのコンテキストにアクセスできない。
//		 * クロージャー実行時は常にスタックが空である。
//		 * このmapはConsではないListのサブクラスを返す点に注意する。
//		 */
//		put(vars, "map", c -> {
//			Instruction closure = c.pop();
//			List list = l(c.pop());
//			Context child = c.fork();    // クロージャー評価用コンテキスト
//			c.push(new List() {
//				@Override
//				public Sequence sequence() {
//					Sequence it = list.sequence();
//					return () -> {
//						Instruction i = it.next();
//						return i == null ? null : child.eval(Cons.list(i, closure));
//					};
//				}
//			});
//		});
		/*
		 * このmapの実装は結果をConsで返す。
		 * closure(mapper)の評価は現在のContext内で行う。
		 */
		context.variable("map", c -> {
			Instruction closure = c.pop();
			List list = l(c.pop());
			c.instruction(new Sequence() {
			    Sequence s = list.sequence();
                java.util.List<Instruction> result = new ArrayList<>();
                @Override
                public Instruction next() {
                    Instruction i = s.next();
                    if (i == null) {
                        c.push(Cons.list(result));
                        return null;
                    }
                    return quickList(i, closure, x -> result.add(c.pop()));
                }
			});
		});
//		/*
//		 * このfilterの実装はクロージャーを別コンテキストで評価する点に注意する。
//		 * クロージャー内ではfilterのコンテキストにアクセスできない。
//		 * クロージャー実行時は常にスタックが空である。
//		 * このfilterはConsではないListのサブクラスを返す点に注意する。
//		 */
//		put(vars, "filter", c -> {
//			Instruction closure = c.pop();
//			List list = l(c.pop());
//			Context child = c.fork();    // クロージャー評価用コンテキスト
//			c.push(new List() {
//				@Override
//				public Sequence sequence() {
//					Sequence it = list.sequence();
//					return new Sequence() {
//						Instruction cur;
//
//						{
//							advance();
//						}
//
//						void advance() {
//							for (cur = it.next(); cur != null && !b(child.eval(Cons.list(cur, closure))); cur = it.next())
//								/* do nothing */;
//						}
//
//						@Override
//						public Instruction next() {
//							Instruction result = cur;
//							advance();
//							return result;
//						}
//					};
//				}
//			});
//		});
		/*
		 * このfilterの実装はConsのリストを返す。
		 */
		context.variable("filter", c -> {
			Instruction closure = c.pop();
			List list = l(c.pop());
			c.instruction(new Sequence() {
			    Sequence s = list.sequence();
                java.util.List<Instruction> result = new ArrayList<>();
                @Override
                public Instruction next() {
                    Instruction i = s.next();
                    if (i == null) {
                        c.push(Cons.list(result));
                        return null;
                    }
                    return quickList(i, closure, quote(x -> result.add(i)), quote(List.NIL), symbol("if"));
                }
			});
		});
		context.variable("range", c -> {
			int step = i(c.pop()), end = i(c.pop()), start = i(c.pop());
			c.push(Range.of(start, end, step));
		});
		context.variable("define", c -> c.variable(s(c.pop()), c.pop()));
		context.variable("yield", Terminal.YIELD);
		context.variable("generator", c -> c.push(Generator.of(c, c.pop())));
		context.variable("generator1", c -> c.push(Generator.of(c, c.pop(), c.pop())));
		context.variable("generator2", c -> c.push(Generator.of(c, c.pop(), c.pop(), c.pop())));
		context.variable("array", c -> c.push(Array.of(i(c.pop()), c.pop())));
		context.variable("to-array", c -> c.push(Array.of(l(c.pop()))));
		context.variable("size", c -> c.push(i(a(c.pop()).size())));
		context.variable("at", c -> c.push(a(c.pop()).at(i(c.pop()))));
		context.variable("put", c -> a(c.pop()).put(i(c.pop()), c.pop()));
	}
}
