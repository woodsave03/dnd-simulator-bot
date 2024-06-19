package game.items;

import communication.Resolvable;
import game.Coin;
import game.CoinComposite;
import game.entities.Ability;
import mechanics.Construct;
import mechanics.Constructor;
import mechanics.actions.WeaponAttack;
import mechanics.dice.Damage;
import mechanics.actions.Roll;
import mechanics.actions.Range;
import mechanics.actions.Save;
import mechanics.dice.Die;
import communication.Pair;

import java.io.File;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.*;

/**
 * BaseWeapon is a class that represents a weapon in the game. It contains all the necessary
 * information to create a weapon object, including cost, weight, range, properties, and attack
 * rolls. It is a Construct and can be deconstructed into a Builder object.
 */
public class BaseWeapon extends Item implements Construct {
    protected static final String BASE_ATTACK = "Base";
    protected static final String VERSATILE_ATTACK = "Versatile";
    protected static final String THROWN_ATTACK = "Thrown";
    protected static final String RANGED_ATTACK = "Ranged";
    protected static final String SAVE = "Save";
    protected Range range;
    private final SortedSet<Weapon.Property> properties;
    private HashMap<String, Roll> rolls;
    private BaseWeapon.Type type;

    /**
     * Constructor for BaseWeapon. It takes a Builder object and a Range object to create a new
     * BaseWeapon object.
     * @param builder Builder object containing all the necessary parameters
     */
    protected BaseWeapon(Builder builder) {
        super(builder);
        this.range = builder.range;
        this.properties = builder.properties;
        this.rolls = builder.rolls;
        this.type = builder.type;
    }

    /**
     * Deconstructs the BaseWeapon object into a Builder object.
     * @return Builder object containing all the necessary parameters
     */
    @Override
    public Builder deconstruct() {
        if (range.isRanged()) {
            return new Builder().with(this.range.getShortRange(), this.range.getLongRange())
                    .with(type);
        } else if (range.getShortRange() > 1) {
            return new Builder().reach().with(type);
        } else {
            return new Builder().simpleMelee().with(type);
        }
    }

    /**
     * Returns a JSON representation of the BaseWeapon object.
     * @return JSON representation of the BaseWeapon object
     */
    @Override
    public String toString() {
        return "BaseWeapon{" + substring() + '}';
    }

    /**
     * Compares the BaseWeapon object to another object to determine if they are equal.
     * @param o Object to compare to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof BaseWeapon baseWeapon) {
            return toString().equalsIgnoreCase(baseWeapon.toString());
        }
        return false;
    }

    /**
     * Returns a substring of the BaseWeapon object.
     * @return Substring of the BaseWeapon object
     */
    @Override
    public String substring() {
        return super.substring() + ", range=" + range +
                ", properties=" + properties +
                ", rolls=" + rolls;
    }

    public String display() {
        return type.toString() + ": " + String.join(", ", rolls.keySet().stream()
                .map(key -> key + rolls.get(key).subDisplay()).toArray(String[]::new));
    }

    // Getters and setters

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public SortedSet<Weapon.Property> getProperties() {
        return properties;
    }

    public void setProperties(HashSet<Weapon.Property> properties) {
        this.properties.clear();
        this.properties.addAll(properties);
    }

    public HashMap<String, Roll> getRolls() {
        return rolls;
    }

    public void setRolls(HashMap<String, Roll> rolls) {
        this.rolls = rolls;
    }

    /**
     * Returns the base damage of the weapon.
     * @return Damage object representing the base damage of the weapon
     */
    public Damage getBaseDamage() {
        if (rolls.containsKey(BASE_ATTACK))
            return ((WeaponAttack) rolls.get(BASE_ATTACK)).getDamage();
        else if (rolls.containsKey(RANGED_ATTACK))
            return ((WeaponAttack) rolls.get(RANGED_ATTACK)).getDamage();
        return new Damage.Builder().with("0").with(Damage.Type.BLUDGEONING).build();
    }

    /**
     * Returns a Resolvable object containing the ability options for the weapon.
     * @return Resolvable object containing the ability options for the weapon
     */
    public Resolvable<Ability.Type> getAbilityResolvable() {
        return new Resolvable<Ability.Type>()
                .addIf(Ability.Type.DEX, properties.contains(Weapon.Property.RANGED)
                        || properties.contains(Weapon.Property.FINESSE))
                .addIf(Ability.Type.STR, !properties.contains(Weapon.Property.RANGED));
    }

