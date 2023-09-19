package saka1029.type;

public class VariableType implements Type {

    final String name;
    
    VariableType(String name) {
        this.name = name;
    }
    
    public static VariableType of(String name) {
        return new VariableType(name);
    }
    
    @Override
    public String toString() {
        return name;
    }
}
