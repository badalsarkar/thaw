package de.be.thaw.text.parser;

import de.be.thaw.text.model.TextModel;
import de.be.thaw.text.parser.exception.ParseException;
import de.be.thaw.text.tokenizer.TextTokenizer;
import de.be.thaw.text.tokenizer.exception.TokenizeException;
import de.be.thaw.text.tokenizer.token.Token;
import de.be.thaw.text.tokenizer.util.result.Result;

import java.io.*;

/**
 * Parser for the thaw document text format.
 */
public class TextParser {

    public static void main(String[] args) {
        // TODO just for testing -> Remove later!
        String filePath = args[5];

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))))) {
            new TextParser().parse(br);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse the text readable using the passed reader.
     *
     * @param reader to use during parsing
     * @return the parsed text model
     * @throws ParseException in case parsing failed
     */
    public TextModel parse(Reader reader) throws ParseException {
        try {
            TextTokenizer tokenizer = new TextTokenizer(reader);

            while (tokenizer.hasNext()) {
                Result<Token, TokenizeException> r = tokenizer.next();
                if (r.isError()) {
                    throw new ParseException(r.error());
                }

                onNextToken(r.result());
            }
        } catch (TokenizeException e) {
            throw new ParseException(e);
        }

        return null;
    }

    /**
     * Process the next token.
     *
     * @param token to process
     * @throws ParseException in case something went wrong during parsing
     */
    private void onNextToken(Token token) throws ParseException {
        // TODO
        System.out.println(token);
    }

}
