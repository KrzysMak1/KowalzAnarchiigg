package cc.dreamcode.kowal.economy;

import java.util.Locale;

public enum PaymentMode {
    MONEY_ONLY,
    ITEMS_ONLY,
    MONEY_AND_ITEMS;

    public static PaymentMode fromString(final String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        final String normalized = raw.trim().toUpperCase(Locale.ROOT);
        for (final PaymentMode mode : values()) {
            if (mode.name().equals(normalized)) {
                return mode;
            }
        }
        return null;
    }

    public boolean usesMoney() {
        return this == MONEY_ONLY || this == MONEY_AND_ITEMS;
    }

    public boolean usesItems() {
        return this == ITEMS_ONLY || this == MONEY_AND_ITEMS;
    }
}
