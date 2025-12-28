package dev.gallon.services;

import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Utility class to convert Minecraft Horse 'jump_strength' attribute
 * into actual jump height in blocks.
 * Based on data mapping from this <a href="https://gist.github.com/Micalobia/f61c902d0c76d582865e8470b0fc4757">gist</a>,
 * cited by the <a href="https://minecraft.wiki/w/Horse#cite_note-2">mc wiki</a>
 * Values calibrated to match the range: 0.4 (1.153 blocks) to 1.0 (5.919 blocks).
 */
public class JumpHeightConverter {

    // Internal map storing (Strength -> Height) pairs from the CSV
    private static final NavigableMap<Double, Double> JUMP_DATA = new TreeMap<>();

    static {
        // Data points extracted from the jump strength curve (every 0.05)
        // Format: JUMP_DATA.put(StrengthValue, HeightInBlocks);
        JUMP_DATA.put(0.400, 1.153);
        JUMP_DATA.put(0.405, 1.177);
        JUMP_DATA.put(0.410, 1.202);
        JUMP_DATA.put(0.415, 1.226);
        JUMP_DATA.put(0.420, 1.252);
        JUMP_DATA.put(0.425, 1.277);
        JUMP_DATA.put(0.430, 1.303);
        JUMP_DATA.put(0.435, 1.328);
        JUMP_DATA.put(0.440, 1.354);
        JUMP_DATA.put(0.445, 1.379);
        JUMP_DATA.put(0.450, 1.405);
        JUMP_DATA.put(0.455, 1.431);
        JUMP_DATA.put(0.460, 1.457);
        JUMP_DATA.put(0.465, 1.483);
        JUMP_DATA.put(0.470, 1.509);
        JUMP_DATA.put(0.475, 1.535);
        JUMP_DATA.put(0.480, 1.561);
        JUMP_DATA.put(0.485, 1.587);
        JUMP_DATA.put(0.490, 1.614);
        JUMP_DATA.put(0.495, 1.640);
        JUMP_DATA.put(0.500, 1.667);
        JUMP_DATA.put(0.505, 1.694);
        JUMP_DATA.put(0.510, 1.721);
        JUMP_DATA.put(0.515, 1.747);
        JUMP_DATA.put(0.520, 1.775);
        JUMP_DATA.put(0.525, 1.802);
        JUMP_DATA.put(0.530, 1.829);
        JUMP_DATA.put(0.535, 1.857);
        JUMP_DATA.put(0.540, 1.884);
        JUMP_DATA.put(0.545, 1.912);
        JUMP_DATA.put(0.550, 1.940);
        JUMP_DATA.put(0.555, 1.968);
        JUMP_DATA.put(0.560, 1.996);
        JUMP_DATA.put(0.565, 2.024);
        JUMP_DATA.put(0.570, 2.053);
        JUMP_DATA.put(0.575, 2.081);
        JUMP_DATA.put(0.580, 2.110);
        JUMP_DATA.put(0.585, 2.138);
        JUMP_DATA.put(0.590, 2.167);
        JUMP_DATA.put(0.595, 2.196);
        JUMP_DATA.put(0.600, 2.225);
        JUMP_DATA.put(0.605, 2.255);
        JUMP_DATA.put(0.610, 2.284);
        JUMP_DATA.put(0.615, 2.314);
        JUMP_DATA.put(0.620, 2.343);
        JUMP_DATA.put(0.625, 2.373);
        JUMP_DATA.put(0.630, 2.403);
        JUMP_DATA.put(0.635, 2.433);
        JUMP_DATA.put(0.640, 2.463);
        JUMP_DATA.put(0.645, 2.493);
        JUMP_DATA.put(0.650, 2.524);
        JUMP_DATA.put(0.655, 2.554);
        JUMP_DATA.put(0.660, 2.585);
        JUMP_DATA.put(0.665, 2.616);
        JUMP_DATA.put(0.670, 2.647);
        JUMP_DATA.put(0.675, 2.678);
        JUMP_DATA.put(0.680, 2.709);
        JUMP_DATA.put(0.685, 2.740);
        JUMP_DATA.put(0.690, 2.772);
        JUMP_DATA.put(0.695, 2.804);
        JUMP_DATA.put(0.700, 2.835);
        JUMP_DATA.put(0.705, 2.867);
        JUMP_DATA.put(0.710, 2.899);
        JUMP_DATA.put(0.715, 2.932);
        JUMP_DATA.put(0.720, 2.964);
        JUMP_DATA.put(0.725, 2.997);
        JUMP_DATA.put(0.730, 3.029);
        JUMP_DATA.put(0.735, 3.062);
        JUMP_DATA.put(0.740, 3.095);
        JUMP_DATA.put(0.745, 3.128);
        JUMP_DATA.put(0.750, 3.161);
        JUMP_DATA.put(0.755, 3.194);
        JUMP_DATA.put(0.760, 3.228);
        JUMP_DATA.put(0.765, 3.261);
        JUMP_DATA.put(0.770, 3.295);
        JUMP_DATA.put(0.775, 3.329);
        JUMP_DATA.put(0.780, 3.363);
        JUMP_DATA.put(0.785, 3.397);
        JUMP_DATA.put(0.790, 3.431);
        JUMP_DATA.put(0.795, 3.465);
        JUMP_DATA.put(0.800, 3.500);
        JUMP_DATA.put(0.805, 3.534);
        JUMP_DATA.put(0.810, 3.569);
        JUMP_DATA.put(0.815, 3.604);
        JUMP_DATA.put(0.820, 3.639);
        JUMP_DATA.put(0.825, 3.674);
        JUMP_DATA.put(0.830, 3.709);
        JUMP_DATA.put(0.835, 3.744);
        JUMP_DATA.put(0.840, 3.780);
        JUMP_DATA.put(0.845, 3.816);
        JUMP_DATA.put(0.850, 3.851);
        JUMP_DATA.put(0.855, 3.887);
        JUMP_DATA.put(0.860, 3.923);
        JUMP_DATA.put(0.865, 3.960);
        JUMP_DATA.put(0.870, 3.996);
        JUMP_DATA.put(0.875, 4.032);
        JUMP_DATA.put(0.880, 4.069);
        JUMP_DATA.put(0.885, 4.106);
        JUMP_DATA.put(0.890, 4.143);
        JUMP_DATA.put(0.895, 4.180);
        JUMP_DATA.put(0.900, 4.217);
        JUMP_DATA.put(0.905, 4.254);
        JUMP_DATA.put(0.910, 4.292);
        JUMP_DATA.put(0.915, 4.329);
        JUMP_DATA.put(0.920, 4.367);
        JUMP_DATA.put(0.925, 4.405);
        JUMP_DATA.put(0.930, 4.443);
        JUMP_DATA.put(0.935, 4.482);
        JUMP_DATA.put(0.940, 4.520);
        JUMP_DATA.put(0.945, 4.559);
        JUMP_DATA.put(0.950, 4.597);
        JUMP_DATA.put(0.955, 4.636);
        JUMP_DATA.put(0.960, 4.675);
        JUMP_DATA.put(0.965, 4.715);
        JUMP_DATA.put(0.970, 4.754);
        JUMP_DATA.put(0.975, 4.794);
        JUMP_DATA.put(0.980, 4.833);
        JUMP_DATA.put(0.985, 4.873);
        JUMP_DATA.put(0.990, 4.913);
        JUMP_DATA.put(0.995, 4.954);
        JUMP_DATA.put(1.000, 5.920);
    }

