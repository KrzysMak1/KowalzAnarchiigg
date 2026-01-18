package cc.dreamcode.kowal.libs.eu.okaeri.pluralize;

import java.util.HashMap;
import java.util.Optional;
import java.util.Locale;
import lombok.NonNull;
import java.util.function.Function;
import java.util.Map;

public final class Pluralize
{
    private static final Map<String, Function<Integer, Integer>> PLURALIZERS;
    private static final Map<String, Integer> PLURALS;
    
    private static void add(@NonNull final String isoLocale, final int nplurals, @NonNull final Function<Integer, Integer> pluralizer) {
        if (isoLocale == null) {
            throw new NullPointerException("isoLocale is marked non-null but is null");
        }
        if (pluralizer == null) {
            throw new NullPointerException("pluralizer is marked non-null but is null");
        }
        Pluralize.PLURALIZERS.put((Object)isoLocale, (Object)pluralizer);
        Pluralize.PLURALS.put((Object)isoLocale, (Object)nplurals);
    }
    
    public static Optional<Function<Integer, Integer>> getPluralizer(@NonNull final Locale locale) {
        if (locale == null) {
            throw new NullPointerException("locale is marked non-null but is null");
        }
        final String lang = locale.getLanguage();
        final String country = locale.getCountry();
        if (!lang.isEmpty() && !country.isEmpty()) {
            final Function<Integer, Integer> pluralizer = (Function<Integer, Integer>)Pluralize.PLURALIZERS.get((Object)(lang + "_" + country));
            if (pluralizer != null) {
                return (Optional<Function<Integer, Integer>>)Optional.of((Object)pluralizer);
            }
        }
        final Function<Integer, Integer> pluralizer = (Function<Integer, Integer>)Pluralize.PLURALIZERS.get((Object)lang);
        return (Optional<Function<Integer, Integer>>)Optional.ofNullable((Object)pluralizer);
    }
    
    @Deprecated
    public static Optional<Function<Integer, Integer>> getPluralizer(@NonNull final String isoLocale) {
        if (isoLocale == null) {
            throw new NullPointerException("isoLocale is marked non-null but is null");
        }
        return (Optional<Function<Integer, Integer>>)Optional.ofNullable((Object)Pluralize.PLURALIZERS.get((Object)isoLocale));
    }
    
    public static String pluralize(@NonNull final Locale locale, final int n, @NonNull final String... plurals) {
        if (locale == null) {
            throw new NullPointerException("locale is marked non-null but is null");
        }
        if (plurals == null) {
            throw new NullPointerException("plurals is marked non-null but is null");
        }
        final Optional<Function<Integer, Integer>> pluralizer = getPluralizer(locale);
        if (!pluralizer.isPresent()) {
            throw new IllegalArgumentException("no pluralizer for locale: " + (Object)locale);
        }
        final int form = (int)((Function)pluralizer.get()).apply((Object)n);
        if (form > plurals.length) {
            throw new IllegalArgumentException("not enough plurals provided: " + form + ">" + plurals.length);
        }
        return plurals[form];
    }
    
    @Deprecated
    public static String pluralize(@NonNull final String isoLocale, final int n, @NonNull final String... plurals) {
        if (isoLocale == null) {
            throw new NullPointerException("isoLocale is marked non-null but is null");
        }
        if (plurals == null) {
            throw new NullPointerException("plurals is marked non-null but is null");
        }
        final Optional<Function<Integer, Integer>> pluralizer = getPluralizer(isoLocale);
        if (!pluralizer.isPresent()) {
            throw new IllegalArgumentException("no pluralizer for locale: " + isoLocale);
        }
        final int form = (int)((Function)pluralizer.get()).apply((Object)n);
        if (form > plurals.length) {
            throw new IllegalArgumentException("not enough plurals provided: " + form + ">" + plurals.length);
        }
        return plurals[form];
    }
    
    public static int plurals(@NonNull final Locale locale) {
        if (locale == null) {
            throw new NullPointerException("locale is marked non-null but is null");
        }
        final String lang = locale.getLanguage();
        final String country = locale.getCountry();
        if (!lang.isEmpty() && !country.isEmpty()) {
            final Integer plurals = (Integer)Pluralize.PLURALS.get((Object)(lang + "_" + country));
            if (plurals != null) {
                return plurals;
            }
        }
        final Integer plurals = (Integer)Pluralize.PLURALS.get((Object)lang);
        if (plurals == null) {
            throw new IllegalArgumentException("no plurals info for locale: " + (Object)locale);
        }
        return plurals;
    }
    
    @Deprecated
    public static int plurals(@NonNull final String isoLocale) {
        if (isoLocale == null) {
            throw new NullPointerException("isoLocale is marked non-null but is null");
        }
        final Integer plurals = (Integer)Pluralize.PLURALS.get((Object)isoLocale);
        if (plurals == null) {
            throw new IllegalArgumentException("no plurals info for locale: " + isoLocale);
        }
        return plurals;
    }
    
