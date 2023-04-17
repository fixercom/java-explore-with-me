package ru.practicum.ewm.event.enums;

import ru.practicum.ewm.exception.unsupported_enum_value.UnsupportedAdminActionException;

public enum AdminActionState {

    PUBLISH_EVENT,
    REJECT_EVENT;

    public static AdminActionState fromName(String name) {
        if (name == null) return null;
        try {
            return AdminActionState.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedAdminActionException(name);
        }
    }
}
