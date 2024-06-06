package mechanics.dice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DiceTests {

    @Test
    public void testDieString() {
        Die d6 = Die.Factory.d6();
        String expected = "1d6";
        String actual = d6.display();
        Assertions.assertEquals(expected, actual);
    }
    @Test
    public void testSequenceString() {
        Sequence s = new Sequence.Builder()
                .with(Die.Factory.d20())
                .with(Die.Factory.d4())
                .with(Die.Factory.d4())
                .with(new Constant(2))
                .with(new Constant(1))
                .build();
        String expected = "1d20 + 2d4 + 3";
        String actual = s.display();
        Assertions.assertEquals(expected, actual);
    }
    @Test
    public void testParse() {
        String notation = "1d6 + 1d4 + 2";
        Sequence expected = new Sequence.Builder()
                .with(Die.Factory.d6())
                .with(Die.Factory.d4())
                .with(new Constant(2))
                .build();
        Sequence actual = (Sequence) Die.Factory.parse(notation);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testEqual() {
        Die d6 = Die.Factory.d6();
        Die d6_2 = Die.Factory.d6();
        Assertions.assertEquals(d6, d6_2);

        Constant c = new Constant(1);
        Constant c2 = new Constant(1);
        Assertions.assertEquals(c, c2);

        Sequence s = new Sequence.Builder()
                .with(d6)
                .with(c)
                .with(new Sequence.Builder()
                        .with(d6)
                        .with(c)
                        .build())
                .build();

        Sequence s2 = s.copy();
        Assertions.assertEquals(s, s2);
    }

    @Test
    public void testRoll() {
        DiceComposite sequence = Die.Factory.parse("1d6 + 1d4 + 2");
        //enter debug mode to see the rolls DONE
        System.out.println(sequence.roll());
    }

    @Test
    public void testFile() {
        try (Scanner scanner = new Scanner(new File("src/main/resources/dieTest.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Sequence sequence = (Sequence) Die.Factory.parse(line);
                System.out.print(sequence.display());
                System.out.println(" rolls a " + sequence.roll());
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testDamage() {
        Damage damage = new Damage.Builder()
                .with(Die.Factory.parse("2d6"))
                .with(Damage.Type.SLASHING)
                .build();
        System.out.println(damage.display());
        System.out.println(damage.roll());
        Assertions.assertEquals("2d6 of SLASHING damage", damage.display());
    }

    @Test
    public void testExplode() {
        Die d6 = Die.Factory.d6();
        Die d6Explode = d6.explode();
        Die d8 = Die.Factory.d8();
        Assertions.assertEquals(d6Explode, d8);

        System.out.println(d6Explode.display());
        System.out.println(d6Explode.roll());
    }
}
