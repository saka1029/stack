package saka1029.stack;

import java.util.NoSuchElementException;

public interface Collection {

    Iterator iterator();
    
    default Iterable<Instruction> iterable() {
        return () -> {
            Iterator it = iterator();
            return new java.util.Iterator<>() {
                Instruction ins = it.next();

                @Override
                public boolean hasNext() {
                    return ins != null;
                }

                @Override
                public Instruction next() {
                    if (ins == null)
                        throw new NoSuchElementException();
                    Instruction result = ins;
                    ins = it.next();
                    return result;
                }
            };
        };
    }
}
