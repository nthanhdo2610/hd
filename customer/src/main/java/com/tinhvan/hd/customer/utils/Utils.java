package com.tinhvan.hd.customer.utils;

import java.util.Random;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final String str = "abcdefghijklmnopqrstuvwxyz";
    private static Random r = new Random();

    /**
     * Generate random password base on minimum and maximum length request
     *
     * @param minLength
     * @param maxLength
     * @return password created successfully
     */
    public String getRandomPassword(int minLength, int maxLength) {
        if (minLength <= 0 || maxLength <= 0 || maxLength < minLength)
            return null;

        Pattern pattern = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[~!@#$%^&*()_+<>?,./]).{" + minLength + "," + maxLength + "})");
        int length;
        while (true) {
            length = r.nextInt(maxLength + 1);
            if (length > 0 && length >= minLength) break;
        }
        int b = 0;
        do {
            String pw = generate(length);
            Matcher matcher = pattern.matcher(pw);
            if (matcher.matches())
                return pw;
        } while (b == 0);
        return null;
    }

    /**
     * Generate string password by length request
     *
     * @param length
     * @return string password
     */
    private String generate(int length) {
        StringJoiner joiner = new StringJoiner("");
        try {
            LowerChar lowerChar = new LowerChar();
            UpperChar upperChar = new UpperChar();
            Numeric numeric = new Numeric();
            SpecialChar specialChar = new SpecialChar();

            joiner.add(lowerChar.getRandomOneChar());
            joiner.add(upperChar.getRandomOneChar());
            joiner.add(numeric.getRandomOneChar());
            joiner.add(specialChar.getRandomOneChar());

            ICharacter[] iCharacters = {lowerChar, upperChar, numeric, specialChar};
            for (int i = 0; i < length - 4; i++)
                joiner.add(iCharacters[r.nextInt(iCharacters.length)].getRandomOneChar());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return joiner.toString();
    }

    private class LowerChar implements ICharacter {
        @Override
        public String getRandomOneChar() {
            final char[] alphabet = str.toLowerCase().toCharArray();
            return String.valueOf(alphabet[r.nextInt(alphabet.length)]);
        }
    }

    private class UpperChar implements ICharacter {
        @Override
        public String getRandomOneChar() {
            final char[] ALPHABET = str.toUpperCase().toCharArray();
            return String.valueOf(ALPHABET[r.nextInt(ALPHABET.length)]);
        }
    }

    private class Numeric implements ICharacter {
        @Override
        public String getRandomOneChar() {
            final char[] numeric = "0123456789".toCharArray();
            return String.valueOf(numeric[r.nextInt(numeric.length)]);
        }
    }

    private class SpecialChar implements ICharacter {
        @Override
        public String getRandomOneChar() {
            final char[] special = "~!@#$%^&*()_+<>?,./".toCharArray();
//            final String[] special = {" ", "!", "\"", "#", "$", "%", "&", "'", "(", ")", "*", "+", ",", "-", ".", "/", ":", ";", "<", "=", ">", "?", "@", "[", "\\", "]", "^", "_", "`", "{", "|", "}", "~"};
            return String.valueOf(special[r.nextInt(special.length)]);
        }
    }
}
