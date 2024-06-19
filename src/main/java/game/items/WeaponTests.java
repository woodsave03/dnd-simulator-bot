package game.items;

import mechanics.dice.Damage;
import mechanics.dice.Die;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ProtocolException;

public class WeaponTests {
    @Test
    public void testWeaponString() {
        System.out.println(BaseWeapon.Factory.create("Net").display());
        System.out.println(BaseWeapon.Factory.create("Longsword").display());
        System.out.println(BaseWeapon.Factory.create("Dagger").display());
        System.out.println(BaseWeapon.Factory.create("Shortsword").display());
    }

    @Test
    public void testEquals() {
        BaseWeapon dagger = BaseWeapon.Factory.create("Dagger");
        BaseWeapon dagger2 = BaseWeapon.Factory.create("Dagger");
        Assertions.assertEquals(dagger, dagger2);

        BaseWeapon longsword = BaseWeapon.Factory.create("Longsword");
        Assertions.assertNotEquals(dagger, longsword);

        CustomWeapon.Builder builder = new CustomWeapon.Builder()
                .with(20, 60)
                .with(new Damage.Builder()
                        .with(Damage.Type.PIERCING)
                        .with(Die.Factory.d4()).build())
                .weigh(1)
                .with(200)
                .with(Weapon.Group.SIMPLE)
                .as("Dagger")
                .with(Weapon.Property.FINESSE, Weapon.Property.LIGHT, Weapon.Property.THROWN);
        CustomWeapon dagger3 = builder.build();
        Assertions.assertEquals(dagger, dagger3);
    }

    @Test
    public void testReadFromFile() {
        String badPath = "test/weaponTemp.txt";
        String filepath = "src/test/dagger.weapon";
        Assertions.assertThrows(ProtocolException.class, () -> CustomWeapon.Builder.readFromFile(badPath));

        Assertions.assertDoesNotThrow(() -> CustomWeapon.Builder.readFromFile(filepath));

        try {
            CustomWeapon weapon = CustomWeapon.Builder.readFromFile(filepath);
            System.out.print(weapon);
        } catch (ProtocolException e) {
            System.out.print(e.getMessage());

        }
    }

    @Test
    public void daggerEqualsFile() {
        String filepath = "src/test/dagger.weapon";
        CustomWeapon weapon = null;
        try {
            weapon = CustomWeapon.Builder.readFromFile(filepath);
        } catch (ProtocolException e) {
            System.out.print(e.getMessage());
        }

        Assertions.assertEquals(weapon, BaseWeapon.Factory.create("dagger"));
    }

    @Test
    public void testWeaponFactoryFunction() {
        Assertions.assertDoesNotThrow(() -> BaseWeapon.Factory.create("Dagger"));
    }

    @Test
    public void testTXTtoWeapon() {
        String filepath = "src/test/warhammerPaste.txt";
        CustomWeapon weapon = null;
        CustomWeapon longbow = null;
        try {
            weapon = CustomWeapon.Builder.readFromFile(CustomWeapon.Factory.convertTXT(filepath));
            longbow = CustomWeapon.Builder.readFromFile(CustomWeapon.Factory.convertTXT("src/test/longbowPaste.txt"));
        } catch (ProtocolException e) {
            System.out.print(e.getMessage());
        }
        Assertions.assertEquals(weapon, BaseWeapon.Factory.create("Warhammer"));
        Assertions.assertEquals(longbow, BaseWeapon.Factory.create("Longbow"));
    }
}
