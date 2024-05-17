package mechanics.dice;

public class Constant extends DiceComposite {
    public Constant(int value) {
        setConstant(value);
    }

    public Constant copy() {
        return new Constant(rollAfter());
    }

    @Override
    public String display() {
        return Integer.toString(rollAfter());
    }

    @Override
    public String toString() {
        return "Constant{" +
                "constant=" + rollAfter() +
                "}";
    }

    @Override
    public DiceComposite add(DiceComposite other) {
        if (other instanceof Constant constant)
            return new Constant(rollAfter() + constant.rollAfter());
        throw new IllegalArgumentException("Cannot add a die to a constant");
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Constant constant)
            return rollAfter() == constant.rollAfter();
        return false;
    }

    @Override
    public Constant explode() {
        throw new UnsupportedOperationException("Cannot explode a constant");
    }
}
