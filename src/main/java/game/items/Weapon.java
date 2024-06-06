package game.items;

import game.entities.Ability;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Weapon class containing information for weapons in the game.
 */
public class Weapon {
    /**
     * Enumeration for different weapon groups that can be classified as proficient.
     */
    public enum Group {
        SIMPLE, MARTIAL, FIREARM, IMPROVISED;

        /**
         * Returns the weapon group based on the weapon type.
         *
         * @param type Weapon type to determine group.
         * @return Group of the weapon type.
         */
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

    /**
     * Enumeration of weapon properties that can be applied to weapons.
     */
    public enum Property {
        RANGED, REACH, THROWN, VERSATILE, FINESSE, HEAVY, LIGHT, LOADING,
        SPECIAL, TWO_HANDED, AMMUNITION;

        /**
         * Sorts the properties in a sorted set.
         *
         * @param properties List of properties to sort.
         * @return Sorted set of properties.
         */
        public static SortedSet<Property> sort(List<Property> properties) {
            TreeSet<Property> sorted = new TreeSet<>();
            sorted.addAll(properties);
            return sorted;
        }

        /**
         * Returns the abilities that are used for the weapon based on the properties.
         *
         * @param properties Properties of the weapon.
         * @return Sorted set of abilities used for the weapon.
         */
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
