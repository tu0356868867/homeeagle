package com.example.ChatBot.exception;

public enum ErrorCode {
    USER_NOT_EXIST(1001, "User does not exist"),
    USER_EXISTED(1002, "User already exists"),
    ROLE_NOT_EXIST(1003,"Role not found"),
    USER_INVALID(1004,"Username or password incorrect"),

    // Authentication
    UNAUTHENTICATED(2001, "Unauthenticated"),
    UNAUTHORIZED(2002, "You do not have permission"),

    //Post
    POST_EXISTED(3001, "Your post already exists"),

    //Format
    INVALID_NUMBER_FORMAT(4001, "Input is not in the correct format."),

    //Home
    HOME_NOT_EXISTED(5001, "Home does not existed!"),
    HOME_NOT_OWNER(5002, "You do not have permission to delete this home!"),
    HOME_NOT_VEW(5003, "You do not have permission to view this home!"),
    HOME_NOT_CHOSE(5004,"You haven't selected a home yet."),

    //Invalid
    INVALID_DELETE(5005,"You do not have permission to delete this!"),
    INVALID_ACCESS(5006, "This resource does not belong to you!"),
    RESOURCES_NOT_AVAILABLE(5007, "Resources not available"),
    INVALID_DATA(6001, "INVALID_DATA");
    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
