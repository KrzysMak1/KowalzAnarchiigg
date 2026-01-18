package cc.dreamcode.kowal.libs.eu.okaeri.placeholders.schema.resolver;

import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import java.util.UUID;
import java.math.BigInteger;
import java.time.temporal.TemporalAccessor;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.Instant;
import java.util.Locale;
import cc.dreamcode.kowal.libs.eu.okaeri.pluralize.Pluralize;
import java.math.BigDecimal;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.message.part.MessageField;
import lombok.NonNull;
import java.util.Set;

public class DefaultSchemaResolver implements SchemaResolver
{
    public static final SchemaResolver INSTANCE;
    private static final Set<Class<?>> SUPPORTED_TYPES;
    
    @Override
    public boolean supports(@NonNull final Class<?> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return DefaultSchemaResolver.SUPPORTED_TYPES.contains((Object)type) || type.isEnum();
    }
    
    @Override
    public String resolve(@NonNull final Object object, @NonNull final MessageField field) {
        if (object == null) {
            throw new NullPointerException("object is marked non-null but is null");
        }
        if (field == null) {
            throw new NullPointerException("field is marked non-null but is null");
        }
        if (field.getMetadataRaw() != null && object instanceof Number && field.getMetadataRaw().length() > 1 && field.getMetadataRaw().charAt(0) == '%') {
            final double doubleValue = new BigDecimal(String.valueOf(object)).doubleValue();
            return String.format(field.getLocale(), field.getMetadataRaw(), new Object[] { doubleValue });
        }
        if (field.getMetadataOptions() != null && object instanceof Number) {
            final int intValue = new BigDecimal(String.valueOf(object)).intValue();
            try {
                if (field.getMetadataOptions().length == Pluralize.plurals(field.getLocale())) {
                    return Pluralize.pluralize(field.getLocale(), intValue, field.getMetadataOptions());
                }
            }
            catch (final IllegalArgumentException exception) {
                try {
                    return Pluralize.pluralize(Locale.ENGLISH, intValue, field.getMetadataOptions());
                }
                catch (final IllegalArgumentException exception2) {
                    return field.getMetadataOptions()[0];
                }
            }
        }
        if (field.getMetadataOptions() != null && object instanceof Boolean && field.getMetadataOptions().length == 2) {
            return object ? field.getMetadataOptions()[0] : field.getMetadataOptions()[1];
        }
        if (field.getMetadataRaw() != null && object instanceof Instant) {
            final String[] metadataOptions = field.getMetadataOptions();
            final String rawFormat = metadataOptions[0].toUpperCase(Locale.ROOT);
            FormatStyle style = null;
            String pattern = null;
            if ("P".equals((Object)rawFormat)) {
                if (metadataOptions.length < 2) {
                    throw new IllegalArgumentException("The pattern formatter ('P') requires a pattern as a second metadata option.");
                }
                pattern = metadataOptions[1];
            }
            else {
                style = ((metadataOptions.length >= 2) ? FormatStyle.valueOf(metadataOptions[1].toUpperCase(Locale.ROOT)) : FormatStyle.SHORT);
            }
            final String s = rawFormat;
            int n = -1;
            switch (s.hashCode()) {
                case 2440: {
                    if (s.equals((Object)"LT")) {
                        n = 0;
                        break;
                    }
                    break;
                }
                case 75228: {
                    if (s.equals((Object)"LDT")) {
                        n = 1;
                        break;
                    }
                    break;
                }
                case 2424: {
                    if (s.equals((Object)"LD")) {
                        n = 2;
                        break;
                    }
                    break;
                }
                case 80: {
                    if (s.equals((Object)"P")) {
                        n = 3;
                        break;
                    }
                    break;
                }
            }
            DateTimeFormatter formatter = null;
            switch (n) {
                case 0: {
                    formatter = DateTimeFormatter.ofLocalizedTime(style);
                    break;
                }
                case 1: {
                    formatter = DateTimeFormatter.ofLocalizedDateTime(style);
                    break;
                }
                case 2: {
                    formatter = DateTimeFormatter.ofLocalizedDate(style);
                    break;
                }
                case 3: {
                    formatter = DateTimeFormatter.ofPattern(pattern);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Unknown time formatter: " + rawFormat);
                }
            }
            final ZoneId zone = (field.getMetadataRaw() == null || field.getMetadataOptions().length < 3) ? ZoneId.systemDefault() : ZoneId.of(metadataOptions[2]);
            return formatter.withLocale(field.getLocale()).withZone(zone).format((TemporalAccessor)object);
        }
        return this.resolve(object);
    }
    
    @Override
    public String resolve(@NonNull final Object object) {
        if (object == null) {
            throw new NullPointerException("object is marked non-null but is null");
        }
        if (object instanceof Enum) {
            return ((Enum)object).name();
        }
        if (object instanceof Float || object instanceof Double) {
            return String.format("%.2f", new Object[] { object });
        }
        return object.toString();
    }
    
    static {
        INSTANCE = new DefaultSchemaResolver();
        SUPPORTED_TYPES = (Set)new HashSet((Collection)Arrays.asList((Object[])new Class[] { BigDecimal.class, BigInteger.class, Boolean.class, Boolean.TYPE, Byte.class, Byte.TYPE, Character.class, Character.TYPE, Double.class, Double.TYPE, Float.class, Float.TYPE, Integer.class, Integer.TYPE, Long.class, Long.TYPE, Short.class, Short.TYPE, String.class, UUID.class, Instant.class }));
    }
}
