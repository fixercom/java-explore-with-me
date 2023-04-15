package ru.practicum.ewm.exception.unsupported_enum;

public class UnsupportedUserActionEnumValueException extends UnsupportedEnumValueException {

    public UnsupportedUserActionEnumValueException(String value) {
        super("UserActionState", value);
    }

}
