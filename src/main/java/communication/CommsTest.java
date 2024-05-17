package communication;

import game.entities.Ability;
import game.entities.AbilityContour;
import game.entities.Creature;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class CommsTest {
    private static Creature creature;
    private static Resolvable<Ability.Type> abilityResolvable;
    @BeforeAll
    public static void setup() {
        creature = new Creature(new AbilityContour(new int[]{15, 14, 13, 12, 10, 8}), 10, "Test Creature");
        abilityResolvable = Resolvable.of(Set.of(Ability.Type.DEX, Ability.Type.STR));
    }
    @Test
    public void testAbilityResolvable() {
        Assertions.assertEquals(Ability.Type.STR, abilityResolvable.resolve(creature).type());
    }

    @Test
    public void testAbilitySource() {
        Assertions.assertEquals(Ability.Type.STR, creature.type(abilityResolvable.options()));
    }

    @Test
    public void testSourceProvide() {
        Assertions.assertEquals(15, creature.provide(Ability.Type.STR).score());
    }
}
