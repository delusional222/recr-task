package com.atipera.demo.GitHubRepositories.Exception;

public class UserNotFoundException extends RuntimeException {
    private final int status;
    private final String message;

    public UserNotFoundException(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}