    /**
     * Enum representing the different types of base weapons in the game.
     */
    public enum Type {
        BOOMERANG(new Damage.Builder().with(Damage.Type.BLUDGEONING).with(Die.Factory.d4()).build(),
                List.of(Weapon.Property.RANGED), 10, 1),
        CLUB(new Damage.Builder().with(Damage.Type.BLUDGEONING).with(Die.Factory.d4()).build(),
                List.of(Weapon.Property.LIGHT), 10, 2),
        HAND_CROSSBOW(new Damage.Builder().with(Damage.Type.PIERCING).with(Die.Factory.d6()).build(),
                List.of(Weapon.Property.AMMUNITION, Weapon.Property.RANGED,
                        Weapon.Property.LOADING, Weapon.Property.LIGHT), 7500, 3),
        HEAVY_CROSSBOW(new Damage.Builder().with(Damage.Type.PIERCING).with(Die.Factory.d10()).build(),
                List.of(Weapon.Property.AMMUNITION, Weapon.Property.RANGED,
                        Weapon.Property.HEAVY, Weapon.Property.LOADING,
                        Weapon.Property.TWO_HANDED), 5000, 18),
        LIGHT_CROSSBOW(new Damage.Builder().with(Damage.Type.PIERCING).with(Die.Factory.d8()).build(),
                List.of(Weapon.Property.AMMUNITION, Weapon.Property.RANGED,
                        Weapon.Property.LOADING, Weapon.Property.TWO_HANDED), 2500, 5),
        DAGGER(new Damage.Builder().with(Damage.Type.PIERCING).with(Die.Factory.d4()).build(),
                List.of(Weapon.Property.FINESSE, Weapon.Property.LIGHT,
                        Weapon.Property.THROWN), 200, 1),
        DART(new Damage.Builder().with(Damage.Type.PIERCING).with(Die.Factory.d4()).build(),
                List.of(Weapon.Property.FINESSE, Weapon.Property.THROWN), 5, 0.25f),
        FLAIL(new Damage.Builder().with(Damage.Type.BLUDGEONING).with(Die.Factory.d8()).build(),
                List.of(), 1000, 2),
        GLAIVE(new Damage.Builder().with(Damage.Type.SLASHING).with(Die.Factory.d10()).build(),
                List.of(Weapon.Property.HEAVY, Weapon.Property.REACH,
                        Weapon.Property.TWO_HANDED), 2000, 6),
        GREATAXE(new Damage.Builder().with(Damage.Type.SLASHING).with(Die.Factory.d12()).build(),
                List.of(Weapon.Property.HEAVY, Weapon.Property.TWO_HANDED), 3000, 7),
        GREATCLUB(new Damage.Builder().with(Damage.Type.BLUDGEONING).with(Die.Factory.d8()).build(),
                List.of(Weapon.Property.TWO_HANDED), 20, 10),
        GREATSWORD(new Damage.Builder().with(Damage.Type.SLASHING).with(Die.Factory.parse("2d6")).build(),
                List.of(Weapon.Property.HEAVY, Weapon.Property.TWO_HANDED), 5000, 6),
        HALBERD(new Damage.Builder().with(Damage.Type.SLASHING).with(Die.Factory.d10()).build(),
                List.of(Weapon.Property.HEAVY, Weapon.Property.REACH,
                        Weapon.Property.TWO_HANDED), 2000, 6),
        HANDAXE(new Damage.Builder().with(Damage.Type.SLASHING).with(Die.Factory.d6()).build(),
                List.of(Weapon.Property.LIGHT, Weapon.Property.THROWN), 500, 2),
        JAVELIN(new Damage.Builder().with(Damage.Type.PIERCING).with(Die.Factory.d6()).build(),
                List.of(Weapon.Property.THROWN), 50, 2),
        LANCE(new Damage.Builder().with(Damage.Type.PIERCING).with(Die.Factory.d12()).build(),
                List.of(Weapon.Property.REACH, Weapon.Property.SPECIAL), 1000, 6),
        LIGHT_HAMMER(new Damage.Builder().with(Damage.Type.BLUDGEONING).with(Die.Factory.d4()).build(),
                List.of(Weapon.Property.LIGHT, Weapon.Property.THROWN), 200, 2),
        LONGBOW(new Damage.Builder().with(Damage.Type.PIERCING).with(Die.Factory.d8()).build(),
                List.of(Weapon.Property.AMMUNITION, Weapon.Property.HEAVY,
                        Weapon.Property.TWO_HANDED, Weapon.Property.RANGED), 5000, 2),
        LONGSWORD(new Damage.Builder().with(Damage.Type.SLASHING).with(Die.Factory.d8()).build(),
                List.of(Weapon.Property.VERSATILE), 1500, 3),
        MACE(new Damage.Builder().with(Damage.Type.BLUDGEONING).with(Die.Factory.d6()).build(),
                List.of(), 500, 4),
        MAUL(new Damage.Builder().with(Damage.Type.BLUDGEONING).with(Die.Factory.parse("2d6")).build(),
                List.of(Weapon.Property.HEAVY, Weapon.Property.TWO_HANDED), 1000, 10),
        MORNINGSTAR(new Damage.Builder().with(Damage.Type.PIERCING).with(Die.Factory.d8()).build(),
                List.of(), 1500, 4),
        NET(new Damage.Builder().with(Damage.Type.BLUDGEONING).build(),
                List.of(Weapon.Property.SPECIAL, Weapon.Property.THROWN), 100, 3),
        PIKE(new Damage.Builder().with(Damage.Type.PIERCING).with(Die.Factory.d10()).build(),
                List.of(Weapon.Property.HEAVY, Weapon.Property.REACH,
                        Weapon.Property.TWO_HANDED), 500, 18),
        QUARTERSTAFF(new Damage.Builder().with(Damage.Type.BLUDGEONING).with(Die.Factory.d6()).build(),
                List.of(Weapon.Property.VERSATILE), 20, 4),
        RAPIER(new Damage.Builder().with(Damage.Type.PIERCING).with(Die.Factory.d8()).build(),
                List.of(Weapon.Property.FINESSE), 2500, 2),
        SCIMITAR(new Damage.Builder().with(Damage.Type.SLASHING).with(Die.Factory.d6()).build(),
                List.of(Weapon.Property.FINESSE, Weapon.Property.LIGHT), 2500, 3),
        SHORTBOW(new Damage.Builder().with(Damage.Type.PIERCING).with(Die.Factory.d6()).build(),
                List.of(Weapon.Property.AMMUNITION, Weapon.Property.TWO_HANDED,
                        Weapon.Property.RANGED), 2500, 2),
        SHORTSWORD(new Damage.Builder().with(Damage.Type.PIERCING).with(Die.Factory.d6()).build(),
                List.of(Weapon.Property.FINESSE, Weapon.Property.LIGHT), 1000, 2),
        SICKLE(new Damage.Builder().with(Damage.Type.SLASHING).with(Die.Factory.d4()).build(),
                List.of(Weapon.Property.LIGHT), 100, 2),
        SLING(new Damage.Builder().with(Damage.Type.BLUDGEONING).with(Die.Factory.d4()).build(),
                List.of(Weapon.Property.AMMUNITION, Weapon.Property.RANGED), 10, 0),
        SPEAR(new Damage.Builder().with(Damage.Type.PIERCING).with(Die.Factory.d6()).build(),
                List.of(Weapon.Property.THROWN, Weapon.Property.VERSATILE), 100, 3),
        TRIDENT(new Damage.Builder().with(Damage.Type.PIERCING).with(Die.Factory.d6()).build(),
                List.of(Weapon.Property.THROWN, Weapon.Property.VERSATILE), 500, 4),
        WAR_PICK(new Damage.Builder().with(Damage.Type.PIERCING).with(Die.Factory.d8()).build(),
                List.of(), 500, 2),
        WARHAMMER(new Damage.Builder().with(Damage.Type.BLUDGEONING).with(Die.Factory.d8()).build(),
                List.of(Weapon.Property.VERSATILE), 1500, 2),
        WHIP(new Damage.Builder().with(Damage.Type.SLASHING).with(Die.Factory.d4()).build(),
                List.of(Weapon.Property.FINESSE, Weapon.Property.REACH), 200, 3);

