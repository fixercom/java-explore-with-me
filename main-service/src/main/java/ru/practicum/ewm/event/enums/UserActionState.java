package ru.practicum.ewm.event.enums;

import ru.practicum.ewm.exception.unsupported_enum_value.UnsupportedUserActionException;

public enum UserActionState {

    SEND_TO_REVIEW,
    CANCEL_REVIEW;

    public static UserActionState fromName(String name) {
        if (name == null) return null;
        try {
            return UserActionState.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedUserActionException(name);
        }
    }

}
