package test.saka1029.stack;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Test;

import saka1029.Common;
import saka1029.stack.Cons;
import saka1029.stack.Int;
import saka1029.stack.List;
import saka1029.stack.Parser;
import saka1029.stack.Quote;

public class TestParser {

    static final Logger logger = Common.logger(TestParser.class);

    @Test
    public void testReadInt() throws IOException {
//        logger.info(Common.methodName());
        String text = "  1  2";
        Parser r = Parser.parse(text);
        assertEquals(Cons.list(Int.of(1), Int.of(2)), r.read());
    }

    @Test
    public void testReadList() throws IOException {
//        logger.info(Common.methodName());
        String text = "  ()  (1)  (1 2)";
        Parser r = Parser.parse(text);
        assertEquals(Cons.list(
            List.NIL,
            Cons.list(Int.of(1)),
            Cons.list(Int.of(1), Int.of(2))),
            r.read());
    }

    @Test
    public void testReadQuote() throws IOException {
//        logger.info(Common.methodName());
        String text = "  '0 '()  '(1)  '(1 2)";
        Parser r = Parser.parse(text);
        assertEquals(Cons.list(
            Quote.of(Int.of(0)),
            Quote.of(List.NIL),
            Quote.of(Cons.list(Int.of(1))),
            Quote.of(Cons.list(Int.of(1), Int.of(2)))),
            r.read());
    }
}
