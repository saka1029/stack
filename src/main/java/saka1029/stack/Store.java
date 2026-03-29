package saka1029.stack;

public abstract class Store implements Instruction {
    public final Symbol symbol;

    Store(Symbol symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "@ " + symbol;
    }
}
