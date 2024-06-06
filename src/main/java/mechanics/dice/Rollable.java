package mechanics.dice;

/**
 * Interface for dice that can be rolled.
 */
public interface Rollable {
    /**
     * Rolls the dice and returns the result, including the rollAfter result in most cases.
     * @return The result of the roll.
     */
    int roll();

    /**
     * Rolls the constant value of the Composite
     * @return The constant value of the Composite
     */
    int rollAfter();

    /**
     * Sets the constant value of the Composite
     * @param constant The constant value to set
     */
    void setConstant(int constant);
}
