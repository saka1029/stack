package saka1029.type;

public class Bind {
    
    public final VariableType key;
    public final Type value;
    public final Bind next;
    
    Bind(VariableType key, Type value, Bind next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }
    
    public static Bind of(VariableType key, Type value, Bind next) {
        return new Bind(key, value, next);
    }

    public static final Bind EMPTY = new Bind(null, null, null);

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        String sep = "";
        for (Bind e = this; e != EMPTY; e = e.next, sep = ", ")
            sb.append(sep).append(key).append("=").append(value);
        sb.append("]");
        return sb.toString();
    }
}
