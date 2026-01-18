package cc.dreamcode.kowal.libs.cc.dreamcode.utilities;

import lombok.Generated;
import lombok.NonNull;
import java.util.function.Consumer;

public final class Validation
{
    public static <T> void nonNull(final T t, @NonNull final Consumer<T> consumer) {
        if (consumer == null) {
            throw new NullPointerException("consumer is marked non-null but is null");
        }
        if (t == null) {
            return;
        }
        consumer.accept((Object)t);
    }
    
    public static void isNull(final Object ob, @NonNull final Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("runnable is marked non-null but is null");
        }
        if (ob != null) {
            return;
        }
        runnable.run();
    }
    
    public static void isTrue(final boolean b, @NonNull final Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("runnable is marked non-null but is null");
        }
        if (!b) {
            return;
        }
        runnable.run();
    }
    
    public static void isFalse(final boolean b, @NonNull final Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("runnable is marked non-null but is null");
        }
        if (b) {
            return;
        }
        runnable.run();
    }
    
    @Generated
    private Validation() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
