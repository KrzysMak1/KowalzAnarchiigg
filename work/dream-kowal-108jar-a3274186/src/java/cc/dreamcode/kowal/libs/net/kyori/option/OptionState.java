package cc.dreamcode.kowal.libs.net.kyori.option;

import java.util.function.Consumer;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface OptionState
{
    default OptionState emptyOptionState() {
        return OptionStateImpl.EMPTY;
    }
    
    @NotNull
    default Builder optionState() {
        return new OptionStateImpl.BuilderImpl();
    }
    
    @NotNull
    default VersionedBuilder versionedOptionState() {
        return new OptionStateImpl.VersionedBuilderImpl();
    }
    
    boolean has(@NotNull final Option<?> option);
    
     <V> V value(@NotNull final Option<V> option);
    
    @ApiStatus.NonExtendable
    public interface Versioned extends OptionState
    {
        @NotNull
        Map<Integer, OptionState> childStates();
        
        @NotNull
        Versioned at(final int version);
    }
    
    @ApiStatus.NonExtendable
    public interface VersionedBuilder
    {
        @NotNull
        VersionedBuilder version(final int version, @NotNull final Consumer<Builder> versionBuilder);
        
        @NotNull
        Versioned build();
    }
    
    @ApiStatus.NonExtendable
    public interface Builder
    {
        @NotNull
         <V> Builder value(@NotNull final Option<V> option, @NotNull final V value);
        
        @NotNull
        Builder values(@NotNull final OptionState existing);
        
        @NotNull
        OptionState build();
    }
}
