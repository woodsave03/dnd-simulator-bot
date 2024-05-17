package game.entities;

import game.items.Weapon;
import mechanics.Proficiency;
import mechanics.RollMode;
import mechanics.actions.*;
import communication.Source;

import java.util.HashMap;
import java.util.Iterator;

public class Creature extends Entity implements Source<Ability.Type> {
    private String name;
    private AbilityContour abilities;
    private int proficiencyBonus = 2;
    private final HashMap<Skill, Proficiency> skills = new HashMap<>();
    private final HashMap<Ability.Type, Proficiency> saves = new HashMap<>();
    private final HashMap<Weapon.Group, Proficiency> weapons = new HashMap<>();
    private int ac;

    public Creature(AbilityContour abilities, int ac, String name) {
        this.abilities = abilities;
        this.ac = ac;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public AbilityContour abilities() {
        return abilities;
    }

    @Override
    public Ability resolve(Iterator<Ability.Type> options) {
        Ability best = provide(options.next());
        while (options.hasNext()) {
            Ability next = provide(options.next());
            if (next.over(best)) {
                best = next;
            }
        }
        return best;
    }

    @Override
    public Ability.Type type(Iterator<Ability.Type> options) {
        return resolve(options).type();
    }

    @Override
    public Ability provide(Ability.Type type) {
        return abilities.get(type);
    }

    public int getArmorClass() {
        return ac;
    }

    public int getProficiencyBonus() {
        return proficiencyBonus;
    }

    public int getDC(Ability.Type type) {
        return 8 + provide(type).modifier() + proficiencyBonus;
    }

    public int make(Roll roll, RollMode mode) {
        return roll.rollWith(mode) + getBonus(roll);
    }

    public int check(Skill skill, RollMode mode) {
        return make(new Check.Builder().with(skill).build(), mode);
    }

    public Skill resolveSkills(Iterator<Skill> skills) {
        Skill best = skills.next();
        while (skills.hasNext()) {
            Skill next = skills.next();
            int bestBonus = provide(best.ability()).modifier()
                    + Proficiency.bonus(this.skills.get(best), proficiencyBonus);
            int nextBonus = provide(next.ability()).modifier()
                    + Proficiency.bonus(this.skills.get(next), proficiencyBonus);
            if (nextBonus > bestBonus) {
                best = next;
            }
        }
        return best;
    }

    public Creature addSkill(Skill skill, Proficiency proficiency) {
        skills.put(skill, proficiency);
        return this;
    }

    public Creature addSave(Ability.Type type, Proficiency proficiency) {
        saves.put(type, proficiency);
        return this;
    }

    public Creature addWeaponGroup(Weapon.Group group, Proficiency proficiency) {
        weapons.put(group, proficiency);
        return this;
    }

    public int getBonus(Roll roll) {
        if (roll instanceof Contest)
            throw new IllegalArgumentException("Contest rolls need clarification for bonus.");
        return resolve(roll.getAbilityOptions().iterator()).modifier() +
                getProficiency(roll);
    }

    public int getProficiency(Skill skill) {
        Proficiency proficiency = skills.get(skill);
        if (proficiency == null) {
            proficiency = Proficiency.NONE;
        }
        return proficiency.bonus(proficiencyBonus);
    }
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

    public enum Type {
        CONSTRUCT, HUMANOID, FEY, ABERRATION, FIEND, UNDEAD, CELESTIAL,
        DRAGON, PLANT, BEAST, ELEMENTAL, GIANT, MONSTROSITY, OOZE
    }
}
