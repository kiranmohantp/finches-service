package com.finches.finchesservice.constents.messages;

public class MappedWarnings {
    private String code;
    private String message;

    public MappedWarnings(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
