package mechanics.actions;

import game.entities.Ability;

import java.util.Set;

public class CustomCheck extends Check {
    private final Ability.Type overrideAbility;

    public CustomCheck(Skill skill, Ability.Type overrideAbility) {
        super(new Check.Builder().with(skill));
        this.overrideAbility = overrideAbility;
    }

    @Override
    public Set<Ability.Type> getAbilityOptions() {
        return Set.of(overrideAbility);
    }
}
