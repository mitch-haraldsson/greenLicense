package de.shadowsoft.greenLicense.common.crypto;

import java.util.concurrent.ThreadLocalRandom;

public class PasswordGenerator {
    private final GeneratorConfig config;
    private String availableChars;

    public PasswordGenerator() {
        this(new GeneratorConfig());
    }

    public PasswordGenerator(GeneratorConfig config) {
        this.config = config;
        initChars();
    }

    public String generate() {
        StringBuilder builder = new StringBuilder();
        long length = ThreadLocalRandom.current().nextLong(config.getMin(), config.getMax() + 1);
        char[] charArray = availableChars.toCharArray();
        int charArrayMaxPos = charArray.length;

        for (long i = 0; i < length; i++) {
            int randomPos = ThreadLocalRandom.current().nextInt(0, charArrayMaxPos);
            builder.append(String.valueOf(charArray[randomPos]));
        }
        return builder.toString();
    }

    private void initChars() {
        availableChars = "";
        addChars();
        removeChars();
    }

    private void removeChars() {
        for (char search : config.getExclude().toCharArray()) {
            String replaceMe = String.valueOf(search);
            availableChars = availableChars.replace(replaceMe, "");
        }
    }

    private void addChars() {
        addLowerAZ();
        addUpperAZ();
        addNumbers();
        addSpecial();
        addBrackets();
        availableChars += config.getInclude();
    }

    private void addBrackets() {
        if (config.isIncludeBrackets()) {
            availableChars += "(){}[]<>";
        }
    }

    private void addSpecial() {
        if (config.isIncludeSpecial()) {
            availableChars += "!@#$%^&*_-=+;:'\"?,.~";
        }
    }

    private void addNumbers() {
        if (config.isIncludeNumbers()) {
            availableChars += "1234567890";
        }
    }

    private void addUpperAZ() {
        if (config.isIncludeAzUpper()) {
            availableChars += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        }
    }

    private void addLowerAZ() {
        if (config.isIncludeAzLower()) {
            availableChars += "abcdefghijklmnopqrstuvwxyz";
        }
    }

}
