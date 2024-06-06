package mechanics.dice;

/**
 * A constant value that only occurs when there are no dice in the expression.
 * The usage of this class has been minimized as much as possible. When added to other DiceComposites,
 * it will simply be converted to an integer to be used in the rollAfter() method.
 */
public class Constant extends DiceComposite {
    /**
     * Constructor for a constant value.
     * @param value The value of the constant.
     */
    public Constant(int value) {
        setConstant(value);
    }

    /**
     * Copy constructor for a constant value.
     * @return A new constant with the same value as the original.
     */
    public Constant copy() {
        return new Constant(rollAfter());
    }

    /**
     * Returns a readable string of the constant value.
     * @return The constant value as a string.
     */
    @Override
    public String display() {
        return Integer.toString(rollAfter());
    }

    /**
     * Returns the JSON representation of the constant value.
     * @return The constant value as a JSON string.
     */
    @Override
    public String toString() {
        return "Constant{" +
                "constant=" + rollAfter() +
                "}";
    }

    /**
     * Adds a DiceComposite and this constant together to rreturn a DiceComposite with both values.
     * @param other The other DiceComposite to add to this constant.
     * @return The DiceComposite formed from the addition.
     */
    @Override
    public DiceComposite add(DiceComposite other) {
        if (other instanceof Constant constant) {
            setConstant(rollAfter() + constant.rollAfter());
            return this;
        }
        return other.add(this);
    }

    /**
     * Compares this constant to another object to see if they are equal.
     * @param other The other object to compare to this constant.
     * @return True if the other object is a constant with the same value, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Constant constant)
            return rollAfter() == constant.rollAfter();
        return false;
    }

    /**
     * Implementation of superclass method that does nothing for a constant.
     * @throws UnsupportedOperationException Cannot explode a constant.
     */
    @Override
    public Constant explode() {
        throw new UnsupportedOperationException("Cannot explode a constant");
    }
}
