package mechanics.dice;

public class Damage extends Sequence {
    public enum Type {
        BLUDGEONING, PIERCING, SLASHING, FIRE, COLD, LIGHTNING, POISON, ACID,
        NECROTIC, RADIANT, FORCE, PSYCHIC, THUNDER, M_BLUDGEONING, M_PIERCING, M_SLASHING;

        public boolean isMagical() {
            return switch (ordinal()) {
                case 0, 1, 2 -> false;
                default -> true;
            };
        }
    }

    private Type type;
    private boolean twice = false;

    private Damage(Builder builder) {
        super(builder);
        this.type = builder.type;
    }
    @Override
    public int roll() {
        return twice ? super.roll() + super.roll() : super.roll();
    }
    public void crit() {
        twice = true;
    }
    public void uncrit() {
        twice = false;
    }
    @Override
    public String display() {
        return super.display() + " of " + type + " damage";
    }
    @Override
    public Damage explode() {
        return new Builder().with(super.explode()).with(type).build();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Damage damage) {
            return super.equals(damage) && damage.type.equals(type);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Damage{" +
                substring() +
                '}';
    }

    @Override
    protected String substring() {
        return super.substring() +
                ", type=" + type +
                ", twice=" + twice;
    }
    public static class Builder extends Sequence.Builder {
        private Type type;

        public Builder with(Type type) {
            this.type = type;
            return this;
        }

        @Override
        public Builder with(DiceComposite die) {
            super.with(die);
            return this;
        }

        public Builder with(int numDice, int dieType) {
            return with(numDice + "d" + dieType);
        }

        public Builder with(String diceNotation) {
            return with(Die.Factory.parse(diceNotation));
        }

        @Override
        public Damage build() {
            if (type == null)
                throw new IllegalArgumentException("Damage type must be specified");
            return new Damage(this);
        }
    }
}
