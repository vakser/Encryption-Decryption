package encryptdecrypt;

public class Main {

    public static void main(String[] args) {
        String[] arguments = new String[args.length];
        System.arraycopy(args, 0, arguments, 0, arguments.length);
        Encryptor encryptor;
        String alg = "";
        boolean algPresent = false;
        for (int i = 0; i < arguments.length; i++) {
            if ("-alg".equals(arguments[i])) {
                alg = arguments[i + 1];
                algPresent = true;
            }
        }
        if (!algPresent) {
            alg = "shift";
        }
        if ("unicode".equals(alg)) {
            encryptor = new Encryptor(new UnicodeEncryptingStrategy());
        } else {
            encryptor = new Encryptor(new AlphabetEncryptingStrategy());
        }
        encryptor.process(arguments);
    }
}
