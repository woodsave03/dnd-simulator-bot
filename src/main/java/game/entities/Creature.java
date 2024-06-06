package game.entities;

import game.items.Weapon;
import mechanics.Proficiency;
import mechanics.RollMode;
import mechanics.actions.*;
import communication.Source;

import java.util.HashMap;
import java.util.Iterator;

/**
 * A creature is an entity that can be interacted with in the game. It has abilities, skills, and
 * proficiencies that can be used to determine the outcome of actions.
 */
public class Creature extends Entity implements Source<Skill> {
    private String name;
    private AbilityContour abilities;
    private int proficiencyBonus = 2;
    private final HashMap<Skill, Proficiency> skills = new HashMap<>();
    private final HashMap<Ability.Type, Proficiency> saves = new HashMap<>();
    private final HashMap<Weapon.Group, Proficiency> weapons = new HashMap<>();
    private int ac;

    /**
     * Constructs a creature with the given abilities, armor class, and name.
     * @param abilities the abilities of the creature
     * @param ac the armor class of the creature
     * @param name the name of the creature
     * TODO: There will be MUCH more to be held within a creature in the future, requiring a Builder class
     */
    public Creature(AbilityContour abilities, int ac, String name) {
        this.abilities = abilities;
        this.ac = ac;
        this.name = name;
    }

    // Getters
    public String getName() {
        return name;
    }

    public AbilityContour abilities() {
        return abilities;
    }

    public int getArmorClass() {
        return ac;
    }

    public int getProficiencyBonus() {
        return proficiencyBonus;
    }

    public int getDC(Ability.Type type) {
        return 8 + abilities.provide(type).modifier() + proficiencyBonus;
    }

    /**
     * Makes a roll with the given roll and mode, returning the result.
     * @param roll the roll to make
     * @param mode the mode to make the roll in
     * @return the result of the roll
     */
    public int make(Roll roll, RollMode mode) {
        return roll.rollWith(mode) + getBonus(roll);
    }

    /**
     * Makes a check with the given skill and mode, returning the result.
     * @param skill the skill to check
     * @param mode the mode to make the check in
     * @return the result of the check
     */
    public int check(Skill skill, RollMode mode) {
        return make(new Check.Builder().with(skill).build(), mode);
    }

    /**
     * Adds a skill with the given proficiency to the creature.
     * @param skill the skill to add
     * @param proficiency the proficiency to add the skill with
     * @return the creature with the added skill
     */
    public Creature addSkill(Skill skill, Proficiency proficiency) {
        skills.put(skill, proficiency);
        return this;
    }

    /**
     * Adds a save with the given proficiency to the creature.
     * @param type the type of the save to add
     * @param proficiency the proficiency to add the save with
     * @return the creature with the added save
     */
    public Creature addSave(Ability.Type type, Proficiency proficiency) {
        saves.put(type, proficiency);
        return this;
    }

    /**
     * Adds a weapon group with the given proficiency to the creature.
     * @param group the group of the weapon to add
     * @param proficiency the proficiency to add the weapon with
     * @return the creature with the added weapon
     */
    public Creature addWeaponGroup(Weapon.Group group, Proficiency proficiency) {
        weapons.put(group, proficiency);
        return this;
    }

    /**
     * Calculates the bonus for the given roll.
     * @param roll the roll to calculate the bonus for
     * @return the bonus for the roll
     */
    public int getBonus(Roll roll) {
        if (roll instanceof Contest)
            throw new IllegalArgumentException("Contest rolls need clarification for bonus.");
        return abilities().resolve(roll.getAbilityOptions().iterator()).modifier() +
                getProficiency(roll);
    }

    /**
     * Calculates the proficiency bonus for the given skill.
     * @param skill the skill to calculate the proficiency bonus for
     * @return the proficiency bonus for the skill
     */
    public int getProficiency(Skill skill) {
        Proficiency proficiency = skills.get(skill);
        if (proficiency == null) {
            proficiency = Proficiency.NONE;
        }
        return proficiency.bonus(proficiencyBonus);
    }

    /**
     * Calculates the proficiency bonus for the given roll.
     * @param roll the roll to calculate the proficiency bonus for
     * @return the proficiency bonus for the roll
     */
    public int getProficiency(Roll roll) {
        Proficiency proficiency = switch (roll.getRollType()) {
            case CHECK -> skills.get(((Check) roll).getSkill());
            case SAVE -> saves.get(((Save) roll).ability());
            case ATTACK -> weapons.get(((Attack) roll).getGroup());
            default -> throw new UnsupportedOperationException("Proficiency of roll type "
                    + roll.getRollType() + " not supported");
        };
        if (proficiency == null) {
            proficiency = Proficiency.NONE;
        }
        return proficiency.bonus(proficiencyBonus);
    }

    /**
     * Determines if the creature is proficient in the given roll.
     * @param roll the roll to check proficiency for
     * @return true if the creature is proficient in the roll, false otherwise
     */
    public boolean isProficient(Roll roll) {
        if (roll instanceof Check check) {
            return skills.containsKey(check.getSkill());
        } else if (roll instanceof Save save) {
            return saves.containsKey(save.getAbilityOptions().iterator().next());
        } else if (roll instanceof Attack attack){
            return weapons.containsKey(attack.getGroup());
        } else {
            throw new IllegalArgumentException("Roll type: " + roll.getClass().getName() + " not supported");
        }
    }

    /**
     * Uses Creature as a Source to resolve a decision between multiple skills
     * @param options The options to choose from.
     * @return The resolved decision (best skill for the creature)
     */
    @Override
    public ConcreteCheck resolve(Iterator<Skill> options) {
        ConcreteCheck result = (new ConcreteCheck.Builder().with(options.next()).with(this)).build();
        while (options.hasNext()) {
            ConcreteCheck nextCheck = (new ConcreteCheck.Builder().with(options.next()).with(this))
                    .build();
            if (nextCheck.over(result)) {
                result = nextCheck;
            }
        }
        return result;
    }

    /**
     * Provides a concrete check for the given skill type.
     * @param type The type of attribute to provide.
     * @return The concrete check for the given skill type.
     */
    @Override
    public ConcreteCheck provide(Skill type) {
        return (new ConcreteCheck.Builder().with(type).with(this)).build();
    }

    /**
     * Returns the type of the best skill for the creature.
     * @param options The options to choose from.
     * @return The type of the best skill for the creature.
     */
    @Override
    public Skill type(Iterator<Skill> options) {
        return resolve(options).type();
    }

    /**
     * Enumerates the types of creatures that can be created.
     */
    public enum Type {
        CONSTRUCT, HUMANOID, FEY, ABERRATION, FIEND, UNDEAD, CELESTIAL,
        DRAGON, PLANT, BEAST, ELEMENTAL, GIANT, MONSTROSITY, OOZE
    }
}
