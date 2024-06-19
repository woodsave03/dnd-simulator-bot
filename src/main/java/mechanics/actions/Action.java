package mechanics.actions;

import game.entities.Creature;

/**
 * An action that a creature can take during its turn.
 * List of actions:
 * - Attack
 * - CastSpell
 * - Dash
 * - Disengage
 * - Dodge
 * - Help
 * - Hide
 * - Ready
 * - Search
 * - UseObject
 */
public abstract class Action {
    private final String name;
    private String description;
    private Type type;


    public Action(String name) {
        this.name = name;
    }

    public enum Type {
        ACTION, BONUS_ACTION, REACTION, FREE_ACTION, NON_COMBAT
    }

    public Type getType() {
        return type;
    }

    abstract void activate(Creature creature);
}
