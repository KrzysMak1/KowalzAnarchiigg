package cc.dreamcode.kowal.libs.net.kyori.adventure.pointer;

import cc.dreamcode.kowal.libs.net.kyori.adventure.util.Buildable;
import java.util.Objects;
import java.util.Optional;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;
import java.util.function.Supplier;
import java.util.Map;

final class PointersImpl implements Pointers
{
    static final Pointers EMPTY;
    private final Map<Pointer<?>, Supplier<?>> pointers;
    
    PointersImpl(@NotNull final BuilderImpl builder) {
        this.pointers = (Map<Pointer<?>, Supplier<?>>)new HashMap(builder.pointers);
    }
    
    @NotNull
    @Override
    public <T> Optional<T> get(@NotNull final Pointer<T> pointer) {
        Objects.requireNonNull((Object)pointer, "pointer");
        final Supplier<?> supplier = (Supplier<?>)this.pointers.get((Object)pointer);
        if (supplier == null) {
            return (Optional<T>)Optional.empty();
        }
        return (Optional<T>)Optional.ofNullable(supplier.get());
    }
    
    @Override
    public <T> boolean supports(@NotNull final Pointer<T> pointer) {
        Objects.requireNonNull((Object)pointer, "pointer");
        return this.pointers.containsKey((Object)pointer);
    }
    
    @Override
    public Builder toBuilder() {
        return new BuilderImpl(this);
    }
    
    static {
        EMPTY = new Pointers() {
            @NotNull
            @Override
            public <T> Optional<T> get(@NotNull final Pointer<T> pointer) {
                return (Optional<T>)Optional.empty();
            }
            
            @Override
            public <T> boolean supports(@NotNull final Pointer<T> pointer) {
                return false;
            }
            
            @Override
            public Builder toBuilder() {
                return new BuilderImpl();
            }
            
            @Override
            public String toString() {
                return "EmptyPointers";
            }
        };
    }
    
    static final class BuilderImpl implements Builder
    {
        private final Map<Pointer<?>, Supplier<?>> pointers;
        
        BuilderImpl() {
            this.pointers = (Map<Pointer<?>, Supplier<?>>)new HashMap();
        }
        
        BuilderImpl(@NotNull final PointersImpl pointers) {
            this.pointers = (Map<Pointer<?>, Supplier<?>>)new HashMap(pointers.pointers);
        }
        
        @NotNull
        @Override
        public <T> Builder withDynamic(@NotNull final Pointer<T> pointer, @NotNull final Supplier<T> value) {
            this.pointers.put((Object)Objects.requireNonNull((Object)pointer, "pointer"), (Object)Objects.requireNonNull((Object)value, "value"));
            return this;
        }
        
        @NotNull
        @Override
        public Pointers build() {
            return new PointersImpl(this);
        }
    }
}
