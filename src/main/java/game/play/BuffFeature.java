package game.play;

import game.entities.Creature;

public abstract class BuffFeature extends ClassFeatureComposite {

    public BuffFeature(int level) {
        super(level);
    }

    public abstract void apply(Creature creature);
}
