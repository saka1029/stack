package saka1029.stack;

import java.io.IOException;
import java.io.Reader;

/**
 * [Syntax]
 * element = int | string | word | '$' word
 * int     = ['-'] DIGIT { DIGIT }
 * string  = '"' CHAR '"'
 * word    = WORDCH
 * DIGIT   = '0' .. '9'
 * CHAR    = '\' ('n' | 'r' | 't' | 'f' | 'u' HEX {HEX})
 * HEX     = DIGIT | ('a' .. 'f')
 * WORDCH  = ANY - '"' - '$'
 */
public class Parser {
	
	final Reader reader;
	
	public Parser(Reader reader) {
		this.reader = reader;
	}
	
	int ch = get();
	
	int get() {
		try {
			return ch = reader.read();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	void spaces() {
		while (Character.isWhitespace(ch))
			get();
	}
	
	Int integer() {
		return null;
	}

	public Evaluable parse() throws ParseException {
		spaces();
		switch (ch) {
		case '-':
		default:
			if (Character.isDigit(ch))
				return integer();
			return null;
		}
		
	}

}
