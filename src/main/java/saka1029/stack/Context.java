package saka1029.stack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

public class Context {

    public final LinkedList<Element> stack = new LinkedList<>();

    public final Map<String, Element> instructions = new HashMap<>();
    public final Map<String, Word> words = new HashMap<>();

    Context() {
    }

    public static Context of() {
        return new Context();
    }

    public int size() {
        return stack.size();
    }

    public void push(Element element) {
        stack.addLast(element);
    }

    public Element pop() {
        return stack.removeLast();
    }

    public Element peek(int offset) {
        return stack.get(stack.size() - offset - 1);
    }

    public Element peek() {
        return stack.getLast();
    }

    public void dup() {
        push(peek());
    }
    
    public void dup(int offset) {
        push(peek(offset));
    }

    public void drop() {
        stack.removeLast();
    }

    public void over() {
        push(peek(1));
    }

    public void swap() {
        Element top = pop(), next = pop();
        push(top);
        push(next);
    }

    public void rot() {
        Element z = pop(), y = pop(), x = pop();
        push(y);
        push(z);
        push(x);
    }

    public void execute(String name) {
        instructions.get(name).execute(this);
    }

    public void execute(Element element) {
        element.execute(this);
    }

    @Override
    public String toString() {
        return stack.stream()
            .map(e -> e.toString())
            .collect(Collectors.joining(" ", "[", "]"));
    }

    public Element instruction(String name) {
        return instructions.get(name);
    }

    public Word instruction(String name, Element element) {
        instructions.put(name, element);
        return word(name);
    }

    {
        instruction("dup", Context::dup);
        instruction("@0", Context::dup);
        instruction("@1", c -> c.dup(1));
        instruction("@2", c -> c.dup(2));
        instruction("@3", c -> c.dup(3));
        instruction("drop", Context::drop);
        instruction("swap", Context::swap);
        instruction("over", Context::over);
        instruction("rot", Context::rot);

        instruction("not", c -> c.push(Bool.of(!((Bool) c.pop()).value)));
        instruction("and", c -> {
            Bool r = (Bool) c.pop(), l = (Bool) c.pop();
            c.push(Bool.of(l.value && r.value));
        });
        instruction("or", c -> {
            Bool r = (Bool) c.pop(), l = (Bool) c.pop();
            c.push(Bool.of(l.value || r.value));
        });

        instruction("==", c -> {
            Element r = c.pop(), l = c.pop();
            c.push(Bool.of(l.equals(r)));
        });
        instruction("!=", c -> {
            Element r = c.pop(), l = c.pop();
            c.push(Bool.of(!l.equals(r)));
        });
        instruction("<", c -> {
            Ordered r = (Ordered) c.pop(), l = (Ordered) c.pop();
            c.push(Bool.of(l.compareTo(r) < 0));
        });
        instruction("<=", c -> {
            Ordered r = (Ordered) c.pop(), l = (Ordered) c.pop();
            c.push(Bool.of(l.compareTo(r) <= 0));
        });
        instruction(">", c -> {
            Ordered r = (Ordered) c.pop(), l = (Ordered) c.pop();
            c.push(Bool.of(l.compareTo(r) > 0));
        });
        instruction(">=", c -> {
            Ordered r = (Ordered) c.pop(), l = (Ordered) c.pop();
            c.push(Bool.of(l.compareTo(r) >= 0));
        });

        instruction("+", c -> {
            Int r = (Int) c.pop(), l = (Int) c.pop();
            c.push(Int.of(l.value + r.value));
        });
        instruction("-", c -> {
            Int r = (Int) c.pop(), l = (Int) c.pop();
            c.push(Int.of(l.value - r.value));
        });
        instruction("*", c -> {
            Int r = (Int) c.pop(), l = (Int) c.pop();
            c.push(Int.of(l.value * r.value));
        });
        instruction("/", c -> {
            Int r = (Int) c.pop(), l = (Int) c.pop();
            c.push(Int.of(l.value / r.value));
        });
        instruction("%", c -> {
            Int r = (Int) c.pop(), l = (Int) c.pop();
            c.push(Int.of(l.value % r.value));
        });

        instruction("true", Bool.TRUE);
        instruction("false", Bool.FALSE);

        instruction("pair", c -> {
            Element r = c.pop(), l = c.pop();
            c.push(Pair.of(l, r));
        });
        instruction("head", c -> c.push(((Pair) c.pop()).head));
        instruction("tail", c -> c.push(((Pair) c.pop()).tail));
        instruction("unpair", c -> {
            Pair p = (Pair) c.pop();
            c.push(p.head);
            c.push(p.tail);
        });

        instruction("execute", c -> ((List) c.pop()).executeAll(c));
        instruction("if", c -> {
            List e = (List) c.pop(), t = (List) c.pop();
            Bool b = (Bool) c.pop();
            if (b.value)
                t.executeAll(c);
            else
                e.executeAll(c);
        });
        instruction("for", c -> {
            List b = (List) c.pop(), l = (List) c.pop();
            for (Element e : l) {
                c.push(e);
                b.executeAll(c);
            }
        });
        instruction("define", c -> {
            List list = (List) c.pop();
            Str name = (Str) c.pop();
            c.instruction(name.value, list.instruction());
//            c.instruction(name.value, cc -> list.executeAll(cc));
        });
    }

    public Word word(String name) {
        return words.computeIfAbsent(name, k -> Word.of(k));
    }

}
