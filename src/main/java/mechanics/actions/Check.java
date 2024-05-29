package mechanics.actions;

import game.entities.Creature;

import java.util.HashMap;

public class Check extends Roll {
    private Skill skill;

    protected Check(Builder builder) {
        super(builder, Type.CHECK);
        this.skill = builder.skill;
    }

    public Skill getSkill() { return skill; }
    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    @Override
    public Builder builder() {
        return new Builder();
    }

    @Override
    protected int resolveBonus(Creature creature) {
        return creature.provide(skill.ability()).modifier() + creature.getProficiency(this);
    }

    @Override
    public String display() {
        return "Check: " + skill.display();
    }

    @Override
    public String toString() {
        return "Check{" + substring() + "}";
    }

    @Override
    public String substring() {
        return super.substring() + ", skill=" + skill;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Check check) {
            return super.equals(other) &&
                    skill == check.skill;
        }
        return false;
    }

    public static class Factory {
        private static final HashMap<Skill, Check> checks = new HashMap<>();
        public static Check create(Skill skill) {
            Check check = checks.get(skill);
            if (check == null) {
                check = new Check.Builder().with(skill).build();
                checks.put(skill, check);
            }
            return check;
        }
    }

    public static class Builder extends Roll.Builder {
        private Skill skill;

        public Builder with(Skill skill) {
            this.skill = skill;
            super.with(skill.ability());
            return this;
        }

        @Override
        public Check build() {
            return new Check(this);
        }
    }
}
