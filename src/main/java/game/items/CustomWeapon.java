package game.items;

import game.entities.Ability;
import mechanics.actions.Range;
import mechanics.dice.Damage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ProtocolException;
import java.util.*;

/**
 * CustomWeapon is a class that extends BaseWeapon and is used to create custom weapons.
 */
public class CustomWeapon extends BaseWeapon {
    private final Weapon.Group group;

    /**
     * Builder for CustomWeapon
     * @param builder Builder object
     */
    private CustomWeapon(Builder builder) {
        super(builder);
        this.group = builder.group;
    }

    /**
     * Deconstructs the CustomWeapon object into a Builder object
     * @return Builder object with the same values as the CustomWeapon object
     */
    @Override
    public Builder deconstruct() {
        Builder builder = new Builder();
        if (range.isRanged()) {
            builder = builder.with(range.getShortRange(), range.getLongRange());
        } else if (range.isReach()) {
            builder = builder.reach();
        } else {
            builder = builder.simpleMelee();
        }
        return builder.with(getBaseDamage())
                .weigh(getWeight())
                .with(getCost().getValue())
                .with(group)
                .with(getProperties());
    }

    public static class Factory extends BaseWeapon.Factory {
        public static Map<String, CustomWeapon> weapons = new HashMap<>();

        public static CustomWeapon create(String name) {
            if (weapons.containsKey(name)) {
                return weapons.get(name);
            } else {
                System.out.println("Weapon not found");
                return null;
            }
        }

        public static void add(String name, CustomWeapon weapon) {
            weapons.put(name, weapon);
        }

        public static String convertTXT(String filename) {
            Set<Weapon.Property> properties = new TreeSet<>();
            Builder builder = new Builder();
            String name = "";
            try (Scanner scanner = new Scanner(new File(filename))) {
                if (check(scanner, false)) {
                    name = scanner.nextLine();
                    builder.as(name);
                }
                check(scanner, false);
                String line = scanner.nextLine();
                if (line.equalsIgnoreCase(name)) {
                    check(scanner, false);
                    line = scanner.nextLine();
                }
                String[] parts = line.split(":\\s+");
                builder.with(Weapon.Group.valueOf(parts[1].split("\\s+")[0].toUpperCase()));
                builder.with(Integer.parseInt(parts[2].split("\\s+")[0]) * 100);
                builder.weigh(convertStrToFloat(parts[3].split("\\s+")[0]));
                do {
                    parts = scanner.nextLine().split("\\t");
                } while (parts.length == 1);
                if (check(scanner, false)) {
                    parts = scanner.nextLine().split("\\t");
                    builder.with(Damage.parse(parts[2]));
                    String[] propertyList = null;
                    if (parts.length == 5)
                        propertyList = parts[4].split(",\\s+");
                    if (propertyList != null) {
                        for (String property : propertyList) {
                            properties.add(parse(property, builder));
                        }
                    }
                    if (!properties.contains(Weapon.Property.RANGED) && !properties.contains(Weapon.Property.REACH)
                            && !properties.contains(Weapon.Property.THROWN)) {
                        builder.simpleMelee();
                    }
                    builder.with(properties);
                }

                String targetFile = "src/test/official/" + name.toLowerCase() + ".weapon";
                targetFile = targetFile.replace(" ", "_").replace(",", "");
                writeToFile(targetFile, builder);
                return targetFile;

            } catch (FileNotFoundException e) {
                System.out.println("File not found");
            } catch (ProtocolException e) {
                System.out.println(e.getMessage());
            }
            return "";
        }

        private static float convertStrToFloat(String str) {
            if (str.contains("/")) {
                String[] parts = str.split("/");
                return Float.parseFloat(parts[0]) / Float.parseFloat(parts[1]);
            } else if (str.equals("--")) {
                return 0;
            }else {
                return Float.parseFloat(str);
            }
        }

        private static Weapon.Property parse(String text, Builder builder) {
            String[] parts = text.split("\\s+");
            Weapon.Property property = null;
            if (parts.length == 1) {
                if (parts[0].equalsIgnoreCase("Two-Handed"))
                    return Weapon.Property.TWO_HANDED;
                else if (parts[0].equalsIgnoreCase("REACH"))
                    builder.reach();
                return Weapon.Property.valueOf(parts[0].toUpperCase());
            } else {
                if (parts[0].equalsIgnoreCase("THROWN")) {
                    builder.with(Range.parse(parts[2]));
                    property = Weapon.Property.THROWN;
                } else if (parts[0].equalsIgnoreCase("(Range")) {
                    builder.with(Range.parse(parts[1]));
                    property = Weapon.Property.RANGED;
                } else if (parts[0].equalsIgnoreCase("VERSATILE")) {
                    property = Weapon.Property.VERSATILE;
                }
            }
            return property;
        }

        private static boolean check(Scanner scanner, boolean optional) throws ProtocolException {
            if (scanner.hasNextLine()) {
                return true;
            } else if (!optional) {
                throw new ProtocolException("File is missing required information");
            }
            return false;
        }

