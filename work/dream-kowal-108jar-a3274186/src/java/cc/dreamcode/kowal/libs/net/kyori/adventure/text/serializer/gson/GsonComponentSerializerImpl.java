package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.gson;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import java.util.Objects;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.json.JSONOptions;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.Services;
import com.google.gson.TypeAdapterFactory;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.Buildable;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.option.OptionState;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer;
import com.google.gson.GsonBuilder;
import java.util.function.UnaryOperator;
import com.google.gson.Gson;
import java.util.function.Consumer;
import java.util.Optional;

final class GsonComponentSerializerImpl implements GsonComponentSerializer
{
    private static final Optional<Provider> SERVICE;
    static final Consumer<Builder> BUILDER;
    private final Gson serializer;
    private final UnaryOperator<GsonBuilder> populator;
    private final LegacyHoverEventSerializer legacyHoverSerializer;
    private final OptionState flags;
    
    GsonComponentSerializerImpl(final OptionState flags, final LegacyHoverEventSerializer legacyHoverSerializer) {
        this.flags = flags;
        this.legacyHoverSerializer = legacyHoverSerializer;
        this.populator = (UnaryOperator<GsonBuilder>)(builder -> {
            builder.registerTypeAdapterFactory((TypeAdapterFactory)new SerializerFactory(flags, legacyHoverSerializer));
            return builder;
        });
        this.serializer = ((GsonBuilder)this.populator.apply((Object)new GsonBuilder().disableHtmlEscaping())).create();
    }
    
    @NotNull
    @Override
    public Gson serializer() {
        return this.serializer;
    }
    
    @NotNull
    @Override
    public UnaryOperator<GsonBuilder> populator() {
        return this.populator;
    }
    
    @NotNull
    @Override
    public Component deserialize(@NotNull final String string) {
        final Component component = (Component)this.serializer().fromJson(string, (Class)Component.class);
        if (component == null) {
            throw ComponentSerializerImpl.notSureHowToDeserialize(string);
        }
        return component;
    }
    
    @Nullable
    @Override
    public Component deserializeOr(@Nullable final String input, @Nullable final Component fallback) {
        if (input == null) {
            return fallback;
        }
        final Component component = (Component)this.serializer().fromJson(input, (Class)Component.class);
        if (component == null) {
            return fallback;
        }
        return component;
    }
    
    @NotNull
    @Override
    public String serialize(@NotNull final Component component) {
        return this.serializer().toJson((Object)component);
    }
    
    @NotNull
    @Override
    public Component deserializeFromTree(@NotNull final JsonElement input) {
        final Component component = (Component)this.serializer().fromJson(input, (Class)Component.class);
        if (component == null) {
            throw ComponentSerializerImpl.notSureHowToDeserialize(input);
        }
        return component;
    }
    
    @NotNull
    @Override
    public JsonElement serializeToTree(@NotNull final Component component) {
        return this.serializer().toJsonTree((Object)component);
    }
    
    @NotNull
    @Override
    public Builder toBuilder() {
        return new BuilderImpl(this);
    }
    
    static {
        SERVICE = Services.service(Provider.class);
        BUILDER = (Consumer)GsonComponentSerializerImpl.SERVICE.map(Provider::builder).orElseGet(() -> builder -> {});
    }
    
    static final class Instances
    {
        static final GsonComponentSerializer INSTANCE;
        static final GsonComponentSerializer LEGACY_INSTANCE;
        
        static {
            INSTANCE = (GsonComponentSerializer)GsonComponentSerializerImpl.SERVICE.map(Provider::gson).orElseGet(() -> new GsonComponentSerializerImpl(JSONOptions.byDataVersion(), null));
            LEGACY_INSTANCE = (GsonComponentSerializer)GsonComponentSerializerImpl.SERVICE.map(Provider::gsonLegacy).orElseGet(() -> new GsonComponentSerializerImpl(JSONOptions.byDataVersion().at(2525), null));
        }
    }
    
    static final class BuilderImpl implements Builder
    {
        private OptionState flags;
        private LegacyHoverEventSerializer legacyHoverSerializer;
        
        BuilderImpl() {
            this.flags = JSONOptions.byDataVersion();
            GsonComponentSerializerImpl.BUILDER.accept((Object)this);
        }
        
        BuilderImpl(final GsonComponentSerializerImpl serializer) {
            this();
            this.flags = serializer.flags;
            this.legacyHoverSerializer = serializer.legacyHoverSerializer;
        }
        
        @NotNull
        @Override
        public Builder options(@NotNull final OptionState flags) {
            this.flags = (OptionState)Objects.requireNonNull((Object)flags, "flags");
            return this;
        }
        
        @NotNull
        @Override
        public Builder editOptions(@NotNull final Consumer<OptionState.Builder> optionEditor) {
            final OptionState.Builder builder = OptionState.optionState().values(this.flags);
            ((Consumer)Objects.requireNonNull((Object)optionEditor, "flagEditor")).accept((Object)builder);
            this.flags = builder.build();
            return this;
        }
        
        @NotNull
        @Override
        public Builder legacyHoverEventSerializer(final LegacyHoverEventSerializer serializer) {
            this.legacyHoverSerializer = serializer;
            return this;
        }
        
        @NotNull
        @Override
        public GsonComponentSerializer build() {
            return new GsonComponentSerializerImpl(this.flags, this.legacyHoverSerializer);
        }
    }
}
