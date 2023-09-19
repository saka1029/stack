package saka1029.stack;

public class Word implements Element {

    public final String name;

    Word(String name) {
        this.name = name;
    }

    public static Word of(String name) {
        return new Word(name);
    }

    @Override
    public void execute(Context context) {
        context.instructions.get(name).execute(context);
    }

    @Override
    public String toString() {
        return name;
    }
}
