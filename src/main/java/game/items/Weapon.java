package game.items;

import game.entities.Ability;
import mechanics.actions.Attack;

import java.util.*;

public class Weapon {
    private static final int HIGH = 0;
    private static final int MEDIUM = 1;
    private static final int LOW = 2;
    public enum Group {
        SIMPLE, MARTIAL, FIREARM, IMPROVISED;

        public static Group of(BaseWeapon.Type type) {
            return switch (type) {
                case CLUB, DAGGER, GREATCLUB, HANDAXE, JAVELIN, LIGHT_HAMMER, MACE,
                        QUARTERSTAFF, SICKLE, SPEAR, LIGHT_CROSSBOW, DART, SHORTBOW,
                        SLING, BOOMERANG -> SIMPLE;
                case FLAIL, GLAIVE, GREATAXE, GREATSWORD, HALBERD, LANCE, LONGSWORD,
                        MAUL, MORNINGSTAR, PIKE, RAPIER, SCIMITAR, SHORTSWORD, TRIDENT,
                        WAR_PICK, WARHAMMER, WHIP, HAND_CROSSBOW, HEAVY_CROSSBOW,
                        LONGBOW, NET -> MARTIAL;
            };
        }
    }

    public enum Property {
        RANGED, REACH, THROWN, VERSATILE, FINESSE, HEAVY, LIGHT, LOADING,
        SPECIAL, TWO_HANDED, AMMUNITION;

        public static SortedSet<Property> sort(List<Property> properties) {
            TreeSet<Property> sorted = new TreeSet<>();
            sorted.addAll(properties);
            return sorted;
        }

        public static SortedSet<Ability.Type> abilities(SortedSet<Property> properties) {
            TreeSet<Ability.Type> modifiers = new TreeSet<>();
            if (!properties.contains(RANGED))
                modifiers.add(Ability.Type.STR);
            else
                modifiers.add(Ability.Type.DEX);
            if (properties.contains(FINESSE))
                modifiers.add(Ability.Type.DEX);
            return modifiers;
        }
    }
}
