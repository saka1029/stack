package test.saka1029.stack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Logger;

import org.junit.Test;

import saka1029.Common;
import saka1029.stack.Int;
import saka1029.stack.Reader;

public class TestReader {

    static final Logger logger = Common.logger(TestReader.class);

    @Test
    public void testRead() throws IOException {
        logger.info(Common.methodName());
        String source = "  1  2";
        try (java.io.Reader sr = new StringReader(source)) {
            Reader r = Reader.of(sr);
            assertEquals(Int.of(1), r.read());
            assertEquals(Int.of(2), r.read());
            assertNull(r.read());
        }
    }

}
