package com.farm.ibot.api.util;

import com.farm.ibot.api.interfaces.Animable;

import java.awt.*;

public class SeedRandom {
    private java.util.Random random;

    public SeedRandom(long seed) {
        this.random = new java.util.Random(seed);
    }

    public double next(double min, double max) {
        return min + this.random.nextDouble() * (max - min);
    }

    public int next(int min, int max) {
        return (int) ((double) min + this.random.nextDouble() * (double) (max - min));
    }

    public double nextGaussian(double min, double max) {
        return min + this.nextDoubleGaussian() * (max - min);
    }

    public int nextGaussian(int min, int max) {
        return (int) ((double) min + this.nextDoubleGaussian() * (double) (max - min));
    }

    public int nextGaussian(int min, int max, double randomDouble) {
        return (int) ((double) min + randomDouble * (double) (max - min));
    }

    public long nextGaussian(long min, long max) {
        return (long) ((double) min + this.nextDoubleGaussian() * (double) (max - min));
    }

    public int human(int min, int max) {
        return this.nextGaussian(min, max);
    }

    public long human(long min, long max) {
        return this.nextGaussian(min, max);
    }

    public Point next(Rectangle r) {
        return new Point(this.next(r.x, r.x + r.width), this.next(r.y, r.y + r.height));
    }

    public Point human(Rectangle rect) {
        return new Point(rect.x + this.nextGaussian(0, rect.width), rect.y + this.nextGaussian(0, rect.height));
    }

    public Point human(Animable animable) {
        if (animable != null) {
            Point p = animable.getScreenPoint();
            Rectangle rect = new Rectangle(p.x - 20, p.y - 20, 40, 60);
            return this.human(rect);
        } else {
            return new Point(0, 0);
        }
    }

    public double nextDoubleGaussian() {
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
                    rand = this.random.nextGaussian() * deviation + mean;
                } while (rand < min);
            } while (rand >= max);

            return rand;
        }
    }

    public double nextDoubleGaussian(double mean) {
        double min = 0.0D;
        double max = 1.0D;
        double deviation = (max - min) / 10.0D;
        if (min == max) {
            return min;
        } else {
            double rand;
            do {
                do {
                    rand = this.random.nextGaussian() * deviation + mean;
                } while (rand < min);
            } while (rand >= max);

            return rand;
        }
    }
}
