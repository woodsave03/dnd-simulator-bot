package game.play;

import java.util.Collection;

/**
 * Class representing the features of a character class in D&D 5e.
 */
public abstract class ClassFeatureComposite {
    private String description;
    private final int level;
    private Collection<ClassFeatureComposite> features;

    public ClassFeatureComposite(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }

    abstract String display();

    public void add(ClassFeatureComposite feature) {
        features.add(feature);
    }

    public void add(ClassFeatureComposite... features) {
        for (ClassFeatureComposite feature : features) {
            add(feature);
        }
    }

    public void add(Collection<ClassFeatureComposite> features) {
        this.features.addAll(features);
    }

    public void remove(ClassFeatureComposite feature) {
        features.remove(feature);
    }

    public void remove(ClassFeatureComposite... features) {
        for (ClassFeatureComposite feature : features) {
            remove(feature);
        }
    }

    public void remove(Collection<ClassFeatureComposite> features) {
        this.features.removeAll(features);
    }
}
