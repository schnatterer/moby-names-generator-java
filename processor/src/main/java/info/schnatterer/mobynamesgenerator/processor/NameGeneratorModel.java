package info.schnatterer.mobynamesgenerator.processor;

public class NameGeneratorModel {
    private final String packageName;
    private final String left;
    private final String right;

    public NameGeneratorModel(String packageName, String left, String right) {
        this.packageName = packageName;
        this.left = left;
        this.right = right;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getLeft() {
        return left;
    }

    public String getRight() {
        return right;
    }
}
