package ru.practicum.ewm.event.enums;

import ru.practicum.ewm.exception.unsupported_enum.UnsupportedAdminActionEnumValueException;

public enum AdminActionState {

    PUBLISH_EVENT,
    REJECT_EVENT;

    public static AdminActionState fromName(String name) {
        if (name == null) return null;
        try {
            return AdminActionState.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedAdminActionEnumValueException(name);
        }
    }
}
