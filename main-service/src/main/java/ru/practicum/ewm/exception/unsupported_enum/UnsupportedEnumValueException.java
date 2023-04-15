package ru.practicum.ewm.exception.unsupported_enum;

public abstract class UnsupportedEnumValueException extends RuntimeException {

    public UnsupportedEnumValueException(String enumStateType, String value) {
        super(String.format("Unsupported %s is %s", enumStateType, value));
    }

}
