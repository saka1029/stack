package saka1029.stack;

public class DummyInstruction implements Instruction {

    public final String name;

    public DummyInstruction(String name) {
        this.name = name;
    }

    @Override
    public void execute(Context context) {
        // do nothing
    }

    @Override
    public String toString() {
        return name;
    }

}
