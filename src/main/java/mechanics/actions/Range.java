package mechanics.actions;

import mechanics.RollMode;

import java.io.Serializable;
import java.util.Optional;

/**
 * A Range object represents the range of an attack or spell.
 * It has a short range, and optionally a long range and a reach.
 *
 * @see game.items.Weapon
 */
public class Range implements Serializable {
    // The short range of the attack
    private int shortRange;
    // The long range of the attack
    private Optional<Integer> longRange = Optional.empty();

    /**
     * Constructs a Range object with a short range of 1.
     */
    public Range() {
        this.shortRange = 1;
    }

    /**
     * Constructs a Range object with the given reach.
     * @param reach the reach of the attack
     */
    public Range(int reach) {
        this.shortRange = reach;
    }

    /**
     * Constructs a Range object with the given short and long range.
     * @param rShort the short range of the attack
     * @param rLong the long range of the attack
     */
    public Range(int rShort, int rLong) {
        this(rShort);
        this.longRange = Optional.of(rLong);
    }

    /**
     * Returns whether the attack has a long range.
     *
     * @return true if the attack has a long range, false otherwise
     */
    public boolean isRanged() {
        return longRange.isPresent();
    }

    /**
     * Returns whether the attack has reach.
     *
     * @return true if the attack has reach, false otherwise
     */
    public boolean isReach() {
        return shortRange > 1;
    }

    /**
     * Returns whether the attack is a melee attack.
     *
     * @return true if the attack is melee, false otherwise
     */
    public boolean isMelee() {
        return longRange.isEmpty();
    }

    /**
     * Returns the long range value of the attack
     *
     * @return the long range value of the attack
     */
    public int getLongRange() {
        if (longRange.isEmpty()) {
            throw new IllegalStateException("This attack does not have a long range");
        }
        return longRange.get();
    }

    /**
     * Sets the long range of the attack.
     *
     * @param longRange the long range to set
     */
    public void setLongRange(int longRange) {
        this.longRange = Optional.of(longRange);
    }

    /**
     * Returns the short range of the attack.
     *
     * @return the short range of the attack
     */
    public int getShortRange() {
        return shortRange;
    }

    /**
     * Sets the short range of the attack.
     *
     * @param shortRange the short range to set
     */
    public void setShortRange(int shortRange) {
        this.shortRange = shortRange;
    }

    /**
     * Returns the JSON string representation of the Range object.
     * @return the JSON string representation of the Range object
     */
    @Override
    public String toString() {
        return "Range{" +
                "shortRange=" + shortRange +
                ", longRange=" + longRange +
                '}';
    }

    /**
     * Returns whether the inputted distance is within range.
     *
     * @param distance the distance to check
     * @return true if the distance is within range, false otherwise
     */
    public boolean inRange(int distance) {
        if (longRange.isPresent()) {
            return distance <= longRange.get();
        }
        return distance <= shortRange;
    }

    /**
     * Returns the RollMode of the attack at the given distance.
     *
     * @param distance the distance to get the RollMode for
     * @return the RollMode of the attack at the given distance
     */
    public RollMode rollMode(int distance) {
        if (!inRange(distance)) {
            throw new IllegalArgumentException("Target is out of range");
        }
        if (isRanged()) {
            if (distance == 1 || distance > shortRange) {
                return RollMode.DISADVANTAGE;
            }
        }
        return RollMode.STRAIGHT;
    }

    /**
     * Compares the Range object to another object.
     *
     * @param o the object to compare to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Range range) {
            return shortRange == range.shortRange &&
                    longRange.equals(range.longRange);
        }
        return false;
    }
}
