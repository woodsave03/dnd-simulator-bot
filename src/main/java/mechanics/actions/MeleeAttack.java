package mechanics.actions;

import game.entities.Ability;
import game.items.Weapon;
import mechanics.*;
import mechanics.dice.Damage;

import java.util.Set;

public class MeleeAttack extends Attack implements Construct {
    private static final int DEFAULT_REACH = 1;
    private int reach;

    public MeleeAttack(Builder builder) {
        super(builder);
        this.reach = builder.reach;
    }

    @Override
    public boolean inRange(int distance) {
        return distance <= reach;
    }

    @Override
    public RollMode rollMode(int distance) {
        if (!inRange(distance))
            throw new IllegalArgumentException("Target is out of range");
        return RollMode.STRAIGHT;
    }

    public int getReach() {
        return reach;
    }

    public void setReach(int reach) {
        this.reach = reach;
    }

    public MeleeAttack reach(int reach) {
        this.reach = reach;
        return this;
    }

    @Override
    public Builder builder() {
        return new Builder();
    }
    @Override
    public String display() {
        return "Melee Attack: " + super.display();
    }
    @Override
    public String toString() {
        return "MeleeAttack{" +
                substring() +
                '}';
    }

    @Override
    public String substring() {
        return super.substring() + ", reach=" + reach;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MeleeAttack attack) {
            return super.equals(o) && attack.reach == reach;
        }
        return false;
    }

    public static class Builder extends Attack.Builder {
        private int reach = DEFAULT_REACH;

        public Builder with(int reach) {
            this.reach = reach;
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
        public MeleeAttack build() {
            if (damage == null)
                throw new IllegalStateException("Damage not set");
            if (abilities.isEmpty())
                throw new IllegalStateException("Abilities not set");
            return new MeleeAttack(this);
        }
    }
}
