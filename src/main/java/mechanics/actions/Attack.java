package mechanics.actions;

import game.entities.Creature;

public class Attack extends Action {
    private AttackRoll attackRoll;

    public Attack(String name, AttackRoll attackRoll) {
        super(name);
        this.attackRoll = attackRoll;
    }

    @Override
    void activate(Creature creature) {
        creature.resolveAttack();
    }
}
