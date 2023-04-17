package ru.practicum.ewm.exception.unsupported_enum_value;

public class UnsupportedRequestStatusException extends UnsupportedEnumValueException {

    public UnsupportedRequestStatusException(String value) {
        super("RequestStatus", value);
    }

}
