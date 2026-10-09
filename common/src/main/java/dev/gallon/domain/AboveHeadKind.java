package dev.gallon.domain;

public enum AboveHeadKind {
    WHEN_LOOKING,
    ALWAYS,
    DISABLED;

    /**
     * Same range as vanilla name tags.
     */
    public static final double WHEN_LOOKING_RANGE = 64.0;

    /**
     * Shorter than vanilla name tags so that a herd does not fill the screen with text.
     */
    static final double ALWAYS_RANGE = 32.0;

    public boolean shouldDisplay(boolean lookedAt, double distanceToCameraSq) {
        return switch (this) {
            case WHEN_LOOKING -> lookedAt && distanceToCameraSq < WHEN_LOOKING_RANGE * WHEN_LOOKING_RANGE;
            case ALWAYS -> distanceToCameraSq < ALWAYS_RANGE * ALWAYS_RANGE;
            case DISABLED -> false;
        };
    }
}
