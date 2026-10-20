package dev.gallon.services;

/**
 * Converts the 'jump_strength' attribute of a mount into the height in blocks it can clear with a fully charged jump.
 * <p>
 * The jump is simulated tick by tick the same way the game does it: {@code LivingEntity#getJumpPower} turns the jump
 * strength (cast to a float) into the initial vertical velocity, then {@code LivingEntity#travelInAir} moves the mount
 * by its velocity, subtracts the gravity (0.08) and applies the vertical air drag (0.98F) every tick. The height is
 * the sum of the velocities while the mount goes up. This gives 1.1531 blocks for the minimum jump strength of 0.4 and
 * 5.9197 blocks for the maximum of 1.0, matching the values listed on the <a href="https://minecraft.wiki/w/Horse">mc wiki</a>.
 */
public class JumpHeightConverter {
    private static final double GRAVITY = 0.08;
    private static final double VERTICAL_AIR_DRAG = 0.98F;
    // The jump strength attribute can't go above 32 in the game
    private static final double MAX_JUMP_STRENGTH = 32.0;

    /**
     * @param jumpStrength The mount's 'jump_strength' attribute (0.4 to 1.0 for horses)
     * @return The jump height in blocks, or NaN if the jump strength isn't a finite number
     */
    public static double getJumpHeight(double jumpStrength) {
        if (!Double.isFinite(jumpStrength)) {
            return Double.NaN;
        }

        double velocity = (float) Math.min(jumpStrength, MAX_JUMP_STRENGTH);
        double height = 0;
        while (velocity > 0) {
            height += velocity;
            velocity = (velocity - GRAVITY) * VERTICAL_AIR_DRAG;
        }

        return height;
    }
}
