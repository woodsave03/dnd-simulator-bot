package mechanics.actions;

import game.entities.Creature;
import mechanics.Construct;
import game.entities.Ability;

import java.util.HashMap;

/**
 * A Save is a Roll that is made by a Creature to resist an effect.
 * This Roll is only associated with one Ability, so its only extra field is a Descriptor.
 * @see Descriptor
 */
public class Save extends Roll implements Construct {
    private final Descriptor descriptor;

    /**
     * Constructor for Save. This constructor is private and should only be called by the Builder.
     * @param builder the Builder to construct this Save
     */
    private Save(Builder builder) {
        super(builder);
        this.descriptor = builder.descriptor;
    }

    /**
     * Deconstructs this Save into a Builder.
     * @return a Builder with the same fields as this Save
     */
    @Override
    public Builder deconstruct() {
        return ((Builder) super.deconstruct()).with(descriptor);
    }

    /**
     * Returns the Descriptor of this Save.
     * @return the Descriptor of this Save
     */
    public Descriptor saveType() {
        return descriptor;
    }

    /**
     * Returns the Ability associated with this Save.
     * @return the Ability associated with this Save
     */
    public Ability.Type ability() {
        return super.getAbilityOptions().iterator().next();
    }

    /**
     * Resolves the bonus of this Save given a Creature.
     * @param creature The relevant creature object
     * @return the bonus of this Save
     */
    @Override
    protected int resolveBonus(Creature creature) {
        return creature.abilities().provide(ability()).modifier() + creature.getProficiency(this);
    }

    /**
     * Returns a readable string representation of this Save.
     * @return a readable string representation of this Save
     */
    @Override
    public String display() {
        return "Save" + subDisplay();
    }

    @Override
    public String subDisplay() {
        return ": " + ability() + " (" + descriptor + ")";
    }

    /**
     * Returns a JSON string representation of this Save.
     * @return a JSON string representation of this Save
     */
    @Override
    public String toString() {
        return "Save{" + substring() + "}";
    }

    /**
     * Helper method for toString() to avoid code duplication.
     * @return a substring of the JSON string representation of this Save
     */
    @Override
    public String substring() {
        return super.substring() + ", descriptor=" + descriptor;
    }

    /**
     * A Flyweight Factory for creating Saves.
     */
    public static class Factory {
        private static HashMap<String, Save> saves = new HashMap<>();
        public static Save create(Ability.Type type, Descriptor saveDescriptor) {
            String key = type.name() + "_" + saveDescriptor.name();
            Save save = saves.get(key);
            if (save == null) {
                save = new Save.Builder().with(type).with(saveDescriptor).build();
                saves.put(key, save);
            }
            return save;
        }
    }

    /**
     * A Builder for constructing Saves.
     */
    private static class Builder extends Roll.Builder {
        private Descriptor descriptor;
        @Override
        public Save build() {
            super.with(Type.SAVE);
            return new Save(this);
        }

        public Builder with(Ability.Type type) {
            super.with(type);
            return this;
        }

        public Builder with(Descriptor descriptor) {
            this.descriptor = descriptor;
            return this;
        }
    }

    /**
     * A Descriptor dictates the type of Save for purposes for class features that may
     * allow for resistance against certain types of Saves.
     */
    public enum Descriptor {
        MAGICAL, NON_MAGICAL
    }
}
