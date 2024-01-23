package saka1029.stack;

import java.util.Objects;

public class Range extends List {
    
    final int start, end, step;
    
    Range(int start, int end, int step) {
        if (step == 0)
            throw new IllegalArgumentException("step == 0");
        this.start = start;
        this.end = end;
        this.step = step;
    }
    
    public static Range of(int start, int end, int step) {
        return new Range(start, end, step);
    }
    
    public static Range of(int start, int end) {
        return new Range(start, end, start <= end ? 1 : -1);
    }
    
    public static Range of(int end) {
        return new Range(1, end, 1);
    }

    @Override
    public Iterator iterator() {
        return new Iterator() {
            
            int current = start;
            
            @Override
            public Instruction next() {
                if (step > 0 && current > end || step < 0 && current < end)
                    return null;
                int result = current;
                current += step;
                return Int.of(result);
            }
        };
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(end, start, step);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Range r && start == r.start && end == r.end && step == r.step;
    }

    @Override
    public String toString() {
        return "range(%d, %d, %d)".formatted(start, end, step);
    }
}
