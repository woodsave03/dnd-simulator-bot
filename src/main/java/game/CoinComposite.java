package game;

import java.util.LinkedList;
import java.util.List;

/**
 * A composite class that represents a collection of coins.
 */
public class CoinComposite {
    private final List<Coin> coins;
    private int total;

    /**
     * Default constructor.
     */
    public CoinComposite() {
        coins = new LinkedList<>();
    }

    /**
     * Constructor that initializes the composite with a list of coins.
     *
     * @param coins the list of coins
     */
    public CoinComposite(List<Coin> coins) {
        this.coins = coins;
        total = calculateTotal();
    }

    /**
     * Adds a coin to the composite.
     * @param coin the coin to add
     */
    public void add(Coin coin) {
        coins.add(coin);
        total += coin.getValue();
    }

    /**
     * Adds a list of coins to the composite.
     * @param coins the list of coins to add
     */
    public void addAll(List<Coin> coins) {
        this.coins.addAll(coins);
        total += calculateTotal();
    }

    /**
     * Removes a coin from the composite.
     * @param coin the coin to remove
     */
    public void remove(Coin coin) {
        coins.remove(coin);
        total -= coin.getValue();
    }

    /**
     * Removes a list of coins from the composite.
     * @param coins the list of coins to remove
     */
    public void removeAll(List<Coin> coins) {
        this.coins.removeAll(coins);
        total = calculateTotal();
    }

    /**
     * Calculates the total value of the coins in the composite.
     * @return the total value
     * TODO: WHY DID I MAKE IT LIKE THIS?? Could be made more efficient with the @see #total class variable
     */
    private int calculateTotal() {
        return coins.stream().mapToInt(Coin::getValue).sum();
    }

    /**
     * Returns the list of coins in the composite.
     * @return the list of coins
     */
    public int getValue() {
        return total;
    }

    /**
     * Converts the composite to JSON format.
     * @return the JSON representation of the composite
     */
    @Override
    public String toString() {
        return "CoinComposite{" +
                "coins=" + coins +
                '}';
    }

    public static CoinComposite of(List<Coin> coins) {
        return new CoinComposite(coins);
    }
}
