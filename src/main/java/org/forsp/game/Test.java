package org.forsp.game;

/**
 * @author Serhii Kryvtsov
 * @since 07/05/2015
 */
public class Test {
    public static void main(String[] args) {
        String words = "qwerff9fd";
        int size = (int) Math.sqrt(words.length());
        if ((size * size) < words.length()) {
            System.out.println("Invalid sequence");
        }
        System.out.println(size);

    }
}
