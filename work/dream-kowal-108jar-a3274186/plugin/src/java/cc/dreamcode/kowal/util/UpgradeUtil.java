package cc.dreamcode.kowal.util;

public final class UpgradeUtil {
    private UpgradeUtil() {
    }

    public static int parseLevel(final String rawLevel) {
        if (rawLevel == null || rawLevel.isBlank()) {
            return 0;
        }
        try {
            return Integer.parseInt(rawLevel);
        }
        catch (final NumberFormatException ex) {
            return 0;
        }
    }

    public static int parseLevel(final Object rawLevel) {
        if (rawLevel == null) {
            return 0;
        }
        if (rawLevel instanceof Number) {
            return ((Number)rawLevel).intValue();
        }
        return parseLevel(rawLevel.toString());
    }
}
