package mechanics.actions;

import communication.Pair;
import game.entities.Ability;
import game.entities.Creature;
import mechanics.RollMode;

import java.util.Optional;

public class RollCommand {
    private final Roll roll;
    private Creature source;
    private Creature target;
    private Optional<Ability.Type> sourceAbility = Optional.empty();
    private Optional<Integer> setDC = Optional.empty();
    private boolean requireSource = true;
    private boolean success;
    private boolean sent = false;
    private int sourceValue;
    private int targetValue;

    public RollCommand(Roll roll) {
        this.roll = roll;
    }

    public RollCommand attach(Creature source) {
        this.source = source;
        return this;
    }

    public boolean sendTo(Creature target) {
        if (requireSource && source == null)
            throw new IllegalStateException("Source is required");
        this.target = target;
        Creature creature;
        int dc;
        boolean setToTarget;
        RollMode mode = RollMode.STRAIGHT;
        if (roll instanceof Attack attack) {
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
            dc = targetValue = target.check(target.resolveSkills(contest.getSkillOptions().iterator()), mode);
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

    public RollCommand withAbility(Ability.Type type) {
        sourceAbility = Optional.of(type);
        requireSource = true;
        return this;
    }

    public RollCommand withDC(int dc) {
        setDC = Optional.of(dc);
        requireSource = false;
        return this;
    }

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

    public int getSourceBonus() {
        if (source == null)
            throw new IllegalStateException("Source is required");
        return source.getBonus(roll);
    }

    public String display() {
        StringBuilder sb = new StringBuilder(roll.display())
                .append(switch (roll.getRollType()) {
                    case CHECK, SAVE -> " (DC " + getDC() + ")";
                    case ATTACK, CONTEST -> " (+" + getSourceBonus() + ")";
                });
        return sb.toString();
    }

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