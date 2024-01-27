package test.saka1029.cons;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import org.junit.Test;

import saka1029.Common;

public class TestCons {

    static final Logger logger = Common.logger(TestCons.class);
    
    interface Instruction {}
    
    static class Int implements Instruction {
        public final int value;
        Int(int value) {
            this.value = value;
        }
        public static Int of(int value) {
            return new Int(value);
        }
        @Override
        public String toString() {
            return "" + value;
        }
    }

    static abstract class List implements Instruction, Iterable<Instruction> {
        public abstract Instruction car();
        public abstract List cdr();
        
        public static List NIL = new List() {
            @Override public Instruction car() { return null; } 
            @Override public List cdr() { return null; } 
            @Override public String toString() { return "()"; }
        };

        public static List of(Instruction... elements) {
            List result = NIL;
            for (int i = elements.length - 1; i >= 0; --i)
                result = Cons.of(elements[i], result);
            return result;
        }
        
        @Override
        public Iterator<Instruction> iterator() {
            return new Iterator<>() {

                List list = List.this;

                @Override
                public boolean hasNext() {
                    return list.car() != null;
                }

                @Override
                public Instruction next() {
                    if (!hasNext())
                        throw new NoSuchElementException();
                    Instruction r = list.car();
                    list = list.cdr();
                    return r;
                }
            };
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("(");
            String sep = "";
            for (Instruction i : this) {
                sb.append(sep).append(i);
                sep = " ";
            }
            return sb.append(")").toString();
        }
    }
    
    static class Cons extends List {
        public final Instruction car;
        public final List cdr;
        
        Cons(Instruction car, List cdr) {
            this.car = car;
            this.cdr = cdr;
        }
        
        public static Cons of(Instruction car, List cdr) {
            return new Cons(car, cdr);
        }

        @Override
        public Instruction car() {
            return car;
        }

        @Override
        public List cdr() {
            return cdr;
        }
    }
    
    static Int i(int value) {
        return Int.of(value);
    }
    
    static List list(Instruction... elements) {
        return List.of(elements);
    }

    @Test
    public void test() {
        logger.info(list(i(1), list(i(2), i(3)), i(4)).toString());
    }

}
