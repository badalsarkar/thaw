package de.be.thaw.style.model.style.value;

import de.be.thaw.util.unit.Unit;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Value of type integer.
 */
public class IntStyleValue extends AbstractStyleValue {

    /**
     * The integer value.
     */
    private final int value;

    /**
     * Unit of the value.
     */
    @Nullable
    private final Unit unit;

    public IntStyleValue(int value) {
        this(value, null);
    }

    public IntStyleValue(int value, @Nullable Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    @Override
    public String value() {
        return String.valueOf(value);
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public boolean booleanValue() {
        return value != 0;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public Optional<Unit> unit() {
        return Optional.ofNullable(unit);
    }

}