        private final Damage baseDamage;
        private final SortedSet<Weapon.Property> properties;
        private final int cost;
        private final float weight;

        /**
         * Constructor for Type enum. Accepts Damage, List of Weapon.Property, cost, and weight.
         * @param damage Damage object representing the base damage of the weapon
         * @param properties List of Weapon.Property representing the properties of the weapon
         * @param cost Cost of the weapon
         * @param weight Weight of the weapon
         */
        Type(Damage damage, List<Weapon.Property> properties, int cost, float weight) {
            this.baseDamage = damage;
            this.properties = new TreeSet<>(properties);
            this.cost = cost;
            this.weight = weight;
        }

        // Getters
        public Damage getBaseDamage() {
            return baseDamage;
        }

        public SortedSet<Weapon.Property> getProperties() {
            return properties;
        }

        public int getCost() {
            return cost;
        }

        public float getWeight() {
            return weight;
        }
    }

    /**
     * Flyweight factory class for BaseWeapon. Contains a map of weapons and a create method to create a new
     * BaseWeapon object.
     */
    public static class Factory {
        private static final Map<String, BaseWeapon> weapons = new HashMap<>();

        public static BaseWeapon create(String name) {
            BaseWeapon weapon = weapons.get(name);
            if (weapon == null) {
                try {
                    weapon = build(name);
                    weapons.put(name, weapon);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid weapon name: " + name +
                            ". Input needs to be built as custom weapon.");
                }
            }
            // TODO: Resolve custom weapon?
            return weapon;
        }

