package de.be.thaw.math.mathml.typeset.impl.handler;

import de.be.thaw.font.util.KernedSize;
import de.be.thaw.math.mathml.tree.node.MathMLNode;
import de.be.thaw.math.mathml.tree.node.impl.OperatorNode;
import de.be.thaw.math.mathml.typeset.element.MathElement;
import de.be.thaw.math.mathml.typeset.element.impl.OperatorElement;
import de.be.thaw.math.mathml.typeset.exception.TypesetException;
import de.be.thaw.math.mathml.typeset.impl.MathTypesetContext;
import de.be.thaw.math.mathml.typeset.util.MathVariantUtil;
import de.be.thaw.util.Position;
import de.be.thaw.util.Size;

import java.util.HashMap;
import java.util.Map;

/**
 * Handler dealing with operator nodes.
 */
public class OperatorNodeHandler implements MathMLNodeHandler {

    /**
     * Replacements for certain characters.
     */
    private static final Map<Integer, Character> OPERATOR_REPLACEMENT_MAP = new HashMap<>();

    static {
        OPERATOR_REPLACEMENT_MAP.put((int) '-', '\u2212');
        OPERATOR_REPLACEMENT_MAP.put((int) '*', '\u2219');
        OPERATOR_REPLACEMENT_MAP.put((int) '/', '\u2236');
    }

    @Override
    public String supportedNodeName() {
        return "mo";
    }

    @Override
    public MathElement handle(MathMLNode node, MathTypesetContext ctx) throws TypesetException {
        OperatorNode mo = (OperatorNode) node;

        String operator = mo.getOperator();

        // Convert operator to the correct font variant (math variant)
        operator = MathVariantUtil.convertStringUsingMathVariant(operator, mo.getMathVariant());

        // Replace certain characters
        StringBuilder builder = new StringBuilder();
        int len = operator.length();
        for (int i = 0; i < len; ) {
            int codePoint = operator.codePointAt(i);
            i += Character.charCount(codePoint);

            if (OPERATOR_REPLACEMENT_MAP.containsKey(codePoint)) {
                builder.append(OPERATOR_REPLACEMENT_MAP.get(codePoint));
            } else {
                builder.append(Character.toChars(codePoint));
            }
        }
        operator = builder.toString();

        // TODO Deal with mathsize (once attribute is parsed)

        KernedSize size;
        try {
            size = ctx.getConfig().getFont().getKernedStringSize(-1, operator, ctx.getLevelAdjustedFontSize());
        } catch (Exception e) {
            throw new TypesetException(e);
        }

        Position position = new Position(ctx.getCurrentX(), ctx.getCurrentY());
        ctx.setCurrentX(position.getX() + size.getWidth());

        return new OperatorElement(operator, ctx.getLevelAdjustedFontSize(), new Size(size.getWidth(), size.getAscent()), size.getKerningAdjustments(), position);
    }

}
