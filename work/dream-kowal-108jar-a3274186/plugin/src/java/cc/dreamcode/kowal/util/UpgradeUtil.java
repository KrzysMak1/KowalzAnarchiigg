package cc.dreamcode.kowal.util;

public final class UpgradeUtil {
    private UpgradeUtil() {
    }

    private static final java.util.regex.Pattern LEVEL_SUFFIX_PATTERN = java.util.regex.Pattern.compile("(?i)\\s*(?:[&§][0-9A-FK-OR])*\\+\\d+$");

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

    public static String buildUpgradeName(final String baseName, final String colorSuffix, final int level) {
        if (baseName == null) {
            return null;
        }
        final String cleaned = LEVEL_SUFFIX_PATTERN.matcher(baseName).replaceFirst("");
        if (level <= 0) {
            return cleaned;
        }
        String suffix = colorSuffix;
        if (suffix == null || suffix.isBlank()) {
            suffix = "+" + level;
        }
        else {
            suffix = suffix.replace("{level}", String.valueOf(level));
        }
        return cleaned + " " + suffix;
    }
}
