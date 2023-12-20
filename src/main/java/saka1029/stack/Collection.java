package saka1029.stack;

import java.util.Iterator;
import java.util.NoSuchElementException;

public interface Collection extends Iterable<Instruction> {

    Iter iter();
    
    @Override
    default Iterator<Instruction> iterator() {
        final Iter iter = iter();
        return new Iterator<>() {
            
            Instruction next = iter.next();

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public Instruction next() {
                if (next == null)
                    throw new NoSuchElementException();
                Instruction result = next;
                next = iter.next();
                return result;
            }
        };
    }

}
