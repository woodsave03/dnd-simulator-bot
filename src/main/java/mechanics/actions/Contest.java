package mechanics.actions;

import game.entities.Ability;
import game.entities.Creature;

import java.util.HashSet;
import java.util.Set;

public class Contest extends Roll {
    private Skill sourceSkill;
    private Set<Skill> targetOptions;
    private Contest(Builder builder) {
        super(builder, Type.CONTEST);
        this.sourceSkill = builder.sourceCheck;
        this.targetOptions = builder.targetOptions;
    }
    public int getSourceBonus(Creature source) {
        return source.provide(sourceSkill.ability()).modifier() +
                source.getProficiency(sourceSkill);
    }
    @Override
    public Builder builder() {
        return new Builder();
    }

    @Override
    public String display() {
        StringBuilder builder = new StringBuilder("Contest: ").append(sourceSkill).append(" vs (");
        for (Skill skill : targetOptions) {
            builder.append(skill).append(" or ");
        }
        return builder.substring(0, builder.length() - 4) + ")";
    }

    @Override
    public String toString() {
        return "Contest{" + substring() + "}";
    }

    @Override
    public String substring() {
        return super.substring() + ", sourceSkill=" + sourceSkill + ", targetOptions=" + targetOptions;
    }

    public Skill getSourceSkill() {
        return sourceSkill;
    }

    public void setSourceSkill(Skill sourceSkill) {
        this.sourceSkill = sourceSkill;
    }

    public Set<Skill> getTargetOptions() {
        return targetOptions;
    }

    public void setTargetOptions(Set<Skill> targetOptions) {
        this.targetOptions = targetOptions;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Contest contest) {
            return super.equals(other) &&
                    sourceSkill == contest.sourceSkill &&
                    targetOptions.equals(contest.targetOptions);
        }
        return false;
    }

    public static class Builder extends Roll.Builder {
        private Skill sourceCheck;
        private Set<Skill> targetOptions = new HashSet<>();
        public Builder with(Skill sourceCheck) {
            this.sourceCheck = sourceCheck;
            return this;
        }
        public Builder against(Set<Skill> targetOptions) {
            for (Skill skill : targetOptions)
                against(skill);
            return this;
        }
        public Builder against(Skill targetOption) {
            this.targetOptions.add(targetOption);
            super.with(targetOption.ability());
            return this;
        }
        public Builder with(Ability.Type ability) {
            super.with(ability);
            return this;
        }
        @Override
        public Contest build() {
            return new Contest(this);
        }
    }

    public Set<Skill> getSkillOptions() {
        return targetOptions;
    }
}
