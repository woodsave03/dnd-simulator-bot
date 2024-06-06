package game.items;

import mechanics.actions.Range;
import mechanics.dice.Damage;

import java.io.File;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.*;

public class CustomWeapon extends BaseWeapon {
    private final String name;
    private final Weapon.Group group;
    private CustomWeapon(Builder builder, Range range) {
        super(builder, range);
        this.name = builder.name;
        this.group = builder.group;
    }

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
        return builder.as(name).withWeight(getWeight()).withCost(getCost().getValue())
                .with(getBaseDamage()).with(group).with(getProperties());
    }

    public static class Builder extends BaseWeapon.Builder {
        @Override
        public CustomWeapon build() {
            return new CustomWeapon(this, calculateRange());
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
        public Builder withWeight(double weight) {
            super.withWeight(weight);
            return this;
        }

        @Override
        public Builder withCost(int cost) {
            super.withCost(cost);
            return this;
        }

        public Builder with(Weapon.Group group) {
            super.setGroup(group);
            return this;
        }

        public Builder as(String name) {
            super.setName(name);
            return this;
        }

        public Builder with(Weapon.Property... properties) {
            setAbilities(Weapon.Property.abilities(new TreeSet<>(Arrays.asList(properties))));
            if (!Arrays.asList(properties).contains(Weapon.Property.RANGED))
                addRoll(BaseWeapon.BASE_ATTACK);
            super.with(new TreeSet<>(Arrays.asList(properties)));
            return this;
        }

        @Override
        public Builder with(SortedSet<Weapon.Property> properties) {
            super.with(properties);
            return this;
        }

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
                for (String str : line) {
                    properties[index] = Weapon.Property.values()[Integer.parseInt(str)];
                    index++;
                }

                if (BaseWeapon.Builder.isNotMeleeRange(properties)) {
                    line = sc.nextLine().split("\\s+");
                    if (line.length == 1)
                        builder.reach();
                    else
                        builder.with(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
                }
                builder.with(properties);

                line = sc.nextLine().split("\\s+");
                builder.withWeight(Double.parseDouble(line[0]));
                builder.withCost(Integer.parseInt(line[1]));
            } catch (IOException ioe) {
                System.out.print(ioe.getMessage());
            }
            return builder.build();
        }
    }
}
