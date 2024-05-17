package mechanics.actions;

import game.entities.Ability;
import game.entities.AbilityContour;
import game.entities.Creature;
import game.items.Weapon;
import mechanics.Proficiency;
import mechanics.dice.Damage;
import mechanics.dice.Die;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class RollTests {
    private static Check check1, check2;
    private static Save save;
    private static RangedAttack rangedAttack;
    private static MeleeAttack meleeAttack;
    private static Contest contest;
    private static Creature creature1, creature2;

    @BeforeAll
    public static void beforeAll() {
        check1 = Check.Factory
                .create(Skill.ATHLETICS);
        check2 = Check.Factory
                .create(Skill.ACROBATICS);
        save = Save.Factory.create(Ability.Type.CON, Save.Descriptor.NON_MAGICAL);
        rangedAttack = new RangedAttack.Builder()
                .with(Ability.Type.DEX)
                .with(20, 60)
                .with(Weapon.Group.SIMPLE)
                .with(new Damage.Builder()
                        .with(Die.Factory.d6())
                        .with(Damage.Type.PIERCING)
                        .build())
                .as("Example Ranged Attack")
                .build();
        meleeAttack = new MeleeAttack.Builder()
                .with(Ability.Type.STR)
                .with(2)
                .with(Weapon.Group.MARTIAL)
                .with(new Damage.Builder()
                        .with(Die.Factory.parse("2d6"))
                        .with(Damage.Type.SLASHING)
                        .build())
                .as("Example Melee Attack")
                .build();
        contest = new Contest.Builder()
                .with(Skill.ATHLETICS)
                .against(Set.of(Skill.ACROBATICS, Skill.ATHLETICS))
                .build();
        creature1 = new Creature(new AbilityContour(new int[]{15, 14, 13, 12, 10, 8}), 12, "Test Creature 1")
                .addWeaponGroup(Weapon.Group.SIMPLE, Proficiency.PROFICIENT);
        creature2 = new Creature(new AbilityContour(new int[]{8, 10, 12, 13, 14, 15}), 10, "Test Creature 2");
    }

    @Test
    public void testCheck() {
        int dc = 10;
        RollCommand command = new RollCommand(check1);
        Assertions.assertThrows(IllegalStateException.class, () -> command.sendTo(creature1));
        command.withDC(dc);
        Assertions.assertDoesNotThrow(() -> command.sendTo(creature1));
    }

    @Test
    public void displayRolls() {
        RollCommand command = new RollCommand(check1).withDC(10);
        Assertions.assertEquals("Check: ATHLETICS (Strength) (DC 10)", command.display());
        System.out.println(command.sendTo(creature2));

        command = new RollCommand(check2).withDC(10);
        Assertions.assertEquals("Check: ACROBATICS (Dexterity) (DC 10)", command.display());
        System.out.println(command.sendTo(creature2));

        command = new RollCommand(save).withAbility(Ability.Type.CON).attach(creature1);
        Assertions.assertEquals("Save: Constitution (NON_MAGICAL) (DC 11)", command.display());
        System.out.println(command.sendTo(creature2));

        command = new RollCommand(rangedAttack).attach(creature1);
        Assertions.assertEquals("Ranged Attack: Example Ranged Attack (1d6 of PIERCING damage) (20/60) (+4)",
                command.display());
        System.out.println(command.sendTo(creature2));

        command = new RollCommand(meleeAttack).attach(creature1);
        Assertions.assertEquals("Melee Attack: Example Melee Attack (2d6 of SLASHING damage) (+2)",
                command.display());
        System.out.println(command.sendTo(creature2));
    }

    @Test
    public void rollCommandEquals() {
        RollCommand command1 = new RollCommand(check1).withDC(10);
        RollCommand command2 = new RollCommand(check1).withDC(10);
        Assertions.assertEquals(command1, command2);
        command1.attach(creature1).sendTo(creature2);
        command2 = command1.copy();
        Assertions.assertEquals(command1, command2);
    }

    @Test
    public void testContest() {
        RollCommand command = new RollCommand(contest);
        Assertions.assertThrows(IllegalStateException.class, () -> command.sendTo(creature1));
        command.attach(creature1);
        Assertions.assertDoesNotThrow(() -> command.sendTo(creature2));
        System.out.println(command.sendTo(creature2));
        System.out.println(command.report());
    }

    @Test
    public void testEquals() {
        Check newCheck = Check.Factory.create(Skill.ATHLETICS);
        Assertions.assertEquals(check1, newCheck);

        Save newSave = Save.Factory.create(Ability.Type.CON, Save.Descriptor.NON_MAGICAL);
        Assertions.assertEquals(save, newSave);

        RangedAttack newRangedAttack = new RangedAttack.Builder()
                .with(Ability.Type.DEX)
                .with(20, 60)
                .with(Weapon.Group.SIMPLE)
                .with(new Damage.Builder()
                        .with(Die.Factory.d6())
                        .with(Damage.Type.PIERCING)
                        .build())
                .as("Example Ranged Attack")
                .build();
        Assertions.assertEquals(rangedAttack, newRangedAttack);

        MeleeAttack newMeleeAttack = new MeleeAttack.Builder()
                .with(Ability.Type.STR)
                .with(2)
                .with(Weapon.Group.MARTIAL)
                .with(new Damage.Builder()
                        .with(Die.Factory.parse("2d6"))
                        .with(Damage.Type.SLASHING)
                        .build())
                .as("Example Melee Attack")
                .build();
        Assertions.assertEquals(meleeAttack, newMeleeAttack);

        Contest newContest = new Contest.Builder()
                .with(Skill.ATHLETICS)
                .against(Set.of(Skill.ACROBATICS, Skill.ATHLETICS))
                .build();
        Assertions.assertEquals(contest, newContest);
    }
}
