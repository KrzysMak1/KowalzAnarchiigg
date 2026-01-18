package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.gson;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.json.JSONOptions;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer;
import cc.dreamcode.kowal.libs.net.kyori.option.OptionState;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.TranslationArgument;
import java.util.UUID;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.BlockNBTComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.TextDecoration;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.TextColor;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.HoverEvent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.ClickEvent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.Style;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;
import com.google.gson.TypeAdapterFactory;

final class SerializerFactory implements TypeAdapterFactory
{
    static final Class<Key> KEY_TYPE;
    static final Class<Component> COMPONENT_TYPE;
    static final Class<Style> STYLE_TYPE;
    static final Class<ClickEvent.Action> CLICK_ACTION_TYPE;
    static final Class<HoverEvent.Action> HOVER_ACTION_TYPE;
    static final Class<HoverEvent.ShowItem> SHOW_ITEM_TYPE;
    static final Class<HoverEvent.ShowEntity> SHOW_ENTITY_TYPE;
    static final Class<String> STRING_TYPE;
    static final Class<TextColorWrapper> COLOR_WRAPPER_TYPE;
    static final Class<TextColor> COLOR_TYPE;
    static final Class<TextDecoration> TEXT_DECORATION_TYPE;
    static final Class<BlockNBTComponent.Pos> BLOCK_NBT_POS_TYPE;
    static final Class<UUID> UUID_TYPE;
    static final Class<TranslationArgument> TRANSLATION_ARGUMENT_TYPE;
    private final OptionState features;
    private final LegacyHoverEventSerializer legacyHoverSerializer;
    
    SerializerFactory(final OptionState features, final LegacyHoverEventSerializer legacyHoverSerializer) {
        this.features = features;
        this.legacyHoverSerializer = legacyHoverSerializer;
    }
    
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
        final Class<? super T> rawType = type.getRawType();
        if (SerializerFactory.COMPONENT_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>)ComponentSerializerImpl.create(this.features, gson);
        }
        if (SerializerFactory.KEY_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>)KeySerializer.INSTANCE;
        }
        if (SerializerFactory.STYLE_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>)StyleSerializer.create(this.legacyHoverSerializer, this.features, gson);
        }
        if (SerializerFactory.CLICK_ACTION_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>)ClickEventActionSerializer.INSTANCE;
        }
        if (SerializerFactory.HOVER_ACTION_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>)HoverEventActionSerializer.INSTANCE;
        }
        if (SerializerFactory.SHOW_ITEM_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>)ShowItemSerializer.create(gson, this.features);
        }
        if (SerializerFactory.SHOW_ENTITY_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>)ShowEntitySerializer.create(gson);
        }
        if (SerializerFactory.COLOR_WRAPPER_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>)TextColorWrapper.Serializer.INSTANCE;
        }
        if (SerializerFactory.COLOR_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>)(this.features.value(JSONOptions.EMIT_RGB) ? TextColorSerializer.INSTANCE : TextColorSerializer.DOWNSAMPLE_COLOR);
        }
        if (SerializerFactory.TEXT_DECORATION_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>)TextDecorationSerializer.INSTANCE;
        }
        if (SerializerFactory.BLOCK_NBT_POS_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>)BlockNBTComponentPosSerializer.INSTANCE;
        }
        if (SerializerFactory.UUID_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>)UUIDSerializer.uuidSerializer(this.features);
        }
        if (SerializerFactory.TRANSLATION_ARGUMENT_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>)TranslationArgumentSerializer.create(gson);
        }
        return null;
    }
    
    static {
        KEY_TYPE = Key.class;
        COMPONENT_TYPE = Component.class;
        STYLE_TYPE = Style.class;
        CLICK_ACTION_TYPE = ClickEvent.Action.class;
        HOVER_ACTION_TYPE = HoverEvent.Action.class;
        SHOW_ITEM_TYPE = HoverEvent.ShowItem.class;
        SHOW_ENTITY_TYPE = HoverEvent.ShowEntity.class;
        STRING_TYPE = String.class;
        COLOR_WRAPPER_TYPE = TextColorWrapper.class;
        COLOR_TYPE = TextColor.class;
        TEXT_DECORATION_TYPE = TextDecoration.class;
        BLOCK_NBT_POS_TYPE = BlockNBTComponent.Pos.class;
        UUID_TYPE = UUID.class;
        TRANSLATION_ARGUMENT_TYPE = TranslationArgument.class;
    }
}
