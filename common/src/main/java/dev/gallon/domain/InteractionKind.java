package dev.gallon.domain;

public enum InteractionKind {
    RIGHT_CLICK,
    SHIFT_RIGHT_CLICK,
    RIGHT_OR_SHIFT_RIGHT_CLICK,
    MIDDLE_CLICK,
    DISABLED;

    public boolean matchesRightClick(boolean shiftKeyDown) {
        return switch (this) {
            case RIGHT_CLICK -> !shiftKeyDown;
            case SHIFT_RIGHT_CLICK -> shiftKeyDown;
            case RIGHT_OR_SHIFT_RIGHT_CLICK -> true;
            case MIDDLE_CLICK, DISABLED -> false;
        };
    }
}