    /**
     * Calculates the exact jump height in blocks using Linear Interpolation.
     * This method handles all values between the defined data points.
     *
     * @param jumpStrength The horse's internal 'jump_strength' (usually 0.4 to 1.0)
     * @return The jump height in blocks (e.g., 2.5)
     */
    public static double getJumpHeight(double jumpStrength) {
        // Boundary check: value lower than minimum
        if (jumpStrength <= JUMP_DATA.firstKey()) {
            return JUMP_DATA.firstEntry().getValue();
        }

        // Boundary check: value higher than maximum
        if (jumpStrength >= JUMP_DATA.lastKey()) {
            return JUMP_DATA.lastEntry().getValue();
        }

        // Find the two closest data points for interpolation
        var lowEntry = JUMP_DATA.floorEntry(jumpStrength);
        var highEntry = JUMP_DATA.ceilingEntry(jumpStrength);

        // If the value exactly matches a key in the map
        if (lowEntry.getKey().equals(highEntry.getKey())) {
            return lowEntry.getValue();
        }

        // Linear Interpolation Formula: y = y0 + (x - x0) * (y1 - y0) / (x1 - x0)
        double x0 = lowEntry.getKey();
        double y0 = lowEntry.getValue();
        double x1 = highEntry.getKey();
        double y1 = highEntry.getValue();

        return y0 + (jumpStrength - x0) * (y1 - y0) / (x1 - x0);
    }
}
