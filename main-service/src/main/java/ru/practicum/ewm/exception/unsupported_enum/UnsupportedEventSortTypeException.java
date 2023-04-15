package ru.practicum.ewm.exception.unsupported_enum;

public class UnsupportedEventSortTypeException extends UnsupportedEnumValueException {
    public UnsupportedEventSortTypeException(String value) {
        super("EventSortType", value);
    }
}
