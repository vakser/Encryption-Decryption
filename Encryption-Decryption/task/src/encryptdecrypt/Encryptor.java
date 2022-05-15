package encryptdecrypt;

public class Encryptor {
    private final EncryptingStrategy strategy;

    public Encryptor(EncryptingStrategy strategy) {
        this.strategy = strategy;
    }

    public void process(String[] arguments) {
        this.strategy.processArguments(arguments);
    }
}
