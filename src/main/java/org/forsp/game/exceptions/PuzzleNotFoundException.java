package org.forsp.game.exceptions;

/**
 * @author Serhii Kryvtsov
 * @since 16/05/2015
 */
public class PuzzleNotFoundException extends PuzzleException {
    private static final int ERROR_CODE = 404;

    public PuzzleNotFoundException(Long gameId) {
        super(String.format("Game with id: %s not found", gameId), ERROR_CODE);
    }
}
