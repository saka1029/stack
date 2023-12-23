package test.saka1029.stack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Test;

import saka1029.Common;
import saka1029.stack.Int;
import saka1029.stack.List;
import saka1029.stack.Parser;
import saka1029.stack.Quote;

public class TestParser {

    static final Logger logger = Common.logger(TestParser.class);

    @Test
    public void testReadInt() throws IOException {
        logger.info(Common.methodName());
        String text = "  1  2";
        Parser r = Parser.of(text);
        assertEquals(Int.of(1), r.read());
        assertEquals(Int.of(2), r.read());
        assertNull(r.read());
    }

    @Test
    public void testReadList() throws IOException {
        logger.info(Common.methodName());
        String text = "  ()  (1)  (1 2)";
        Parser r = Parser.of(text);
        assertEquals(List.NIL, r.read());
        assertEquals(List.of(Int.of(1)), r.read());
        assertEquals(List.of(Int.of(1), Int.of(2)), r.read());
        assertNull(r.read());
    }

    @Test
    public void testReadQuote() throws IOException {
        logger.info(Common.methodName());
        String text = "  '0 '()  '(1)  '(1 2)";
        Parser r = Parser.of(text);
        assertEquals(Quote.of(Int.of(0)), r.read());
        assertEquals(Quote.of(List.NIL), r.read());
        assertEquals(Quote.of(List.of(Int.of(1))), r.read());
        assertEquals(Quote.of(List.of(Int.of(1), Int.of(2))), r.read());
        assertNull(r.read());
    }
}
