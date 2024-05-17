package game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Coin {
    public final static int PLATINUM_VALUE = 1000;
    public final static int GOLD_VALUE = 100;
    public final static int ELECTRUM_VALUE = 50;
    public final static int SILVER_VALUE = 10;
    public final static int COPPER_VALUE = 1;
    public static boolean electrum = true;
    int value;
    private Coin(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Coin{" +
                "value=" + value +
                '}';
    }
    public static class Factory {
        private static HashMap<Type, Coin> flyweight = new HashMap<>();
        public static List<Coin> change(int total) {
            List<Coin> result = new LinkedList<>();
            int current = total;
            while (current != 0) {
                Type target;
                if (current >= PLATINUM_VALUE)
                    target = Type.PLATINUM;
                else if (current >= GOLD_VALUE)
                    target = Type.GOLD;
                else if (current >= ELECTRUM_VALUE && electrum)
                    target = Type.ELECTRUM;
                else if (current >= SILVER_VALUE)
                    target = Type.SILVER;
                else
                    target = Type.COPPER;
                result.add(create(target));
                current -= target.value;
            }
            return result;
        }
        public static Coin create(Type type) {
            Coin coin = flyweight.get(type);
            if (coin == null) {
                coin = new Coin(type.value);
                flyweight.put(type, coin);
            }
            return coin;
        }
    }

    public enum Type {
        PLATINUM(PLATINUM_VALUE), GOLD(GOLD_VALUE), ELECTRUM(ELECTRUM_VALUE),
        SILVER(SILVER_VALUE), COPPER(COPPER_VALUE);

        private int value;

        Type(int value) {
            this.value = value;
        }

        public static int convert(int num, Type type1, Type type2) {
            return (num * type1.value) / type2.value;
        }

        public static int remainder(int num, Type type1, Type type2) {
            return (num * type1.value) % type2.value;
        }
    }

    public static void useElectrum() {
        electrum = true;
    }

    public static void beHeathen() {
        electrum = false;
    }
}
