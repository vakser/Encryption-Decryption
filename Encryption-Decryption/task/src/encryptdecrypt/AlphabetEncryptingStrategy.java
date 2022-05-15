package encryptdecrypt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class AlphabetEncryptingStrategy implements EncryptingStrategy {
    StringBuilder output = new StringBuilder();
    String alphabet = "abcdefghijklmnopqrstuvwxyz";
    String alphabetCaps = alphabet.toUpperCase();
    String mode;
    String keyStr;
    String data;
    String in;
    String out;
    boolean modePresent = false;
    boolean keyPresent = false;
    boolean dataPresent = false;
    boolean inPresent = false;
    boolean outPresent = false;

    @Override
    public void processArguments(String[] arguments) {
        for (int i = 0; i < arguments.length; i += 2) {
            String argument = arguments[i];
            switch (argument) {
                case "-mode" -> {
                    mode = arguments[i + 1];
                    modePresent = true;
                }
                case "-key" -> {
                    keyStr = arguments[i + 1];
                    keyPresent = true;
                }
                case "-data" -> {
                    data = arguments[i + 1];
                    dataPresent = true;
                }
                case "-in" -> {
                    in = arguments[i + 1];
                    inPresent = true;
                }
                case "-out" -> {
                    out = arguments[i + 1];
                    outPresent = true;
                }
                default -> {
                }
            }
        }
        setDefaultArgsIfNotPresent();
        cutQuotesForDataStringWithSpaces();
        processData();
    }

    public void setDefaultArgsIfNotPresent() {
        if (!modePresent) {
            mode = "enc";
        }
        if (!keyPresent) {
            keyStr = "0";
        }
        if (!dataPresent && !inPresent) {
            data = "";
        }
    }

    public void cutQuotesForDataStringWithSpaces() {
        if (data != null && data.length() > 0 && data.charAt(0) == '"' && data.charAt(data.length() - 1) == '"') {
            data = data.substring(1, data.length() - 1);
        }
    }

    public void processData() {
        int key = Integer.parseInt(keyStr);
        if (dataPresent) {
            assert data != null;
            if ("dec".equals(mode)) {
                decrypt(data, key);
            } else {
                encrypt(data, key);
            }
        }
        if (!dataPresent && inPresent) {
            File file = new File(in);
            StringBuilder input = new StringBuilder();
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNext()) {
                    input.append(scanner.nextLine());
                }
            } catch (FileNotFoundException e) {
                System.out.println("Error: input file not found");
            }
            if ("dec".equals(mode)) {
                decrypt(input.toString(), key);
            } else {
                encrypt(input.toString(), key);
            }
        }
    }

    public void encrypt(String data, int key) {
        for (int i = 0; i < data.length(); i++) {
            if (Character.isUpperCase(data.charAt(i)) || !Character.isLetter(data.charAt(i))) {
                output.append(data.charAt(i));
            }
            for (int j = 0; j < alphabet.length(); j++) {
                if (alphabet.charAt(j) != data.charAt(i)) {
                    continue;
                }
                output.append(alphabet.charAt((j + key) % alphabet.length()));
            }

        }
        data = output.toString();
        output = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            if (!Character.isUpperCase(data.charAt(i)) || !Character.isLetter(data.charAt(i))) {
                output.append(data.charAt(i));
            }
            for (int j = 0; j < alphabetCaps.length(); j++) {
                if (alphabetCaps.charAt(j) != data.charAt(i)) {
                    continue;
                }
                output.append(alphabetCaps.charAt((j + key) % alphabetCaps.length()));
            }
        }
        chooseOutput();
    }

    public void decrypt(String data, int key) {
        for (int i = 0; i < data.length(); i++) {
            if (Character.isUpperCase(data.charAt(i)) || !Character.isLetter(data.charAt(i))) {
                output.append(data.charAt(i));
            }
            for (int j = 0; j < alphabet.length(); j++) {
                if (alphabet.charAt(j) != data.charAt(i)) {
                    continue;
                }
                if (j - key >= 0) {
                    output.append(alphabet.charAt((j - key) % alphabet.length()));
                } else {
                    output.append(alphabet.charAt((alphabet.length() + j - key) % alphabet.length()));
                }
            }
        }
        data = output.toString();
        output = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            if (!Character.isUpperCase(data.charAt(i)) || !Character.isLetter(data.charAt(i))) {
                output.append(data.charAt(i));
            }
            for (int j = 0; j < alphabetCaps.length(); j++) {
                if (alphabetCaps.charAt(j) != data.charAt(i)) {
                    continue;
                }
                if (j - key >= 0) {
                    output.append(alphabetCaps.charAt((j - key) % alphabetCaps.length()));
                } else {
                    output.append(alphabetCaps.charAt((alphabetCaps.length() + j - key) % alphabetCaps.length()));
                }
            }
        }
        chooseOutput();
    }

    public void chooseOutput() {
        if (!outPresent) {
            System.out.println(output);
        } else {
            File file = new File(out);
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(output.toString());
            } catch (IOException e) {
                System.out.println("Error: file cannot be written");
            }
        }
    }
}
