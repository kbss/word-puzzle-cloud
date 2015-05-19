package org.forsp.game.exceptions;

/**
 * @author Serhii Kryvtsov
 * @since 16/05/2015
 */
public class WordNotFoundException extends PuzzleException {
    private static final int ERROR_CODE = 404;

    public WordNotFoundException(String message) {
        super(message, ERROR_CODE);
    }
}
