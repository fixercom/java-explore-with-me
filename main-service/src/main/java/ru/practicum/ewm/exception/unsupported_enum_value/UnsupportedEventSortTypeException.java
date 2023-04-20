package ru.practicum.ewm.exception.unsupported_enum_value;

public class UnsupportedEventSortTypeException extends UnsupportedEnumValueException {
    public UnsupportedEventSortTypeException(String value) {
        super("EventSortType", value);
    }
}
