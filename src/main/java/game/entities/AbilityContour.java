package game.entities;

import communication.Source;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Represents a character's ability contour.
 */
public class AbilityContour implements Source<Ability.Type> {
    private final Ability[] abilities = new Ability[6];

    /**
     * Constructs a new ability contour with all abilities set to 10.
     */
    public AbilityContour() {
        Iterator<Ability.Type> types = Arrays.stream(Ability.Type.values()).iterator();
        for (int i = 0; i < 6; i++) {
            abilities[i] = new Ability(types.next(), 10);
        }
    }

    /**
     * Constructs a new ability contour with the given scores.
     *
     * @param scores the scores for each ability
     */
    public AbilityContour(int[] scores) {
        if (scores.length != 6) {
            throw new IllegalArgumentException("Ability contour must have 6 scores");
        }
        Iterator<Ability.Type> types = Arrays.stream(Ability.Type.values()).iterator();
        int count = 0;
        for (int i : scores) {
            abilities[count++] = new Ability(types.next(), i);
        }
    }

    /**
     * Gets the score of the given ability.
     * @param ability the ability to get the score of
     * @return the score of the given ability
     */
    public int score(Ability.Type ability) {
        return get(ability).score();
    }

    /**
     * Gets the modifier of the given ability.
     * @param ability the ability to get the modifier of
     * @return the modifier of the given ability
     */
    public int modifier(Ability.Type ability) {
        return get(ability).modifier();
    }

    /**
     * Gets the ability of the given type.
     * @param ability the type of ability to get
     * @return the ability of the given type
     */
    public Ability get(Ability.Type ability) {
        return abilities[ability.ordinal()];
    }

    /**
     * Uses the AbilityContour as a decision Source to resolve a choice between abilities.
     * @param options The options to choose from.
     * @return The highest Ability.
     */
    @Override
    public Ability resolve(Iterator<Ability.Type> options) {
        Ability.Type result = options.next();
        while (options.hasNext()) {
            Ability.Type next = options.next();
            if (score(next) > score(result)) {
                result = next;
            }
        }
        return provide(result);
    }

    /**
     * Provides an ability of the given type.
     * @param type The type of attribute to provide.
     * @return The ability of the given type.
     */
    @Override
    public Ability provide(Ability.Type type) {
        return get(type);
    }

    /**
     * Gets the type of the highest ability.
     * @param options The options to choose from.
     * @return The type of the highest ability.
     */
    @Override
    public Ability.Type type(Iterator<Ability.Type> options) {
        return resolve(options).type();
    }
}
