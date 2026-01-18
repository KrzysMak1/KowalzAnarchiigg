package cc.dreamcode.kowal.libs.net.kyori.option;

import java.util.function.Consumer;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Collections;
import java.util.SortedMap;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import java.util.Map;
import java.util.IdentityHashMap;

final class OptionStateImpl implements OptionState
{
    static final OptionState EMPTY;
    private final IdentityHashMap<Option<?>, Object> values;
    
    OptionStateImpl(final IdentityHashMap<Option<?>, Object> values) {
        this.values = (IdentityHashMap<Option<?>, Object>)new IdentityHashMap((Map)values);
    }
    
    @Override
    public boolean has(@NotNull final Option<?> option) {
        return this.values.containsKey(Objects.requireNonNull((Object)option, "flag"));
    }
    
    @Override
    public <V> V value(@NotNull final Option<V> option) {
        final V value = option.type().cast(this.values.get(Objects.requireNonNull((Object)option, "flag")));
        return (value == null) ? option.defaultValue() : value;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final OptionStateImpl that = (OptionStateImpl)other;
        return Objects.equals((Object)this.values, (Object)that.values);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(new Object[] { this.values });
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{values=" + (Object)this.values + '}';
    }
    
    static {
        EMPTY = new OptionStateImpl((IdentityHashMap<Option<?>, Object>)new IdentityHashMap());
    }
    
    static final class VersionedImpl implements Versioned
    {
        private final SortedMap<Integer, OptionState> sets;
        private final int targetVersion;
        private final OptionState filtered;
        
        VersionedImpl(final SortedMap<Integer, OptionState> sets, final int targetVersion, final OptionState filtered) {
            this.sets = sets;
            this.targetVersion = targetVersion;
            this.filtered = filtered;
        }
        
        @Override
        public boolean has(@NotNull final Option<?> option) {
            return this.filtered.has(option);
        }
        
        @Override
        public <V> V value(@NotNull final Option<V> option) {
            return this.filtered.value(option);
        }
        
        @NotNull
        @Override
        public Map<Integer, OptionState> childStates() {
            return (Map<Integer, OptionState>)Collections.unmodifiableSortedMap(this.sets.headMap((Object)(this.targetVersion + 1)));
        }
        
        @NotNull
        @Override
        public Versioned at(final int version) {
            return new VersionedImpl(this.sets, version, flattened(this.sets, version));
        }
        
        public static OptionState flattened(final SortedMap<Integer, OptionState> versions, final int targetVersion) {
            final Map<Integer, OptionState> applicable = (Map<Integer, OptionState>)versions.headMap((Object)(targetVersion + 1));
            final Builder builder = OptionState.optionState();
            for (final OptionState child : applicable.values()) {
                builder.values(child);
            }
            return builder.build();
        }
        
        @Override
        public boolean equals(@Nullable final Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || this.getClass() != other.getClass()) {
                return false;
            }
            final VersionedImpl that = (VersionedImpl)other;
            return this.targetVersion == that.targetVersion && Objects.equals((Object)this.sets, (Object)that.sets) && Objects.equals((Object)this.filtered, (Object)that.filtered);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(new Object[] { this.sets, this.targetVersion, this.filtered });
        }
        
        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "{sets=" + (Object)this.sets + ", targetVersion=" + this.targetVersion + ", filtered=" + (Object)this.filtered + '}';
        }
    }
    
    static final class BuilderImpl implements Builder
    {
        private final IdentityHashMap<Option<?>, Object> values;
        
        BuilderImpl() {
            this.values = (IdentityHashMap<Option<?>, Object>)new IdentityHashMap();
        }
        
        @NotNull
        @Override
        public OptionState build() {
            if (this.values.isEmpty()) {
                return OptionStateImpl.EMPTY;
            }
            return new OptionStateImpl(this.values);
        }
        
        @NotNull
        @Override
        public <V> Builder value(@NotNull final Option<V> option, @NotNull final V value) {
            this.values.put((Object)Objects.requireNonNull((Object)option, "flag"), Objects.requireNonNull((Object)value, "value"));
            return this;
        }
        
        @NotNull
        @Override
        public Builder values(@NotNull final OptionState existing) {
            if (existing instanceof OptionStateImpl) {
                this.values.putAll((Map)((OptionStateImpl)existing).values);
            }
            else {
                if (!(existing instanceof VersionedImpl)) {
                    throw new IllegalArgumentException("existing set " + (Object)existing + " is of an unknown implementation type");
                }
                this.values.putAll((Map)((OptionStateImpl)((VersionedImpl)existing).filtered).values);
            }
            return this;
        }
    }
    
    static final class VersionedBuilderImpl implements VersionedBuilder
    {
        private final Map<Integer, BuilderImpl> builders;
        
        VersionedBuilderImpl() {
            this.builders = (Map<Integer, BuilderImpl>)new TreeMap();
        }
        
        @Override
        public Versioned build() {
            if (this.builders.isEmpty()) {
                return new VersionedImpl((SortedMap<Integer, OptionState>)Collections.emptySortedMap(), 0, OptionState.emptyOptionState());
            }
            final SortedMap<Integer, OptionState> built = (SortedMap<Integer, OptionState>)new TreeMap();
            for (final Map.Entry<Integer, BuilderImpl> entry : this.builders.entrySet()) {
                built.put((Object)entry.getKey(), (Object)((BuilderImpl)entry.getValue()).build());
            }
            return new VersionedImpl(built, (int)built.lastKey(), VersionedImpl.flattened(built, (int)built.lastKey()));
        }
        
        @NotNull
        @Override
        public VersionedBuilder version(final int version, @NotNull final Consumer<Builder> versionBuilder) {
            ((Consumer)Objects.requireNonNull((Object)versionBuilder, "versionBuilder")).accept((Object)this.builders.computeIfAbsent((Object)version, $ -> new BuilderImpl()));
            return this;
        }
    }
}
