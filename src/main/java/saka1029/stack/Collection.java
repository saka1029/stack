package saka1029.stack;

import java.util.NoSuchElementException;

public interface Collection {

    Iterator iterator();
    
    default Iterable<Instruction> iterable() {
        return () -> new java.util.Iterator<>() {
            final Iterator it = iterator();
            Instruction next = it.next();

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public Instruction next() {
                if (next == null)
                    throw new NoSuchElementException();
                Instruction result = next;
                next = it.next();
                return result;
            }
        };
    }
}
