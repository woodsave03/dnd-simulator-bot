package mechanics.actions;

import communication.Pair;
import communication.Resolvable;
import game.entities.Ability;
import game.entities.Creature;
import mechanics.RollMode;

import java.util.Optional;

/**
 * A RollCommand is a command that can be sent betwixt creatures within a game.
 * It is used to determine the outcome of a roll, such as an attack, save, check, or contest.
 * The RollCommand is sent from a source creature to a target creature, and the result is determined.
 */
public class RollCommand {
    // The roll to be executed
    private final Roll roll;
    // The source creature of the roll
    private Creature source;
    // The target creature of the roll
    private Creature target;
    // The ability used by the source creature
    private Optional<Ability.Type> sourceAbility = Optional.empty();
    // The DC of the roll
    private Optional<Integer> setDC = Optional.empty();
    // Whether the source creature is required
    private boolean requireSource = true;
    // Whether the source creature has succeeded
    private boolean success;
    // Whether the roll has been sent
    private boolean sent = false;
    // The value of the source creature's roll
    private int sourceValue;
    // The value of the target creature's roll or DC
    private int targetValue;

    /**
     * Constructs a RollCommand with the given roll.
     * @param roll the roll to be executed
     */
    public RollCommand(Roll roll) {
        this.roll = roll;
    }

    /**
     * Assigns the source Creature to the RollCommand, then sets the respective source ability
     * using the Communications system Visitor Pseudopattern
     * @param source the source Creature
     * @return the RollCommand with the source Creature attached
     */
    public RollCommand attach(Creature source) {
        this.source = source;
        sourceAbility = Optional.of(new Resolvable<>(roll.getAbilityOptions())
                .resolve(source.abilities()).type());
        return this;
    }

    /**
     * Sends the RollCommand to the target Creature and determines the outcome of the roll.
     *
     * @param target the target Creature
     * @return whether the roll was successful for the source Creature
     */
    public boolean sendTo(Creature target) {
        // TODO: Add functionality for arbitrary set DCs
        if (requireSource && source == null)
            throw new IllegalStateException("Source is required");
        this.target = target;
        Creature creature;
        int dc;
        boolean setToTarget;
        RollMode mode = RollMode.STRAIGHT;
        if (roll instanceof WeaponAttack attack) {
            dc = targetValue = target.getArmorClass();
            creature = source;
            setToTarget = false;
        } else if (roll instanceof Save save) {
            if (sourceAbility.isEmpty())
                throw new IllegalStateException("Save must have a source ability");
            dc = sourceValue = getDC();
            creature = target;
            setToTarget = true;
        } else if (roll instanceof Check check) {
            if (setDC.isEmpty())
                throw new IllegalStateException("Check must have a set DC");
            dc = sourceValue = getDC();
            creature = target;
            setToTarget = true;
        } else if (roll instanceof Contest contest) {
            dc = targetValue = target.check(target.type(contest.getTargetOptions().iterator()), mode);
            creature = source;
            setToTarget = false;
        } else {
            throw new IllegalArgumentException("Roll type not recognized");
        }
        sent = true;
        Pair<Boolean, Integer> result = roll.success(dc, creature, mode);
        success = result.key();
        if (setToTarget)
            targetValue = result.value();
        else sourceValue = result.value();
        return success;
    }

    /**
     * Returns the readable result of the roll.
     * @return the result of the roll
     */
    public String report() {
        if (!sent)
            throw new IllegalStateException("Roll has not been sent");
        StringBuilder sb = new StringBuilder(roll.toString()).append(": ");
        switch (roll.getRollType()) {
            case ATTACK -> sb.append(source.getName()).append(success ? " hits " : " misses ")
                    .append(" against ").append(target.getName()).append(" (AC ").append(targetValue).append(")");
            case SAVE -> sb.append(target.getName()).append(success ? " saves " : " fails ")
                    .append(" against ").append(source.getName()).append("'s ").append(sourceAbility.get())
                    .append(" save (DC ").append(sourceValue).append(")");
            case CHECK -> sb.append(target.getName()).append(success ? " passes " : " fails ")
                    .append(" against ").append(((Check) roll).getSkill()).append(" check (DC ")
                    .append(sourceValue).append(")");
            case CONTEST -> sb.append(source.getName()).append(success ? " beats " : " loses to ")
                    .append(target.getName()).append(" in a skill contest (DC ").append(targetValue).append(")");
        }
        return sb.toString();
    }