        private static void writeToFile(String filename, Builder builder) {
            /*
            WeaponGroup Name (Enum, String)
            Damage DamageType (int, int, Enum)
            Property, Property, ... (Enum...)
            *Range/Reach (optional int, int)
            Weight Cost

            ex:
            0 Dagger
            1 4 1
            4 2 6
            20 60
            1 200
             */
            Weapon.Group group = builder.group;
            String name = builder.name;
            Damage damage = builder.damage;
            SortedSet<Weapon.Property> properties = builder.properties;
            Range range = builder.range;
            float weight = builder.weight;
            int cost = builder.costVal;

            try (PrintWriter pw = new PrintWriter(new File(filename))) {
                pw.print(group.ordinal() + " " + name + "\n");
                Set<Integer> sides = damage.getSides();
                if (sides.size() != 1) {
                    throw new ProtocolException("Damage must have a single die type");
                }
                pw.print(damage.dieCount() + " " + sides.iterator().next() + " " + damage.getType().ordinal() + "\n");
                StringBuilder sb = new StringBuilder();
                for (Weapon.Property property : properties) {
                    sb.append(property.ordinal()).append(" ");
                }
                pw.print(sb.toString().trim() + "\n");
                if (range.isRanged()) {
                    pw.print(range.getShortRange() * 5 + " " + range.getLongRange() * 5 + "\n");
                } else if (range.isReach()) {
                    pw.print("1\n");
                }
                pw.print(weight + " " + cost);
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
            } catch (ProtocolException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Builder class for CustomWeapon
     */
    public static class Builder extends BaseWeapon.Builder {
        @Override
        public CustomWeapon build() {
            return new CustomWeapon(this);
        }

        @Override
        public Builder with(int shortRange, int longRange) {
            super.with(shortRange, longRange);
            return this;
        }

        @Override
        public Builder reach() {
            super.reach();
            return this;
        }

        @Override
        public Builder simpleMelee() {
            super.simpleMelee();
            return this;
        }

        @Override
        public Builder with(Damage damage) {
            super.with(damage);
            return this;
        }

        @Override
        public Builder weigh(float weight) {
            super.weigh(weight);
            return this;
        }

        @Override
        public Builder as(String name) {
            super.as(name);
            return this;
        }

        @Override
        public Builder with(int cost) {
            super.with(cost);
            return this;
        }

        public Builder with(Weapon.Group group) {
            super.setGroup(group);
            return this;
        }

        public Builder with(Weapon.Property... properties) {
            if (properties.length > 0 && properties[0] != null) {
                setAbilities(Weapon.Property.abilities(new TreeSet<>(Arrays.asList(properties))));
                if (!Arrays.asList(properties).contains(Weapon.Property.RANGED))
                    addRoll(BaseWeapon.BASE_ATTACK);

                super.with(new TreeSet<>(Arrays.asList(properties)));
            } else {
                setAbilities(Set.of(Ability.Type.STR));
                addRoll(BaseWeapon.BASE_ATTACK);
                super.with(new HashSet<>());
            }

            return this;
        }

        @Override
        public Builder with(Set<Weapon.Property> properties) {
            super.with(properties);
            return this;
        }

        /**
         * Reads a weapon from a file
         * @param filename Name of the file
         * @return CustomWeapon object
         * @throws ProtocolException If the file is not of type *.weapon
         */
        public static CustomWeapon readFromFile(String filename) throws ProtocolException {
            CustomWeapon.Builder builder = new Builder();
            if (!filename.substring(filename.indexOf(".")).equals(".weapon"))
                throw new ProtocolException("File must be of type *.weapon.");

            try (Scanner sc = new Scanner(new File(filename))) {
                String[] line = sc.nextLine().split("\\s+");
                builder.with(Weapon.Group.values()[Integer.parseInt(line[0])]);
                builder.as(line[1]);

                line = sc.nextLine().split("\\s+");
                builder.with((new Damage.Builder()).with(line[0] + "d" + line[1])
                        .with(Damage.Type.values()[Integer.parseInt(line[2])]).build());

                line = sc.nextLine().split("\\s+");
                Weapon.Property[] properties = new Weapon.Property[line.length];
                int index = 0;
                if (!line[0].isEmpty()) {
                    for (String str : line) {
                        properties[index] = Weapon.Property.values()[Integer.parseInt(str)];
                        index++;
                    }
                }

                if (BaseWeapon.Builder.isNotMeleeRange(properties)) {
                    line = sc.nextLine().split("\\s+");
                    if (line.length == 1)
                        builder.reach();
                    else
                        builder.with(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
                } else {
                    builder.simpleMelee();
                }
                builder.with(properties);

                line = sc.nextLine().split("\\s+");
                builder.weigh(Float.parseFloat(line[0]));
                builder.with(Integer.parseInt(line[1]));
            } catch (IOException ioe) {
                System.out.print(ioe.getMessage());
            }
            return builder.build();
        }
    }
}
