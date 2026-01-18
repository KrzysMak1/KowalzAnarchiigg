package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.json;

import org.jetbrains.annotations.Nullable;
import java.util.function.Consumer;
import cc.dreamcode.kowal.libs.net.kyori.option.OptionState;
import java.util.function.Supplier;
import org.jetbrains.annotations.ApiStatus;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.PlatformAPI;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.ComponentSerializer;

public interface JSONComponentSerializer extends ComponentSerializer<Component, Component, String>
{
    @NotNull
    default JSONComponentSerializer json() {
        return JSONComponentSerializerAccessor.Instances.INSTANCE;
    }
    
    default Builder builder() {
        return (Builder)JSONComponentSerializerAccessor.Instances.BUILDER_SUPPLIER.get();
    }
    
    @PlatformAPI
    @ApiStatus.Internal
    public interface Provider
    {
        @PlatformAPI
        @ApiStatus.Internal
        @NotNull
        JSONComponentSerializer instance();
        
        @PlatformAPI
        @ApiStatus.Internal
        @NotNull
        Supplier<Builder> builder();
    }
    
    public interface Builder
    {
        @NotNull
        Builder options(@NotNull final OptionState flags);
        
        @NotNull
        Builder editOptions(@NotNull final Consumer<OptionState.Builder> optionEditor);
        
        @Deprecated
        @NotNull
        Builder downsampleColors();
        
        @NotNull
        Builder legacyHoverEventSerializer(@Nullable final LegacyHoverEventSerializer serializer);
        
        @Deprecated
        @NotNull
        Builder emitLegacyHoverEvent();
        
        @NotNull
        JSONComponentSerializer build();
    }
}
