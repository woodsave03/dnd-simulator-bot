package mechanics.actions;

import game.entities.Creature;

public class Dash extends Action {
    public Dash() {
        super("Dash");
    }

    @Override
    public void activate(Creature creature) {
        creature.addTemporarySpeed(creature.getSpeed(), Duration.ROUND);
    }
}
