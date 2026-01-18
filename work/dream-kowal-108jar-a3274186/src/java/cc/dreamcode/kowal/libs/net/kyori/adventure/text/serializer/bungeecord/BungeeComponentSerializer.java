package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.bungeecord;

import java.io.IOException;
import com.google.gson.stream.JsonWriter;
import com.google.gson.TypeAdapterFactory;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Field;
import com.google.gson.GsonBuilder;
import java.util.function.Consumer;
import java.util.Objects;
import com.google.gson.Gson;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.ComponentSerializer;

public final class BungeeComponentSerializer implements ComponentSerializer<Component, Component, BaseComponent[]>
{
    private static boolean SUPPORTED;
    private static final BungeeComponentSerializer MODERN;
    private static final BungeeComponentSerializer PRE_1_16;
    private final GsonComponentSerializer serializer;
    private final LegacyComponentSerializer legacySerializer;
    
    public static boolean isNative() {
        return BungeeComponentSerializer.SUPPORTED;
    }
    
    public static BungeeComponentSerializer get() {
        return BungeeComponentSerializer.MODERN;
    }
    
    public static BungeeComponentSerializer legacy() {
        return BungeeComponentSerializer.PRE_1_16;
    }
    
    public static BungeeComponentSerializer of(final GsonComponentSerializer serializer, final LegacyComponentSerializer legacySerializer) {
        if (serializer == null || legacySerializer == null) {
            return null;
        }
        return new BungeeComponentSerializer(serializer, legacySerializer);
    }
    
    public static boolean inject(final Gson existing) {
        final boolean result = GsonInjections.injectGson((Gson)Objects.requireNonNull((Object)existing, "existing"), (Consumer<GsonBuilder>)(builder -> {
            GsonComponentSerializer.gson().populator().apply((Object)builder);
            builder.registerTypeAdapterFactory((TypeAdapterFactory)new SelfSerializable.AdapterFactory());
        }));
        BungeeComponentSerializer.SUPPORTED &= result;
        return result;
    }
    
    private BungeeComponentSerializer(final GsonComponentSerializer serializer, final LegacyComponentSerializer legacySerializer) {
        this.serializer = serializer;
        this.legacySerializer = legacySerializer;
    }
    
    private static void bind() {
        try {
            final Field gsonField = GsonInjections.field(net.md_5.bungee.chat.ComponentSerializer.class, "gson");
            inject((Gson)gsonField.get((Object)null));
        }
        catch (final Throwable error) {
            BungeeComponentSerializer.SUPPORTED = false;
        }
    }
    
    @NotNull
    @Override
    public Component deserialize(@NotNull final BaseComponent[] input) {
        Objects.requireNonNull((Object)input, "input");
        if (input.length == 1 && input[0] instanceof AdapterComponent) {
            return ((AdapterComponent)input[0]).component;
        }
        return ((ComponentSerializer<I, Component, String>)this.serializer).deserialize(net.md_5.bungee.chat.ComponentSerializer.toString(input));
    }
    
    @NotNull
    @Override
    public BaseComponent[] serialize(@NotNull final Component component) {
        Objects.requireNonNull((Object)component, "component");
        if (BungeeComponentSerializer.SUPPORTED) {
            return new BaseComponent[] { new AdapterComponent(component) };
        }
        return net.md_5.bungee.chat.ComponentSerializer.parse((String)((ComponentSerializer<Component, O, String>)this.serializer).serialize(component));
    }
    
    static {
        BungeeComponentSerializer.SUPPORTED = true;
        bind();
        MODERN = new BungeeComponentSerializer(GsonComponentSerializer.gson(), LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().build());
        PRE_1_16 = new BungeeComponentSerializer(GsonComponentSerializer.builder().downsampleColors().emitLegacyHoverEvent().build(), LegacyComponentSerializer.legacySection());
    }
    
    class AdapterComponent extends BaseComponent implements SelfSerializable
    {
        private final Component component;
        private volatile String legacy;
        
        AdapterComponent(final Component component) {
            this.component = component;
        }
        
        public String toLegacyText() {
            if (this.legacy == null) {
                this.legacy = BungeeComponentSerializer.this.legacySerializer.serialize(this.component);
            }
            return this.legacy;
        }
        
        @NotNull
        public BaseComponent duplicate() {
            return this;
        }
        
        public void write(final JsonWriter out) throws IOException {
            BungeeComponentSerializer.this.serializer.serializer().getAdapter((Class)Component.class).write(out, (Object)this.component);
        }
    }
}
