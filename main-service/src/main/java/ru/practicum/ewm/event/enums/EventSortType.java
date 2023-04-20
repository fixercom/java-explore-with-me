package ru.practicum.ewm.event.enums;

import ru.practicum.ewm.exception.unsupported_enum_value.UnsupportedEventSortTypeException;

public enum EventSortType   {

    EVENT_DATE,
    VIEWS;

    public static EventSortType fromName(String name) {
        if (name == null) return null;
        try {
            return EventSortType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedEventSortTypeException(name);
        }
    }
}
