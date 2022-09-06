package com.bonynomo.newdivsitetickertracker.exception;

import java.io.IOException;

public class UnableToInitTickersException extends RuntimeException {

    public UnableToInitTickersException(String msg, IOException e) {
        super(msg, e);
    }

    public UnableToInitTickersException(String msg) {
        super(msg);
    }
}
