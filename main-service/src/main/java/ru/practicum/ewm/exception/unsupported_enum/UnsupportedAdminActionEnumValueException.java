package ru.practicum.ewm.exception.unsupported_enum;

public class UnsupportedAdminActionEnumValueException extends UnsupportedEnumValueException {

    public UnsupportedAdminActionEnumValueException(String value) {
        super("AdminActionState", value);
    }
}
