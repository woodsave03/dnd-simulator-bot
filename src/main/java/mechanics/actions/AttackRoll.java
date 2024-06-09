package mechanics.actions;

import communication.Resolvable;
import game.entities.Ability;
import game.entities.Creature;
import mechanics.dice.Damage;

public abstract class AttackRoll extends Roll {
    private Damage damage;
    private String title;

    /**
     * Constructor for a new Roll object with the given ability options and type.
     *
     * @param builder The builder object used to create the Roll object
     */
    public AttackRoll(Builder builder) {
        super(builder);
        this.damage = builder.damage;
        this.title = builder.title;
    }

    /**
     * Uses the communication system to resolve the ideal Ability.Type for the Attack.
     *
     * @param creature the Creature to resolve the Ability.Type for
     * @return the resolved Ability.Type
     */
    public Ability.Type resolveType(Creature creature) {
        Resolvable<Ability.Type> resolvable = new Resolvable<>(getAbilityOptions());
        return creature.abilities().resolve(resolvable.options()).type();
    }

    /**
     * Returns the damage object of the attack.
     *
     * @return the damage object of the attack
     */
    public Damage getDamage() {
        return damage;
    }

    /**
     * Sets the damage object of the attack.
     *
     * @param damage the damage object to set
     */
    public void setDamage(Damage damage) {
        this.damage = damage;
    }

    /**
     * Returns the title of the attack.
     *
     * @return the title of the attack
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the attack.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    protected String substring() {
        return super.substring() + ", damage=" + getDamage() + ", title='" + title;
    }

    /**
     * Compares this Attack to another Object.
     *
     * @param o the Object to compare to
     * @return true if the Object is an Attack and has the same fields as this Attack
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof AttackRoll attack) {
            return super.equals(attack) && attack.damage.equals(damage)
                    && attack.title.equals(title);
        }
        return false;
    }

    @Override
    public Builder deconstruct() {
        return ((Builder) super.deconstruct())
                .with(getDamage())
                .as(title);
    }

    public static abstract class Builder extends Roll.Builder {
        private static final String DEFAULT_TITLE = "Attack";

        private Damage damage;
        private String title = DEFAULT_TITLE;

        @Override
        public Builder with(Ability.Type type) {
            super.with(type);
            return this;
        }

        /**
         * Sets the damage of the attack.
         *
         * @param damage the damage to set
         * @return the Builder object
         */
        public Builder with(Damage damage) {
            this.damage = damage;
            return this;
        }


        /**
         * Sets the title of the attack.
         *
         * @param title the title to set
         * @return the Builder object
         */
        public Builder as(String title) {
            this.title = title;
            return this;
        }
    }
}
