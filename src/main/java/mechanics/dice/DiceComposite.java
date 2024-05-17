package mechanics.dice;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public abstract class DiceComposite implements Rollable, Serializable {
    private final List<DiceComposite> children = new LinkedList<>();
    private int constant = 0;
    @Override
    public int rollAfter() {
        return constant;
    }
    @Override
    public int roll() {
        int total = 0;
        for (DiceComposite die : children) {
            total += die.roll();
        }
        return total + rollAfter();
    }
    public DiceComposite add(DiceComposite other) {
        if (other instanceof Constant constant)
            add(constant.rollAfter());
        else
            children.add(other);
        return this;
    }
    public DiceComposite add(int constant) {
        this.constant += constant;
        return this;
    }

    @Override
    public String toString() {
        return "DiceComposite{" +
                substring() +
                '}';
    }

    protected String substring() {
        return "children=" + children +
                ", constant=" + constant;
    }

    public void setChildren(List<DiceComposite> children) {
        this.children.clear();
        this.children.addAll(children);
    }
    public int getConstant() {
        return constant;
    }
    public void setConstant(int constant) {
        this.constant = constant;
    }
    public int minus(DiceComposite other) {
        return roll() - other.roll();
    }
    public int doubleDamage() {
        return roll() * 2;
    }
    public int halfDamage() {
        return roll() / 2;
    }
    public int advantage() {
        int first = roll();
        int second = roll();
        return Math.max(first, second);
    }
    public int disadvantage() {
        int first = roll();
        int second = roll();
        return Math.min(first, second);
    }
    public List<DiceComposite> getChildren() {
        return children;
    }
    public Damage of(Damage.Type type) {
        return (Damage) new Damage.Builder()
                .with(type)
                .with(this)
                .build();
    }
    public String display() {
        StringBuilder sb = new StringBuilder();
        for (DiceComposite die : children) {
            sb.append(die.display()).append(" + ");
        }
        return sb.substring(0, sb.length() - 3);
    }
    public abstract DiceComposite explode();
}
