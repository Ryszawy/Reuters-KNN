package pl.ksr.exception;

import java.io.IOException;

public class DataNotFoundException extends IOException {
    public DataNotFoundException(String message) {
        super(message);
    }
}
