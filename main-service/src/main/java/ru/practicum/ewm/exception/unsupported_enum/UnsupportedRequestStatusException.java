package ru.practicum.ewm.exception.unsupported_enum;

public class UnsupportedRequestStatusException extends UnsupportedEnumValueException {

    public UnsupportedRequestStatusException(String value) {
        super("RequestStatus", value);
    }

}
