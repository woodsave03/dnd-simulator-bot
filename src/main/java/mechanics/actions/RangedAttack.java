package mechanics.actions;

import game.entities.Ability;
import game.items.Weapon;
import mechanics.Constructor;
import mechanics.dice.Damage;
import mechanics.RollMode;

import java.util.Set;

public class RangedAttack extends Attack {
    private static final int DISADVANTAGE_RANGE = 1;
    private int shortRange, longRange;

    public RangedAttack(Builder builder) {
        super(builder);
        this.shortRange = builder.rShort;
        this.longRange = builder.rLong;
    }

    public void setRange(int rShort, int rLong) {
        this.shortRange = rShort;
        this.longRange = rLong;
    }

    public int getShortRange() {
        return shortRange;
    }
    public void setShortRange(int shortRange) {
        this.shortRange = shortRange;
    }
    public int getLongRange() {
        return longRange;
    }
    public void setLongRange(int longRange) {
        this.longRange = longRange;
    }

    @Override
    public boolean inRange(int distance) {
        return distance <= longRange;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RangedAttack attack) {
            return super.equals(o) && attack.shortRange == shortRange && attack.longRange == longRange;
        }
        return false;
    }
    @Override
    public String display() {
        return "Ranged Attack: " + super.display() + " (" + shortRange + "/" + longRange + ")";
    }

    @Override
    public String toString() {
        return "Ranged Attack{" + substring() + "}";
    }

    @Override
    protected String substring() {
        return super.substring() + ", shortRange=" + shortRange + ", longRange=" + longRange;
    }

    @Override
    public RollMode rollMode(int distance) {
        if (!inRange(distance))
            throw new IllegalArgumentException("Target is out of range");
        if (distance == DISADVANTAGE_RANGE)
            return RollMode.DISADVANTAGE;
        return (distance <= shortRange) ? RollMode.STRAIGHT : RollMode.DISADVANTAGE;
    }

    @Override
    public Constructor builder() {
        return new Builder();
    }

    public static class Builder extends Attack.Builder {
        int rShort, rLong;
        public Builder with(int rShort, int rLong) {
            this.rShort = rShort;
            this.rLong = rLong;
            return this;
        }

        @Override
        public Builder with(Damage damage) {
            super.with(damage);
            return this;
        }

        @Override
        public Builder as(String title) {
            super.as(title);
            return this;
        }

        @Override
        public Builder with(Ability.Type type) {
            super.with(type);
            return this;
        }

        @Override
        public Builder with(Set<Ability.Type> abilities) {
            super.with(abilities);
            return this;
        }

        @Override
        public Builder with(Weapon.Group group) {
            super.with(group);
            return this;
        }

        @Override
        public RangedAttack build() {
            if (damage == null)
                throw new IllegalStateException("Damage must be set");
            if (abilities.isEmpty())
                throw new IllegalStateException("Ability must be set");
            return new RangedAttack(this);
        }
    }
}
