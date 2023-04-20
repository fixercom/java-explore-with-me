package ru.practicum.ewm.exception.unsupported_enum_value;

public class UnsupportedAdminActionException extends UnsupportedEnumValueException {

    public UnsupportedAdminActionException(String value) {
        super("AdminActionState", value);
    }
}
