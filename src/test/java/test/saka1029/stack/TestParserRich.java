package test.saka1029.stack;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import saka1029.stack.ParserRich;

public class TestParserRich {

    @Test
    public void testToken() {
        ParserRich parser = ParserRich.of("  (abc:123, 4f@def789  '&&)");
        assertEquals(ParserRich.Type.LP, parser.token());
        assertEquals(ParserRich.Type.ID, parser.token()); assertEquals("abc", parser.string);
        assertEquals(ParserRich.Type.COLON, parser.token());
        assertEquals(ParserRich.Type.INT, parser.token()); assertEquals("123", parser.string);
        assertEquals(ParserRich.Type.COMMA, parser.token());
        assertEquals(ParserRich.Type.ID, parser.token()); assertEquals("4f", parser.string);
        assertEquals(ParserRich.Type.AT, parser.token());
        assertEquals(ParserRich.Type.ID, parser.token()); assertEquals("def789", parser.string);
        assertEquals(ParserRich.Type.QUOTE, parser.token());
        assertEquals(ParserRich.Type.ID, parser.token()); assertEquals("&&", parser.string);
        assertEquals(ParserRich.Type.RP, parser.token());
    }

}