    /**
     * Sets an arbitrary DC for the roll.
     * @param dc the DC to set
     * @return the RollCommand with the DC set
     */
    public RollCommand withDC(int dc) {
        setDC = Optional.of(dc);
        requireSource = false;
        return this;
    }

    /**
     * Returns the DC of the roll.
     * @return the DC of the roll
     * @throws IllegalStateException if the roll is an attack or contest, or the source is not set correctly
     */
    public int getDC() {
        if (roll.getRollType() == Roll.Type.ATTACK || roll.getRollType() == Roll.Type.CONTEST)
            throw new IllegalStateException("DC is not required");
        if (requireSource) {
            if (source == null)
                throw new IllegalStateException("Source is required");
            return source.getDC(sourceAbility.get());
        } else {
            if (setDC.isEmpty())
                throw new IllegalStateException("DC is required");
            return setDC.get();
        }
    }

    /**
     * Returns the bonus of the source Creature for the roll.
     * @return the bonus of the source Creature for the roll
     * @throws IllegalStateException if the source is not set
     */
    public int getSourceBonus() {
        if (source == null)
            throw new IllegalStateException("Source is required");
        return source.getBonus(roll);
    }

    /**
     * Returns a readable string representation of the RollCommand.
     * @return a string representation of the RollCommand
     */
    public String display() {
        StringBuilder sb = new StringBuilder(roll.display())
                .append(switch (roll.getRollType()) {
                    case CHECK, SAVE -> " (DC " + getDC() + ")";
                    case ATTACK, CONTEST -> " (+" + getSourceBonus() + ")";
                });
        return sb.toString();
    }

    /**
     * Compares this RollCommand to another object.
     * @param o the object to compare to the RollCommand
     * @return true if the object is a RollCommand with the same fields, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        boolean equals = false;
        if (o instanceof RollCommand rollCommand) {
            equals = roll.equals(rollCommand.roll) && sourceAbility.equals(rollCommand.sourceAbility)
                    && setDC.equals(rollCommand.setDC) && requireSource == rollCommand.requireSource;
            if (sent) {
                // TODO: Add Creature.equals()
                equals = equals && success == rollCommand.success && sourceValue == rollCommand.sourceValue
                        && targetValue == rollCommand.targetValue;
            }
        }
        return equals;
    }

    /**
     * Returns a copy of the RollCommand.
     * @return a copy of the RollCommand
     */
    public RollCommand copy() {
        RollCommand copy = new RollCommand(roll);
        copy.source = source;
        copy.target = target;
        copy.sourceAbility = sourceAbility;
        copy.setDC = setDC;
        copy.requireSource = requireSource;
        copy.success = success;
        copy.sent = sent;
        copy.sourceValue = sourceValue;
        copy.targetValue = targetValue;
        return copy;
    }

    /**
     * Returns a JSON representation of the RollCommand.
     * @return a JSON representation of the RollCommand
     */
    @Override
    public String toString() {
        return "RollCommand{" +
                "roll=" + roll +
                ", source=" + source +
                ", target=" + target +
                ", sourceAbility=" + sourceAbility +
                ", setDC=" + setDC +
                ", requireSource=" + requireSource +
                ", success=" + success +
                ", sent=" + sent +
                ", sourceValue=" + sourceValue +
                ", targetValue=" + targetValue +
                "}";
    }
}