    static {
        PLURALIZERS = (Map)new HashMap();
        PLURALS = (Map)new HashMap();
        add("ach", 2, (Function<Integer, Integer>)(n -> (int)((n > 1) ? 1 : 0)));
        add("af", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("ak", 2, (Function<Integer, Integer>)(n -> (int)((n > 1) ? 1 : 0)));
        add("am", 2, (Function<Integer, Integer>)(n -> (int)((n > 1) ? 1 : 0)));
        add("an", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("anp", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("ar", 6, (Function<Integer, Integer>)(n -> (n == 0) ? 0 : ((n == 1) ? 1 : ((n == 2) ? 2 : ((n % 100 >= 3 && n % 100 <= 10) ? 3 : ((n % 100 >= 11) ? 4 : 5))))));
        add("arn", 2, (Function<Integer, Integer>)(n -> (int)((n > 1) ? 1 : 0)));
        add("as", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("ast", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("ay", 1, (Function<Integer, Integer>)(n -> 0));
        add("az", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("be", 3, (Function<Integer, Integer>)(n -> (n % 10 == 1 && n % 100 != 11) ? 0 : ((n % 10 >= 2 && n % 10 <= 4 && (n % 100 < 10 || n % 100 >= 20)) ? 1 : 2)));
        add("bg", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("bn", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("bo", 1, (Function<Integer, Integer>)(n -> 0));
        add("br", 2, (Function<Integer, Integer>)(n -> (int)((n > 1) ? 1 : 0)));
        add("brx", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("bs", 3, (Function<Integer, Integer>)(n -> (n % 10 == 1 && n % 100 != 11) ? 0 : ((n % 10 >= 2 && n % 10 <= 4 && (n % 100 < 10 || n % 100 >= 20)) ? 1 : 2)));
        add("ca", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("cgg", 1, (Function<Integer, Integer>)(n -> 0));
        add("cs", 3, (Function<Integer, Integer>)(n -> (n == 1) ? 0 : ((n >= 2 && n <= 4) ? 1 : 2)));
        add("csb", 3, (Function<Integer, Integer>)(n -> (n == 1) ? 0 : ((n % 10 >= 2 && n % 10 <= 4 && (n % 100 < 10 || n % 100 >= 20)) ? 1 : 2)));
        add("cy", 4, (Function<Integer, Integer>)(n -> (n == 1) ? 0 : ((n == 2) ? 1 : ((n != 8 && n != 11) ? 2 : 3))));
        add("da", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("de", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("doi", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("dz", 1, (Function<Integer, Integer>)(n -> 0));
        add("el", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("en", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("eo", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("es", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("es_AR", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("et", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("eu", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("fa", 2, (Function<Integer, Integer>)(n -> (int)((n > 1) ? 1 : 0)));
        add("ff", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("fi", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("fil", 2, (Function<Integer, Integer>)(n -> (int)((n > 1) ? 1 : 0)));
        add("fo", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("fr", 2, (Function<Integer, Integer>)(n -> (int)((n > 1) ? 1 : 0)));
        add("fur", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("fy", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("ga", 5, (Function<Integer, Integer>)(n -> (n == 1) ? 0 : ((n == 2) ? 1 : ((n > 2 && n < 7) ? 2 : ((n > 6 && n < 11) ? 3 : 4)))));
        add("gd", 4, (Function<Integer, Integer>)(n -> (n == 1 || n == 11) ? 0 : ((n == 2 || n == 12) ? 1 : ((n > 2 && n < 20) ? 2 : 3))));
        add("gl", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("gu", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("gun", 2, (Function<Integer, Integer>)(n -> (int)((n > 1) ? 1 : 0)));
        add("ha", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("he", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("hi", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("hne", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("hr", 3, (Function<Integer, Integer>)(n -> (n % 10 == 1 && n % 100 != 11) ? 0 : ((n % 10 >= 2 && n % 10 <= 4 && (n % 100 < 10 || n % 100 >= 20)) ? 1 : 2)));
        add("hu", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("hy", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("ia", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("id", 1, (Function<Integer, Integer>)(n -> 0));
        add("is", 2, (Function<Integer, Integer>)(n -> (int)((n % 10 != 1 || n % 100 == 11) ? 1 : 0)));
        add("it", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("ja", 1, (Function<Integer, Integer>)(n -> 0));
        add("jbo", 1, (Function<Integer, Integer>)(n -> 0));
        add("jv", 2, (Function<Integer, Integer>)(n -> (int)((n != 0) ? 1 : 0)));
        add("ka", 1, (Function<Integer, Integer>)(n -> 0));
        add("kk", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("kl", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("km", 1, (Function<Integer, Integer>)(n -> 0));
        add("kn", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("ko", 1, (Function<Integer, Integer>)(n -> 0));
        add("ku", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("kw", 4, (Function<Integer, Integer>)(n -> (n == 1) ? 0 : ((n == 2) ? 1 : ((n == 3) ? 2 : 3))));
        add("ky", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("lb", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("ln", 2, (Function<Integer, Integer>)(n -> (int)((n > 1) ? 1 : 0)));
        add("lo", 1, (Function<Integer, Integer>)(n -> 0));
        add("lt", 3, (Function<Integer, Integer>)(n -> (n % 10 == 1 && n % 100 != 11) ? 0 : ((n % 10 >= 2 && (n % 100 < 10 || n % 100 >= 20)) ? 1 : 2)));
        add("lv", 3, (Function<Integer, Integer>)(n -> (n % 10 == 1 && n % 100 != 11) ? 0 : ((n != 0) ? 1 : 2)));
        add("mai", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("me", 3, (Function<Integer, Integer>)(n -> (n % 10 == 1 && n % 100 != 11) ? 0 : ((n % 10 >= 2 && n % 10 <= 4 && (n % 100 < 10 || n % 100 >= 20)) ? 1 : 2)));
        add("mfe", 2, (Function<Integer, Integer>)(n -> (int)((n > 1) ? 1 : 0)));
        add("mg", 2, (Function<Integer, Integer>)(n -> (int)((n > 1) ? 1 : 0)));
        add("mi", 2, (Function<Integer, Integer>)(n -> (int)((n > 1) ? 1 : 0)));
        add("ml", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("mn", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("mni", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("mnk", 3, (Function<Integer, Integer>)(n -> (n == 0) ? 0 : ((n == 1) ? 1 : 2)));
        add("mr", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("ms", 1, (Function<Integer, Integer>)(n -> 0));
        add("mt", 4, (Function<Integer, Integer>)(n -> (n == 1) ? 0 : ((n == 0 || (n % 100 > 1 && n % 100 < 11)) ? 1 : ((n % 100 > 10 && n % 100 < 20) ? 2 : 3))));
        add("my", 1, (Function<Integer, Integer>)(n -> 0));
        add("nah", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("nap", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("nb", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("ne", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("nl", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("nn", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("no", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("nso", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("oc", 2, (Function<Integer, Integer>)(n -> (int)((n > 1) ? 1 : 0)));
        add("or", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("pa", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("pap", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("pl", 3, (Function<Integer, Integer>)(n -> (n == 1) ? 0 : ((n % 10 >= 2 && n % 10 <= 4 && (n % 100 < 10 || n % 100 >= 20)) ? 1 : 2)));
        add("pms", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("ps", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("pt", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("pt_BR", 2, (Function<Integer, Integer>)(n -> (int)((n > 1) ? 1 : 0)));
        add("rm", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("ro", 3, (Function<Integer, Integer>)(n -> (n == 1) ? 0 : ((n == 0 || (n % 100 > 0 && n % 100 < 20)) ? 1 : 2)));
        add("ru", 3, (Function<Integer, Integer>)(n -> (n % 10 == 1 && n % 100 != 11) ? 0 : ((n % 10 >= 2 && n % 10 <= 4 && (n % 100 < 10 || n % 100 >= 20)) ? 1 : 2)));
        add("rw", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("sah", 1, (Function<Integer, Integer>)(n -> 0));
        add("sat", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("sco", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("sd", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("se", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("si", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("sk", 3, (Function<Integer, Integer>)(n -> (n == 1) ? 0 : ((n >= 2 && n <= 4) ? 1 : 2)));
        add("sl", 4, (Function<Integer, Integer>)(n -> (n % 100 == 1) ? 0 : ((n % 100 == 2) ? 1 : ((n % 100 == 3 || n % 100 == 4) ? 2 : 3))));
        add("so", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("son", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("sq", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("sr", 3, (Function<Integer, Integer>)(n -> (n % 10 == 1 && n % 100 != 11) ? 0 : ((n % 10 >= 2 && n % 10 <= 4 && (n % 100 < 10 || n % 100 >= 20)) ? 1 : 2)));
        add("su", 1, (Function<Integer, Integer>)(n -> 0));
        add("sv", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("sw", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("ta", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("te", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("tg", 2, (Function<Integer, Integer>)(n -> (int)((n > 1) ? 1 : 0)));
        add("th", 1, (Function<Integer, Integer>)(n -> 0));
        add("ti", 2, (Function<Integer, Integer>)(n -> (int)((n > 1) ? 1 : 0)));
        add("tk", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("tr", 2, (Function<Integer, Integer>)(n -> (int)((n > 1) ? 1 : 0)));
        add("tt", 1, (Function<Integer, Integer>)(n -> 0));
        add("ug", 1, (Function<Integer, Integer>)(n -> 0));
        add("uk", 3, (Function<Integer, Integer>)(n -> (n % 10 == 1 && n % 100 != 11) ? 0 : ((n % 10 >= 2 && n % 10 <= 4 && (n % 100 < 10 || n % 100 >= 20)) ? 1 : 2)));
        add("ur", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("uz", 2, (Function<Integer, Integer>)(n -> (int)((n > 1) ? 1 : 0)));
        add("vi", 1, (Function<Integer, Integer>)(n -> 0));
        add("wa", 2, (Function<Integer, Integer>)(n -> (int)((n > 1) ? 1 : 0)));
        add("wo", 1, (Function<Integer, Integer>)(n -> 0));
        add("yo", 2, (Function<Integer, Integer>)(n -> (int)((n != 1) ? 1 : 0)));
        add("zh", 1, (Function<Integer, Integer>)(n -> 0));
    }
}
