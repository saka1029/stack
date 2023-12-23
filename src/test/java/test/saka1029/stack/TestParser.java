package test.saka1029.stack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Test;

import saka1029.Common;
import saka1029.stack.Int;
import saka1029.stack.Parser;

public class TestParser {

    static final Logger logger = Common.logger(TestParser.class);

    @Test
    public void testRead() throws IOException {
        logger.info(Common.methodName());
        String text = "  1  2";
        Parser r = Parser.of(text);
        assertEquals(Int.of(1), r.read());
        assertEquals(Int.of(2), r.read());
        assertNull(r.read());
    }
}
