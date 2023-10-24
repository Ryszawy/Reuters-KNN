package pl.ksr.exception;

import java.io.IOException;

public class DictionaryNotFoundException extends IOException {
    public DictionaryNotFoundException(String message) {
        super(message);
    }
}
