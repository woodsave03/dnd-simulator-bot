package mechanics.actions;

import communication.Resolvable;
import game.entities.Ability;
import game.entities.Creature;
import game.items.Weapon;
import mechanics.*;
import mechanics.dice.Damage;

public abstract class Attack extends Roll {
    private Damage damage;
    private String title;
    private Weapon.Group group;
    public Attack(Builder builder) {
        super(builder, Type.ATTACK);
        this.damage = builder.damage;
        this.title = builder.title;
        this.group = builder.group;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Ability.Type resolveType(Creature creature) {
        Resolvable<Ability.Type> resolvable = new Resolvable<>(getAbilityOptions());
        return creature.resolve(resolvable.options()).type();
    }
    public Weapon.Group getGroup() {
        return group;
    }
    public void setGroup(Weapon.Group group) {
        this.group = group;
    }
    @Override
    public boolean equals(Object o) {
        if (o instanceof Attack attack) {
            return super.equals(attack) && attack.damage.equals(damage)
                    && attack.title.equals(title) && attack.group.equals(group);
        }
        return false;
    }
    @Override
    public String toString() {
        return "Attack{" +
                substring() +
                '}';
    }

    @Override
    protected String substring() {
        return super.substring() + ", damage=" + damage + ", title='" + title + "', group=" + group;
    }

    public Damage getDamage() {
        return damage;
    }
    public void setDamage(Damage damage) {
        this.damage = damage;
    }
    @Override
    public String display() {
        return title + " (" + damage.display() + ")";
    }
    abstract boolean inRange(int distance);
    abstract RollMode rollMode(int distance);
    public abstract static class Builder extends Roll.Builder {
        private static final String DEFAULT_TITLE = "Attack";
        private String title = DEFAULT_TITLE;
        protected Damage damage;
        private Weapon.Group group;

        public Builder as(String title) {
            this.title = title;
            return this;
        }

        public Builder with(Damage damage) {
            this.damage = damage;
            return this;
        }

        public Builder with(Weapon.Group group) {
            this.group = group;
            return this;
        }
    }
}
