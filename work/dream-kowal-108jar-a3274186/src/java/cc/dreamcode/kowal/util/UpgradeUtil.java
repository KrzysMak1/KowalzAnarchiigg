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
}
