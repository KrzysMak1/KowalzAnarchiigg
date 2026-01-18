package cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet;

import java.io.PrintStream;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.Set;

public final class Knob
{
    private static final String NAMESPACE;
    public static final boolean DEBUG;
    private static final Set<Object> UNSUPPORTED;
    public static volatile Consumer<String> OUT;
    public static volatile BiConsumer<String, Throwable> ERR;
    
    private Knob() {
    }
    
    public static boolean isEnabled(@NotNull final String key, final boolean defaultValue) {
        return System.getProperty(Knob.NAMESPACE + "." + key, Boolean.toString(defaultValue)).equalsIgnoreCase("true");
    }
    
    public static void logError(@Nullable final Throwable error, @NotNull final String format, @NotNull final Object... arguments) {
        if (Knob.DEBUG) {
            Knob.ERR.accept((Object)String.format(format, arguments), (Object)error);
        }
    }
    
    public static void logMessage(@NotNull final String format, @NotNull final Object... arguments) {
        if (Knob.DEBUG) {
            Knob.OUT.accept((Object)String.format(format, arguments));
        }
    }
    
    public static void logUnsupported(@NotNull final Object facet, @NotNull final Object value) {
        if (Knob.DEBUG && Knob.UNSUPPORTED.add(value)) {
            Knob.OUT.accept((Object)String.format("Unsupported value '%s' for facet: %s", new Object[] { value, facet }));
        }
    }
    
    static {
        NAMESPACE = "net.kyo".concat("ri.adventure");
        DEBUG = isEnabled("debug", false);
        UNSUPPORTED = (Set)new CopyOnWriteArraySet();
        final PrintStream out = System.out;
        Objects.requireNonNull((Object)out);
        Knob.OUT = (Consumer<String>)out::println;
        Knob.ERR = (BiConsumer<String, Throwable>)((message, err) -> {
            System.err.println(message);
            if (err != null) {
                err.printStackTrace(System.err);
            }
        });
    }
}
