package saka1029.stack;

public interface List extends Instruction, Collection {
    
    public static final List NULL = new List() {

        @Override
        public Iter iter() {
            return () -> null;
        }
        
        @Override
        public void execute(Context context) {
            // TODO Auto-generated method stub
            
        }
    };

}
