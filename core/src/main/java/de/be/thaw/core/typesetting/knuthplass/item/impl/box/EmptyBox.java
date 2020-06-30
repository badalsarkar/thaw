package de.be.thaw.core.typesetting.knuthplass.item.impl.box;

import de.be.thaw.core.typesetting.knuthplass.item.impl.Box;

/**
 * An empty box.
 */
public class EmptyBox extends Box {

    /**
     * Width of the empty box.
     */
    private final double width;

    public EmptyBox(double width) {
        this.width = width;
    }

    @Override
    public double getWidth() {
        return width;
    }

}