        /**
         * Builds a new BaseWeapon object based on the name of the weapon.
         * @param name Name of the weapon
         * @return BaseWeapon object representing the weapon
         * @throws IllegalArgumentException if the name is not a valid weapon name
         */
        private static BaseWeapon build(String name) throws IllegalArgumentException {
            Type type = Type.valueOf(name.toUpperCase());
            Builder builder = new Builder();
            builder = switch (type) {
                case BOOMERANG -> builder.with(60, 120);
                case HAND_CROSSBOW, JAVELIN, SLING -> builder.with(30, 120);
                case HEAVY_CROSSBOW -> builder.with(100, 400);
                case LIGHT_CROSSBOW, SHORTBOW -> builder.with(80, 320);
                case DAGGER, DART, HANDAXE, LIGHT_HAMMER, SPEAR, TRIDENT -> builder.with(20, 60);
                case GLAIVE, HALBERD, LANCE, PIKE, WHIP -> builder.reach();
                case LONGBOW -> builder.with(150, 600);
                case NET -> builder.with(5, 15);
                default -> builder.simpleMelee();
            };
            return builder.with(type).build();
        }

        public static BaseWeapon readFromTXT(String filename) {
            BaseWeapon weapon = null;
            try (Scanner scanner = new Scanner(new File(filename))) {
                if (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    weapon = create(line);
                } else
                    throw new ProtocolException("File is empty: " + filename);
            } catch (ProtocolException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println("File not found: " + filename);
            }
            return weapon;
        }
    }

    /**
     * Builder class for BaseWeapon. Contains all the necessary parameters to create a new BaseWeapon object.
     */
    protected static class Builder extends Item.Builder {
        protected SortedSet<Weapon.Property> properties;
//        private Optional<Integer> reach = Optional.empty();
//        private Optional<Pair<Integer, Integer>> range = Optional.empty();
        protected Range range;
        protected Damage damage;
        private final HashMap<String, Roll> rolls = new HashMap<>();
        private Type type;
        private boolean rangeSet = false;
        protected Weapon.Group group;
        private Set<Ability.Type> abilities = new HashSet<>();


        @Override
        public BaseWeapon build() {
            if (rolls.isEmpty())
                throw new IllegalStateException("BaseWeapon must have at least one attack roll.");
            return new BaseWeapon(this);
        }

        @Override
        public Builder as(String name) {
            super.as(name);
            return this;
        }

        /**
         * Constructs an Attack.Builder to construct a new Attack other than 'MELEE' to be added to
         * BaseWeapon.Builder's `rolls` HashMap
         *
         * @param rollName String to match one of the static final keywords in BaseWeapon
         */
        protected void addRoll(String rollName) {
            if (!rollName.equals(BASE_ATTACK) && !rollName.equals(RANGED_ATTACK) && !rollName.equals(VERSATILE_ATTACK)
                    && !rollName.equals(THROWN_ATTACK))
                throw new IllegalArgumentException("Roll name must be one of: " + BASE_ATTACK + ", "
                        + RANGED_ATTACK + ", " + VERSATILE_ATTACK + ", " + THROWN_ATTACK);
            Roll result = null;
            switch (rollName) {
                case BASE_ATTACK -> {
                    WeaponAttack.Builder builder = new WeaponAttack.Builder().with(this.damage)
                            .with(abilities);
                    if (range.isReach())
                        builder = builder.with(2);
                    result = builder.with(group).build();
                }
                case RANGED_ATTACK, THROWN_ATTACK -> {
                    WeaponAttack.Builder builder = new WeaponAttack.Builder()
                            .with(this.damage)
                            .with(abilities).with(group);
                    if (this.range.isRanged())
                        builder = builder.with(this.range.getShortRange(), this.range.getLongRange());
                    result = builder.build();
                }
                case VERSATILE_ATTACK -> {
                    WeaponAttack.Builder builder = new WeaponAttack.Builder()
                            .with(this.damage.explode())
                            .with(abilities).with(group);
                    if (this.range.isReach())
                        builder = builder.with(this.range.getShortRange());
                    result = builder.build();
                }
            }
            assert result != null;
            rolls.put(rollName, result);
        }

        /**
         * Calculates the range of the weapon based on the reach and range properties.
         */
        protected Range getRange() {
            return range;
        }

        public Builder simpleMelee() {
            range = new Range();
            rangeSet = true;
            return this;
        }

        @Override
        public Builder weigh(float weight) {
            super.weigh(weight);
            return this;
        }

        public Builder with(int cost) {
            super.with(cost);
            return this;
        }

        public Builder with(Set<Weapon.Property> properties) {
            if (!properties.isEmpty()) {
                this.properties = new TreeSet<>(properties);
                for (Weapon.Property property : this.properties) {
                    with(property);
                }
            } else
                this.properties = new TreeSet<>();
            return this;
        }

        public Builder reach() {
            if (range != null && range.isRanged())
                throw new IllegalStateException("BaseWeapon cannot have RANGED and REACH.");
            if (range == null)
                range = new Range();
            this.range.setShortRange(2);
            rangeSet = true;
            return this;
        }

        public Builder with(int rShort, int rLong) {
            if (range != null && range.isReach())
                throw new IllegalStateException("BaseWeapon cannot have REACH and RANGED.");
            else if (rShort > rLong)
                throw new IllegalArgumentException(String.format("Short range (%d) must be less than " +
                        "long range (%d)", rShort, rLong));
            assert rShort != 0 && rLong != 0;
            this.range = new Range(rShort / 5, rLong / 5);
            rangeSet = true;
            return this;
        }

        public Builder with(Range range) {
            this.range = range;
            rangeSet = true;
            return this;
        }

        /**
         * Allows the user to create a weapon simply by passing in the BaseWeapon type.
         * @param type BaseWeapon type
         * @return Builder object with the BaseWeapon type, ready to be built
         */
        public Builder with(BaseWeapon.Type type) {
            this.type = type;
            this.group = Weapon.Group.of(type);
            super.as(type.name());
            with(type.getCost());
            weigh(type.getWeight());
            with(type.getBaseDamage());
            setAbilities(Weapon.Property.abilities(type.getProperties()));
            if (type == Type.NET)
                rolls.put(SAVE, Save.Factory.create(Ability.Type.DEX, Save.Descriptor.NON_MAGICAL));
            if (!type.getProperties().contains(Weapon.Property.RANGED)) {
                addRoll(BASE_ATTACK);
            }
            with(type.getProperties());
            return this;
        }

        public Builder with(Damage damage) {
            this.damage = damage;
            return this;
        }

        public Builder with(Weapon.Property property) {
            if (!rangeSet)
                throw new IllegalStateException("Range must be set before properties.");
            switch (property) {
                case RANGED -> addRoll(RANGED_ATTACK);
                case VERSATILE -> addRoll(VERSATILE_ATTACK);
                case THROWN -> addRoll(THROWN_ATTACK);
            }
            return this;
        }

        protected void setGroup(Weapon.Group group) {
            this.group = group;
        }


        protected void setAbilities(Set<Ability.Type> abilities) {
            this.abilities = abilities;
        }

        private boolean isNotMeleeRange() {
            return properties.contains(Weapon.Property.REACH)
                    || properties.contains(Weapon.Property.RANGED)
                    || properties.contains(Weapon.Property.THROWN);
        }

        /**
         * Determines if the weapon is not a melee weapon given the weapon properties.
         * @param props Weapon properties
         * @return true if the weapon is not a melee weapon, false otherwise
         */
        public static boolean isNotMeleeRange(Weapon.Property... props) {
            Set<Weapon.Property> propertySet = new HashSet<Weapon.Property>(Arrays.asList(props));
            if (propertySet.isEmpty())
                return false;
            return propertySet.contains(Weapon.Property.REACH)
                    || propertySet.contains(Weapon.Property.RANGED)
                    || propertySet.contains(Weapon.Property.THROWN);
        }
    }
}
