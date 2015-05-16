package org.forsp.game.exceptions;

import com.google.api.server.spi.ServiceException;
import com.google.api.server.spi.response.BadRequestException;

/**
 * @author Serhii Kryvtsov
 * @since 12/05/2015
 */
public class PuzzleException extends ServiceException {
    private static final int ERROR_CODE = 400;

    public PuzzleException(String message) {
        this(message, ERROR_CODE);
    }

    public PuzzleException(String message, int errorCode) {
        super(errorCode, message);
    }
}
