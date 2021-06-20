package com.farm.ibot.api.util;

import com.farm.ibot.api.interfaces.Animable;

import java.awt.*;
import java.security.SecureRandom;

public class Random {
    private static final SecureRandom random = new SecureRandom();

    public static double next(double min, double max) {
        return min + random.nextDouble() * (max - min);
    }

    public static int next(int min, int max) {
        return (int) ((double) min + random.nextDouble() * (double) (max - min));
    }

    public static double nextGaussian(double min, double max) {
        return min + nextDoubleGaussian() * (max - min);
    }

    public static int nextGaussian(int min, int max) {
        return (int) ((double) min + nextDoubleGaussian() * (double) (max - min));
    }

    public static long nextGaussian(long min, long max) {
        return (long) ((double) min + nextDoubleGaussian() * (double) (max - min));
    }

    public static int human(int min, int max) {
        return nextGaussian(min, max);
    }

    public static long human(long min, long max) {
        return nextGaussian(min, max);
    }

    public static Point next(Rectangle r) {
        return new Point(next(r.x, r.x + r.width), next(r.y, r.y + r.height));
    }

    public static Point human(Rectangle rect) {
        return new Point(rect.x + nextGaussian(0, rect.width), rect.y + nextGaussian(0, rect.height));
    }

    public static Point human(Animable animable) {
        if (animable != null) {
            Point p = animable.getScreenPoint();
            Rectangle rect = new Rectangle(p.x - 20, p.y - 20, 40, 60);
            return human(rect);
        } else {
            return new Point(0, 0);
        }
    }

    public static double nextDoubleGaussian() {
        double min = 0.0D;
        double max = 1.0D;
        double mean = min + (max - min) / 2.0D;
        double deviation = (max - min) / 10.0D;
        if (min == max) {
            return min;
        } else {
            double rand;
            do {
                do {
                    rand = random.nextGaussian() * deviation + mean;
                } while (rand < min);
            } while (rand >= max);

            return rand;
        }
    }
}
