package mechanics.actions;

import mechanics.Construct;
import game.entities.Ability;

import java.util.HashMap;

public class Save extends Roll implements Construct {
    private final Descriptor descriptor;
    public Save(Builder builder) {
        super(builder, Type.SAVE);
        this.descriptor = builder.descriptor;
    }
    @Override
    public Builder builder() {
        return new Builder();
    }
    public Descriptor saveType() {
        return descriptor;
    }
    public Ability.Type ability() {
        return super.getAbilityOptions().iterator().next();
    }

    @Override
    public String display() {
        return "Save: " + ability() + " (" + descriptor + ")";
    }
    @Override
    public String toString() {
        return "Save{" + substring() + "}";
    }
    @Override
    public String substring() {
        return super.substring() + ", descriptor=" + descriptor;
    }

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
    private static class Builder extends Roll.Builder {
        private Descriptor descriptor;
        @Override
        public Save build() {
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
    public enum Descriptor {
        MAGICAL, NON_MAGICAL
    }
}
