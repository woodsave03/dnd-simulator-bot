package game.play;

import mechanics.actions.Action;

public class ActionFeature extends ClassFeatureComposite {
    private Action action;

    public ActionFeature(int level, Action action) {
        super(level);
        this.action = action;
    }

    @Override
    String display() {
        return getDescription();
    }

    public Action.Type getType() {
        return action.getType();
    }


}
