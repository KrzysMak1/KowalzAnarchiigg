package cc.dreamcode.kowal.libs.net.kyori.adventure.text;

import cc.dreamcode.kowal.libs.net.kyori.adventure.util.Buildable;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.internal.Internals;
import java.util.Objects;
import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;

final class StorageNBTComponentImpl extends NBTComponentImpl<StorageNBTComponent, StorageNBTComponent.Builder> implements StorageNBTComponent
{
    private final Key storage;
    
    @NotNull
    static StorageNBTComponent create(@NotNull final List<? extends ComponentLike> children, @NotNull final Style style, final String nbtPath, final boolean interpret, @Nullable final ComponentLike separator, @NotNull final Key storage) {
        return new StorageNBTComponentImpl(ComponentLike.asComponents(children, (Predicate<? super Component>)StorageNBTComponentImpl.IS_NOT_EMPTY), (Style)Objects.requireNonNull((Object)style, "style"), (String)Objects.requireNonNull((Object)nbtPath, "nbtPath"), interpret, ComponentLike.unbox(separator), (Key)Objects.requireNonNull((Object)storage, "storage"));
    }
    
    StorageNBTComponentImpl(@NotNull final List<Component> children, @NotNull final Style style, final String nbtPath, final boolean interpret, @Nullable final Component separator, final Key storage) {
        super(children, style, nbtPath, interpret, separator);
        this.storage = storage;
    }
    
    @NotNull
    @Override
    public StorageNBTComponent nbtPath(@NotNull final String nbtPath) {
        if (Objects.equals((Object)this.nbtPath, (Object)nbtPath)) {
            return this;
        }
        return create(this.children, this.style, nbtPath, this.interpret, this.separator, this.storage);
    }
    
    @NotNull
    @Override
    public StorageNBTComponent interpret(final boolean interpret) {
        if (this.interpret == interpret) {
            return this;
        }
        return create(this.children, this.style, this.nbtPath, interpret, this.separator, this.storage);
    }
    
    @Nullable
    @Override
    public Component separator() {
        return this.separator;
    }
    
    @NotNull
    @Override
    public StorageNBTComponent separator(@Nullable final ComponentLike separator) {
        return create(this.children, this.style, this.nbtPath, this.interpret, separator, this.storage);
    }
    
    @NotNull
    @Override
    public Key storage() {
        return this.storage;
    }
    
    @NotNull
    @Override
    public StorageNBTComponent storage(@NotNull final Key storage) {
        if (Objects.equals((Object)this.storage, (Object)storage)) {
            return this;
        }
        return create(this.children, this.style, this.nbtPath, this.interpret, this.separator, storage);
    }
    
    @NotNull
    @Override
    public StorageNBTComponent children(@NotNull final List<? extends ComponentLike> children) {
        return create(children, this.style, this.nbtPath, this.interpret, this.separator, this.storage);
    }
    
    @NotNull
    @Override
    public StorageNBTComponent style(@NotNull final Style style) {
        return create(this.children, style, this.nbtPath, this.interpret, this.separator, this.storage);
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof StorageNBTComponent)) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        final StorageNBTComponentImpl that = (StorageNBTComponentImpl)other;
        return Objects.equals((Object)this.storage, (Object)that.storage());
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.storage.hashCode();
        return result;
    }
    
    @Override
    public String toString() {
        return Internals.toString(this);
    }
    
    @Override
    public StorageNBTComponent.Builder toBuilder() {
        return new BuilderImpl(this);
    }
    
    static class BuilderImpl extends AbstractNBTComponentBuilder<StorageNBTComponent, StorageNBTComponent.Builder> implements StorageNBTComponent.Builder
    {
        @Nullable
        private Key storage;
        
        BuilderImpl() {
        }
        
        BuilderImpl(@NotNull final StorageNBTComponent component) {
            super(component);
            this.storage = component.storage();
        }
        
        @Override
        public StorageNBTComponent.Builder storage(@NotNull final Key storage) {
            this.storage = (Key)Objects.requireNonNull((Object)storage, "storage");
            return this;
        }
        
        @NotNull
        @Override
        public StorageNBTComponent build() {
            if (this.nbtPath == null) {
                throw new IllegalStateException("nbt path must be set");
            }
            if (this.storage == null) {
                throw new IllegalStateException("storage must be set");
            }
            return StorageNBTComponentImpl.create(this.children, this.buildStyle(), this.nbtPath, this.interpret, this.separator, this.storage);
        }
    }
}
