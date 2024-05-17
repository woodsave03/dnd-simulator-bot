package mechanics.actions;

import communication.Pair;
import game.entities.Creature;
import mechanics.Construct;
import mechanics.Constructor;
import mechanics.RollMode;
import mechanics.dice.Die;
import mechanics.dice.Rollable;
import game.entities.Ability;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/*
 * Roll Types (4):
 *  - Attack
 *  - Save
 *  - Check
 *  - Contest
 */
public abstract class Roll implements Rollable, Construct, Serializable {
    private static final Die d20 = Die.Factory.d20();
    private Set<Ability.Type> abilityOptions;
    private Type rollType;

    public Roll(Builder builder, Type type) {
        this.abilityOptions = builder.abilities;
        this.rollType = type;
    }

    @Override
    public void setConstant(int constant) {
        throw new UnsupportedOperationException("Rolls cannot have constants");
    }
    @Override
    public int roll() {
        return d20.roll();
    }
    public int rollWith(RollMode mode) {
        return switch (mode) {
            case STRAIGHT -> roll();
            case ADVANTAGE -> Math.max(roll(), roll());
            case DISADVANTAGE -> Math.min(roll(), roll());
        };
    }
    @Override
    public int rollAfter() {
        throw new UnsupportedOperationException("Rolls cannot have after modifiers");
    }
    public Set<Ability.Type> getAbilityOptions() {
        return abilityOptions;
    }
    public void setAbilityOptions(Set<Ability.Type> abilityOptions) {
        this.abilityOptions = abilityOptions;
    }
    public Pair<Boolean, Integer> success(int dc, int bonus, RollMode mode) {
        int roll = rollWith(mode) + bonus;
        return new Pair<>(roll >= dc, roll);
    }
    public Type getRollType() {
        return rollType;
    }
    public void setRollType(Type rollType) {
        this.rollType = rollType;
    }
    public abstract String display();

    @Override
    public String toString() {
        return "Roll{" +
                substring() +
                '}';
    }

    protected String substring() {
        return "abilityOptions=" + abilityOptions +
                ", rollType=" + rollType;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Roll roll) {
            return abilityOptions.equals(roll.abilityOptions) &&
                    rollType == roll.rollType;
        }
        return false;
    }

    public static abstract class Builder implements Constructor {
        protected Set<Ability.Type> abilities = new HashSet<>();

        public Builder with(Set<Ability.Type> abilities) {
            assert !abilities.isEmpty();
            this.abilities.addAll(abilities);
            return this;
        }

        public Builder with(Ability.Type type) {
            this.abilities.add(type);
            return this;
        }
    }

    public enum Type {
        ATTACK, SAVE, CHECK, CONTEST;
    }
}
