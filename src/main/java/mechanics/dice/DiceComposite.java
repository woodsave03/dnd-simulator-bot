package mechanics.dice;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * A composite of dice that can be rolled.
 * Uses the Composite Java Design Pattern
 */
public abstract class DiceComposite implements Rollable, Serializable {
    private final List<DiceComposite> children = new LinkedList<>();
    private int constant = 0;

    /**
     * Add the constant to the total roll
     * @return the constant to add to the total roll
     */
    @Override
    public int rollAfter() {
        return constant;
    }

    /**
     * Roll the dice and return the total
     * @return the total of the dice roll
     */
    @Override
    public int roll() {
        int total = 0;
        for (DiceComposite die : children) {
            total += die.roll();
        }
        return total + rollAfter();
    }

    /**
     * Add a die to the composite
     * @param other the die to add
     * @return the composite with the die added
     */
    public DiceComposite add(DiceComposite other) {
        if (other instanceof Constant constant)
            add(constant.rollAfter());
        else
            children.add(other);
        return this;
    }

    /**
     * Add a constant to the composite
     * @param constant the constant to add
     * @return the composite with the constant added
     */
    public DiceComposite add(int constant) {
        this.constant += constant;
        return this;
    }

    /**
     * Convert to a JSON string
     * @return the JSON string
     */
    @Override
    public String toString() {
        return "DiceComposite{" +
                substring() +
                '}';
    }

    /**
     * Helper method for toString to avoid code duplication
     * @return the substring of the toString method
     */
    protected String substring() {
        return "children=" + children +
                ", constant=" + constant;
    }

    /**
     * Set the children of the composite
     * @param children the children to set
     */
    public void setChildren(List<DiceComposite> children) {
        this.children.clear();
        this.children.addAll(children);
    }

    /**
     * Get the constant of the composite
     * @return the constant
     */
    public int getConstant() {
        return constant;
    }

    /**
     * Set the constant of the composite
     * @param constant the constant to set
     */
    public void setConstant(int constant) {
        this.constant = constant;
    }

    /**
     * Subtract the given composite from this composite
     * @param other the composite to subtract
     * @return the difference between the two composites
     */
    public int minus(DiceComposite other) {
        return roll() - other.roll();
    }

    /**
     * Return the double damage of the roll
     * @return the double damage of the roll
     */
    public int doubleDamage() {
        return roll() * 2;
    }

    /**
     * Return the half damage of the roll
     * @return the half damage of the roll
     */
    public int halfDamage() {
        return roll() / 2;
    }

    /**
     * Return the advantage of the roll
     * @return the advantage of the roll
     */
    public int advantage() {
        int first = roll();
        int second = roll();
        return Math.max(first, second);
    }

    /**
     * Return the disadvantage of the roll
     * @return the disadvantage of the roll
     */
    public int disadvantage() {
        int first = roll();
        int second = roll();
        return Math.min(first, second);
    }

    /**
     * Getter for the children of the composite
     * @return the children of the composite
     */
    public List<DiceComposite> getChildren() {
        return children;
    }

    /**
     * Convert the composite to a damage object
     * @param type the type of damage
     * @return the damage object with the composite as the damage
     */
    public Damage of(Damage.Type type) {
        return new Damage.Builder()
                .with(type)
                .with(this)
                .build();
    }

    /**
     * Display the composite as a readable string
     * @return the string representation of the composite
     */
    public String display() {
        StringBuilder sb = new StringBuilder();
        for (DiceComposite die : children) {
            sb.append(die.display()).append(" + ");
        }
        return sb.substring(0, sb.length() - 3);
    }

    /**
     * Explode the dice
     * ex.: 6d6 -> 6d8
     * @return the exploded dice
     */
    public abstract DiceComposite explode();
}
