package Exceptions;

import java.io.File;

public class DirectoryCreationException extends RuntimeException {
    public DirectoryCreationException(String message) {
        super(message);
    }
    public DirectoryCreationException(String message, String path) {
        super(message + path);
    }
    public DirectoryCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}

