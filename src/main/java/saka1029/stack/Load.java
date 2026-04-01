package saka1029.stack;

public abstract class Load implements Instruction {

    public final Symbol symbol;

    Load(Symbol symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol.toString();
    }
}
