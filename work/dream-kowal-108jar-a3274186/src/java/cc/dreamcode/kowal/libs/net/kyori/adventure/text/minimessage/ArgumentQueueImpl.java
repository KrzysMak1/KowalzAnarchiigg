package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage;

import org.jetbrains.annotations.Nullable;
import java.util.function.Supplier;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;

final class ArgumentQueueImpl<T extends Tag.Argument> implements ArgumentQueue
{
    private final Context context;
    final List<T> args;
    private int ptr;
    
    ArgumentQueueImpl(final Context context, final List<T> args) {
        this.ptr = 0;
        this.context = context;
        this.args = args;
    }
    
    @NotNull
    @Override
    public T pop() {
        if (!this.hasNext()) {
            throw this.context.newException("Missing argument for this tag!", this);
        }
        return (T)this.args.get(this.ptr++);
    }
    
    @NotNull
    @Override
    public T popOr(@NotNull final String errorMessage) {
        Objects.requireNonNull((Object)errorMessage, "errorMessage");
        if (!this.hasNext()) {
            throw this.context.newException(errorMessage, this);
        }
        return (T)this.args.get(this.ptr++);
    }
    
    @NotNull
    @Override
    public T popOr(@NotNull final Supplier<String> errorMessage) {
        Objects.requireNonNull((Object)errorMessage, "errorMessage");
        if (!this.hasNext()) {
            throw this.context.newException((String)Objects.requireNonNull((Object)errorMessage.get(), "errorMessage.get()"), this);
        }
        return (T)this.args.get(this.ptr++);
    }
    
    @Nullable
    @Override
    public T peek() {
        return (T)(this.hasNext() ? ((Tag.Argument)this.args.get(this.ptr)) : null);
    }
    
    @Override
    public boolean hasNext() {
        return this.ptr < this.args.size();
    }
    
    @Override
    public void reset() {
        this.ptr = 0;
    }
    
    @Override
    public String toString() {
        return this.args.toString();
    }
}
