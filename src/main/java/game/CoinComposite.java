package game;

import java.util.LinkedList;
import java.util.List;

public class CoinComposite {
    private final List<Coin> coins;
    private int total;

    public CoinComposite() {
        coins = new LinkedList<>();
    }

    public CoinComposite(List<Coin> coins) {
        this.coins = coins;
        total = calculateTotal();
    }

    public void add(Coin coin) {
        coins.add(coin);
        total += coin.getValue();
    }

    public void addAll(List<Coin> coins) {
        this.coins.addAll(coins);
        total += calculateTotal();
    }

    public void remove(Coin coin) {
        coins.remove(coin);
        total -= coin.getValue();
    }

    public void removeAll(List<Coin> coins) {
        this.coins.removeAll(coins);
        total = calculateTotal();
    }

    private int calculateTotal() {
        return coins.stream().mapToInt(Coin::getValue).sum();
    }

    public int getValue() {
        return total;
    }

    @Override
    public String toString() {
        return "CoinComposite{" +
                "coins=" + coins +
                '}';
    }
}
