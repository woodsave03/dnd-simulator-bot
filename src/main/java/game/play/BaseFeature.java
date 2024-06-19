package game.play;

import communication.Pair;
import game.entities.Ability;
import mechanics.Construct;
import mechanics.Constructor;
import mechanics.actions.Skill;
import mechanics.dice.Die;

public class BaseFeature extends ClassFeatureComposite implements Construct {
    private final Die hitDie;
    private final Pair<Ability.Type, Ability.Type> saveProficiencies;
    private final Skill[] skillOptions;
    private final Skill[] skillProficiencies;

    public BaseFeature(Builder builder) {
        super(1);
        this.hitDie = builder.hitDie;
        this.saveProficiencies = builder.saveProficiencies;
        this.skillOptions = builder.skillOptions;
        this.skillProficiencies = builder.skillProficiencies;
    }

    public int getStartingHP() {
        return hitDie.getSides();
    }

    @Override
    String display() {
        return "";
    }

    @Override
    public Builder deconstruct() {
        return null;
    }

    public static class Builder implements Constructor {
        private Die hitDie;
        private Pair<Ability.Type, Ability.Type> saveProficiencies;
        private Skill[] skillOptions;
        private int skillCount = -1;
        private Skill[] skillProficiencies;

        public Builder with(Die hitDie) {
            this.hitDie = hitDie;
            return this;
        }

        public Builder with(Ability.Type save1, Ability.Type save2) {
            this.saveProficiencies = new Pair<>(save1, save2);
            return this;
        }

        public Builder with(Skill... skillOptions) {
            this.skillOptions = skillOptions;
            return this;
        }

        public Builder with(int skillCount) {
            this.skillCount = skillCount;
            return this;
        }

        public Builder choose(Skill... skillProficiencies) {
            if (skillOptions == null) {
                throw new IllegalArgumentException("Must specify skill options before choosing proficiencies");
            } else if (skillCount == -1) {
                throw new IllegalArgumentException("Must specify number of skill proficiencies before choosing them");
            } else if (skillProficiencies.length != skillCount) {
                throw new IllegalArgumentException("Must choose exactly " + skillCount + " skill proficiencies");
            }

            this.skillProficiencies = skillProficiencies;
            return this;
        }

        @Override
        public Construct build() {
            return new BaseFeature(this);
        }
    }
}
