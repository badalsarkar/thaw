package de.be.thaw.math.mathml.parser.impl.handler;

import de.be.thaw.math.mathml.parser.exception.ParseException;
import de.be.thaw.math.mathml.parser.impl.context.MathMLParseContext;
import de.be.thaw.math.mathml.tree.node.MathMLNode;
import org.jsoup.nodes.Element;

/**
 * Parse handler dealing with a specific type of a MathML tree node.
 */
public interface MathMLNodeParseHandler {

    /**
     * Get the node name this handler is able to deal with.
     *
     * @return node name
     */
    String getNodeName();

    /**
     * Try to parse the passed element to a MathML node.
     *
     * @param element to parse
     * @param ctx     context used during parsing
     * @return the parsed node
     * @throws ParseException in case the passed element could not be parsed
     */
    MathMLNode parse(Element element, MathMLParseContext ctx) throws ParseException;

}
