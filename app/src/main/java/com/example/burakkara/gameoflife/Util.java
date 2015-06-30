package com.example.burakkara.gameoflife;

import java.util.Arrays;

/**
 * Author: karab on 27/06/15.
 */
public class Util {
    public static int[][] deepCopy(int[][] original) {
        if (original == null) {
            return null;
        }

        final int[][] result = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }

}
