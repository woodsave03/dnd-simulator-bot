package game.items;

import game.Coin;
import game.CoinComposite;
import mechanics.Construct;
import mechanics.Constructor;

public class Item implements Construct {
    private float weight;
    private String name;
    private int costVal;

    public Item(Builder builder) {
        this.weight = builder.weight;
        this.name = builder.name;
        this.costVal = builder.costVal;
    }

    public CoinComposite getCost() {
        return Coin.Factory.change(costVal);
    }

    public float getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Item{" + substring() + "}";
    }

    public String substring() {
        return "costVal=" + costVal + ", name=" + name + ", weight=" + weight;
    }

    @Override
    public Builder deconstruct() {
        // TODO
        return null;
    }

    public static class Builder implements Constructor {
        protected float weight;
        protected String name;
        protected int costVal;

        public Builder weigh(float weight) {
            this.weight = weight;
            return this;
        }

        public Builder as(String name) {
            this.name = name;
            return this;
        }

        public Builder with(int costVal) {
            this.costVal = costVal;
            return this;
        }

        @Override
        public Item build() {
            return new Item(this);
        }
    }
}
