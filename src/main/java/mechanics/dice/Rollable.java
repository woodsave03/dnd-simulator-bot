package mechanics.dice;

public interface Rollable {
    int roll();
    int rollAfter();
    void setConstant(int constant);
}
