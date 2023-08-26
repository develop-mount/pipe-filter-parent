package icu.develop.expression.filter.utils;

/**
 *
 * @author KK
 */
public enum TrafficUnit {

    /**
     * Traffic unit
     */
    B {
        public double toB(double d)   { return d; }
        public double toKB(double d)  { return d/(C1/C0); }
        public double toMB(double d)  { return d/(C2/C0); }
        public double toGB(double d) { return d/(C3/C0); }
        public double convert(double d, TrafficUnit u) { return u.toB(d); }
    },

    /**
     * Time unit representing one thousandth of a millisecond
     */
    KB {
        public double toB(double d)   { return x(d, C1/C0, MAX/(C1/C0)); }
        public double toKB(double d)  { return d; }
        public double toMB(double d)  { return d/(C2/C1); }
        public double toGB(double d) { return d/(C3/C1); }
        public double convert(double d, TrafficUnit u) { return u.toKB(d); }
    },

    /**
     * Time unit representing one thousandth of a second
     */
    MB {
        public double toB(double d)   { return x(d, C2/C0, MAX/(C2/C0)); }
        public double toKB(double d)  { return x(d, C2/C1, MAX/(C2/C1)); }
        public double toMB(double d)  { return d; }
        public double toGB(double d) { return d/(C3/C2); }
        public double convert(double d, TrafficUnit u) { return u.toMB(d); }
    },

    /**
     * Time unit representing one second
     */
    GB {
        public double toB(double d)   { return x(d, C3/C0, MAX/(C3/C0)); }
        public double toKB(double d)  { return x(d, C3/C1, MAX/(C3/C1)); }
        public double toMB(double d)  { return x(d, C3/C2, MAX/(C3/C2)); }
        public double toGB(double d) { return d; }
        public double convert(double d, TrafficUnit u) { return u.toGB(d); }
    };

    // Handy constants for conversion methods
    static final long C0 = 1L;
    static final long C1 = C0 * 1024L;
    static final long C2 = C1 * 1024L;
    static final long C3 = C2 * 1024L;

    static final long MAX = Long.MAX_VALUE;

    /**
     * Scale d by m, checking for overflow.
     * This has a short name to make above code more readable.
     */
    static double x(double d, long m, long over) {
        if (d >  over) {
            return Long.MAX_VALUE;
        }
        if (d < -over) {
            return Long.MIN_VALUE;
        }
        return d * m;
    }

    /**
     *
     * @param sourceDuration
     * @param sourceUnit
     * @return
     */
    public double convert(double sourceDuration, TrafficUnit sourceUnit) {
        throw new AbstractMethodError();
    }

    /**
     *
     * @param duration
     * @return
     */
    public double toB(double duration) {
        throw new AbstractMethodError();
    }

    /**
     *
     * @param duration
     * @return
     */
    public double toKB(double duration) {
        throw new AbstractMethodError();
    }

    /**
     *
     * @param duration
     * @return
     */
    public double toMB(double duration) {
        throw new AbstractMethodError();
    }

    /**
     *
     * @param duration
     * @return
     */
    public double toGB(double duration) {
        throw new AbstractMethodError();
    }

    public static TrafficUnit ofName(String name) {
        for (TrafficUnit value : values()) {
            if (value.name().equalsIgnoreCase(name)) {
                return value;
            }
        }
        return null;
    }

    public static boolean isB(TrafficUnit trafficUnit) {
        return TrafficUnit.B.name().equals(trafficUnit.name());
    }

    public static boolean isKB(TrafficUnit trafficUnit) {
        return TrafficUnit.KB.name().equals(trafficUnit.name());
    }

    public static boolean isMB(TrafficUnit trafficUnit) {
        return TrafficUnit.MB.name().equals(trafficUnit.name());
    }

    public static boolean isGB(TrafficUnit trafficUnit) {
        return TrafficUnit.GB.name().equals(trafficUnit.name());
    }
}
