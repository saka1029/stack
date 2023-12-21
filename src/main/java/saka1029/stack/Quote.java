package saka1029.stack;

import java.util.Objects;

public class Quote implements Instruction {
    public final Instruction value;
    
    Quote(Instruction value) {
        this.value = value;
    }
    
    public static Quote of(Instruction value) {
        return new Quote(value);
    }

    @Override
    public void execute(Context context) {
        context.push(value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Quote q && value.equals(q.value);
    }

    @Override
    public String toString() {
        return "\'" + value;
    }
}
