package ru.practicum.ewm.exception.unsupported_enum_value;

public class UnsupportedUserActionException extends UnsupportedEnumValueException {

    public UnsupportedUserActionException(String value) {
        super("UserActionState", value);
    }

}
