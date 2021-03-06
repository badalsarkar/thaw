package de.be.thaw.math.mathml.typeset.element.impl;

import de.be.thaw.math.mathml.typeset.element.AbstractMathElement;
import de.be.thaw.util.Position;
import de.be.thaw.util.Size;

/**
 * A token element is a element without children and with text content.
 */
public abstract class TokenElement extends AbstractMathElement {

    /**
     * The text of the token element.
     */
    private final String text;

    /**
     * Font size of the element.
     */
    private double fontSize;

    /**
     * Kerning adjustments.
     */
    private final double[] kerningAdjustments;

    /**
     * The offset from the y-position of the element where the text baseline is.
     */
    private double baseline;

    public TokenElement(String text, Size size, double fontSize, double baseline, double[] kerningAdjustments, Position position) {
        super(position);

        setSize(size);
        this.text = text;
        this.fontSize = fontSize;
        this.baseline = baseline;
        this.kerningAdjustments = kerningAdjustments;
    }

    /**
     * Get the text.
     *
     * @return text
     */
    public String getText() {
        return text;
    }

    @Override
    public double getBaseline() {
        return baseline;
    }

    @Override
    public void setBaseline(double baseline) {
        this.baseline = baseline;
    }

    /**
     * Get the font size to display the text with.
     *
     * @return font size
     */
    public double getFontSize() {
        return fontSize;
    }

    /**
     * Get kerning adjustments of the text.
     *
     * @return kerning adjustments
     */
    public double[] getKerningAdjustments() {
        return kerningAdjustments;
    }

    @Override
    public void scale(double factor) {
        super.scale(factor);

        fontSize *= factor;
        baseline *= factor;
        for (int i = 0; i < kerningAdjustments.length; i++) {
            kerningAdjustments[i] *= factor;
        }
    }

}
