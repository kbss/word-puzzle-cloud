package org.forsp.game.exceptions;

import com.google.api.server.spi.response.BadRequestException;

/**
 * @author Serhii Kryvtsov
 * @since 12/05/2015
 */
public class PuzzleException extends BadRequestException {
    public PuzzleException(String message) {
        super(message);
    }
}
