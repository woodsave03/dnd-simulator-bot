package game.entities;

import java.util.Arrays;
import java.util.Iterator;

public class AbilityContour {
    private final Ability[] abilities = new Ability[6];

    public AbilityContour() {
        Iterator<Ability.Type> types = Arrays.stream(Ability.Type.values()).iterator();
        for (int i = 0; i < 6; i++) {
            abilities[i] = new Ability(types.next(), 10);
        }
    }

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

    public int score(Ability.Type ability) {
        return get(ability).score();
    }

    public int modifier(Ability.Type ability) {
        return get(ability).modifier();
    }

    public Ability get(Ability.Type ability) {
        return abilities[ability.ordinal()];
    }
}
