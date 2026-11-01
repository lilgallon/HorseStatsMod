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
        JUMP_DATA.put(0.410, 1.201);
        JUMP_DATA.put(0.415, 1.225);
        JUMP_DATA.put(0.420, 1.252);
        JUMP_DATA.put(0.425, 1.281);
        JUMP_DATA.put(0.430, 1.309);
        JUMP_DATA.put(0.435, 1.338);
        JUMP_DATA.put(0.440, 1.366);
        JUMP_DATA.put(0.445, 1.395);
        JUMP_DATA.put(0.450, 1.423);
        JUMP_DATA.put(0.455, 1.452);
        JUMP_DATA.put(0.460, 1.481);
        JUMP_DATA.put(0.465, 1.509);
        JUMP_DATA.put(0.470, 1.538);
        JUMP_DATA.put(0.475, 1.566);
        JUMP_DATA.put(0.480, 1.595);
        JUMP_DATA.put(0.485, 1.623);
        JUMP_DATA.put(0.490, 1.652);
        JUMP_DATA.put(0.495, 1.680);
        JUMP_DATA.put(0.500, 1.709);
        JUMP_DATA.put(0.505, 1.737);
        JUMP_DATA.put(0.510, 1.770);
        JUMP_DATA.put(0.515, 1.803);
        JUMP_DATA.put(0.520, 1.836);
        JUMP_DATA.put(0.525, 1.869);
        JUMP_DATA.put(0.530, 1.902);
        JUMP_DATA.put(0.535, 1.935);
        JUMP_DATA.put(0.540, 1.968);
        JUMP_DATA.put(0.545, 2.001);
        JUMP_DATA.put(0.550, 2.034);
        JUMP_DATA.put(0.555, 2.067);
        JUMP_DATA.put(0.560, 2.100);
        JUMP_DATA.put(0.565, 2.133);
        JUMP_DATA.put(0.570, 2.166);
        JUMP_DATA.put(0.575, 2.199);
        JUMP_DATA.put(0.580, 2.232);
        JUMP_DATA.put(0.585, 2.265);
        JUMP_DATA.put(0.590, 2.298);
        JUMP_DATA.put(0.595, 2.331);
        JUMP_DATA.put(0.600, 2.368);
        JUMP_DATA.put(0.605, 2.405);
        JUMP_DATA.put(0.610, 2.442);
        JUMP_DATA.put(0.615, 2.479);
        JUMP_DATA.put(0.620, 2.517);
        JUMP_DATA.put(0.625, 2.554);
        JUMP_DATA.put(0.630, 2.591);
        JUMP_DATA.put(0.635, 2.629);
        JUMP_DATA.put(0.640, 2.666);
        JUMP_DATA.put(0.645, 2.703);
        JUMP_DATA.put(0.650, 2.741);
        JUMP_DATA.put(0.655, 2.778);
        JUMP_DATA.put(0.660, 2.815);
        JUMP_DATA.put(0.665, 2.853);
        JUMP_DATA.put(0.670, 2.890);
        JUMP_DATA.put(0.675, 2.927);
        JUMP_DATA.put(0.680, 2.965);
        JUMP_DATA.put(0.685, 3.002);
        JUMP_DATA.put(0.690, 3.039);
        JUMP_DATA.put(0.695, 3.083);
        JUMP_DATA.put(0.700, 3.124);
        JUMP_DATA.put(0.705, 3.166);
        JUMP_DATA.put(0.710, 3.207);
        JUMP_DATA.put(0.715, 3.249);
        JUMP_DATA.put(0.720, 3.291);
        JUMP_DATA.put(0.725, 3.332);
        JUMP_DATA.put(0.730, 3.374);
        JUMP_DATA.put(0.735, 3.415);
        JUMP_DATA.put(0.740, 3.457);
        JUMP_DATA.put(0.745, 3.498);
        JUMP_DATA.put(0.750, 3.540);
        JUMP_DATA.put(0.755, 3.581);
        JUMP_DATA.put(0.760, 3.623);
        JUMP_DATA.put(0.765, 3.665);
        JUMP_DATA.put(0.770, 3.706);
        JUMP_DATA.put(0.775, 3.748);
        JUMP_DATA.put(0.780, 3.789);
        JUMP_DATA.put(0.785, 3.831);
        JUMP_DATA.put(0.790, 3.879);
        JUMP_DATA.put(0.795, 3.925);
        JUMP_DATA.put(0.800, 3.971);
        JUMP_DATA.put(0.805, 4.017);
        JUMP_DATA.put(0.810, 4.062);
        JUMP_DATA.put(0.815, 4.108);
        JUMP_DATA.put(0.820, 4.154);
        JUMP_DATA.put(0.825, 4.199);
        JUMP_DATA.put(0.830, 4.245);
        JUMP_DATA.put(0.835, 4.291);
        JUMP_DATA.put(0.840, 4.337);
        JUMP_DATA.put(0.845, 4.382);
        JUMP_DATA.put(0.850, 4.428);
        JUMP_DATA.put(0.855, 4.474);
        JUMP_DATA.put(0.860, 4.520);
        JUMP_DATA.put(0.865, 4.565);
        JUMP_DATA.put(0.870, 4.611);
        JUMP_DATA.put(0.875, 4.657);
        JUMP_DATA.put(0.880, 4.703);
        JUMP_DATA.put(0.885, 4.754);
        JUMP_DATA.put(0.890, 4.804);
        JUMP_DATA.put(0.895, 4.854);
        JUMP_DATA.put(0.900, 4.904);
        JUMP_DATA.put(0.905, 4.954);
        JUMP_DATA.put(0.910, 5.003);
        JUMP_DATA.put(0.915, 5.053);
        JUMP_DATA.put(0.920, 5.103);
        JUMP_DATA.put(0.925, 5.153);
        JUMP_DATA.put(0.930, 5.203);
        JUMP_DATA.put(0.935, 5.252);
        JUMP_DATA.put(0.940, 5.302);
        JUMP_DATA.put(0.945, 5.352);
        JUMP_DATA.put(0.950, 5.402);
        JUMP_DATA.put(0.955, 5.452);
        JUMP_DATA.put(0.960, 5.502);
        JUMP_DATA.put(0.965, 5.551);
        JUMP_DATA.put(0.970, 5.601);
        JUMP_DATA.put(0.975, 5.651);
        JUMP_DATA.put(0.980, 5.704);
        JUMP_DATA.put(0.985, 5.758);
        JUMP_DATA.put(0.990, 5.812);
        JUMP_DATA.put(0.995, 5.866);
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
        if (Double.isNaN(jumpStrength)) {
            return JUMP_DATA.firstEntry().getValue();
        }

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
