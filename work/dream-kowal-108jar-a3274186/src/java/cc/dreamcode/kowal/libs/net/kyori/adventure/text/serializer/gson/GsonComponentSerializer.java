package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.gson;

import org.jetbrains.annotations.ApiStatus;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.PlatformAPI;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.json.JSONOptions;
import org.jetbrains.annotations.Nullable;
import java.util.function.Consumer;
import cc.dreamcode.kowal.libs.net.kyori.option.OptionState;
import cc.dreamcode.kowal.libs.net.kyori.adventure.builder.AbstractBuilder;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import com.google.gson.JsonElement;
import com.google.gson.GsonBuilder;
import java.util.function.UnaryOperator;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.Buildable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.json.JSONComponentSerializer;

public interface GsonComponentSerializer extends JSONComponentSerializer, Buildable<GsonComponentSerializer, GsonComponentSerializer.Builder>
{
    @NotNull
    default GsonComponentSerializer gson() {
        return GsonComponentSerializerImpl.Instances.INSTANCE;
    }
    
    @NotNull
    default GsonComponentSerializer colorDownsamplingGson() {
        return GsonComponentSerializerImpl.Instances.LEGACY_INSTANCE;
    }
    
    default Builder builder() {
        return new GsonComponentSerializerImpl.BuilderImpl();
    }
    
    @NotNull
    Gson serializer();
    
    @NotNull
    UnaryOperator<GsonBuilder> populator();
    
    @NotNull
    Component deserializeFromTree(@NotNull final JsonElement input);
    
    @NotNull
    JsonElement serializeToTree(@NotNull final Component component);
    
    public interface Builder extends AbstractBuilder<GsonComponentSerializer>, Buildable.Builder<GsonComponentSerializer>, JSONComponentSerializer.Builder
    {
        @NotNull
        Builder options(@NotNull final OptionState flags);
        
        @NotNull
        Builder editOptions(@NotNull final Consumer<OptionState.Builder> optionEditor);
        
        @NotNull
        default Builder downsampleColors() {
            return this.editOptions((Consumer<OptionState.Builder>)(features -> features.value(JSONOptions.EMIT_RGB, false)));
        }
        
        @Deprecated
        @NotNull
        default Builder legacyHoverEventSerializer(@Nullable final LegacyHoverEventSerializer serializer) {
            return this.legacyHoverEventSerializer((cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer)serializer);
        }
        
        @NotNull
        Builder legacyHoverEventSerializer(final cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer serializer);
        
        @Deprecated
        @NotNull
        default Builder emitLegacyHoverEvent() {
            return this.editOptions((Consumer<OptionState.Builder>)(b -> b.value(JSONOptions.EMIT_HOVER_EVENT_TYPE, JSONOptions.HoverEventValueMode.BOTH)));
        }
        
        @NotNull
        GsonComponentSerializer build();
    }
    
    @PlatformAPI
    @ApiStatus.Internal
    public interface Provider
    {
        @PlatformAPI
        @ApiStatus.Internal
        @NotNull
        GsonComponentSerializer gson();
        
        @PlatformAPI
        @ApiStatus.Internal
        @NotNull
        GsonComponentSerializer gsonLegacy();
        
        @PlatformAPI
        @ApiStatus.Internal
        @NotNull
        Consumer<Builder> builder();
    }
}
