package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.gson;

import java.util.Set;
import java.util.EnumSet;
import com.google.gson.JsonSyntaxException;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.TextColor;
import com.google.gson.stream.JsonWriter;
import com.google.gson.JsonParseException;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.Codec;
import java.io.IOException;
import com.google.gson.JsonPrimitive;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.HoverEventSource;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import com.google.gson.JsonElement;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.HoverEvent;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonToken;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.ClickEvent;
import java.lang.reflect.Type;
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;
import com.google.gson.stream.JsonReader;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.json.JSONOptions;
import cc.dreamcode.kowal.libs.net.kyori.option.OptionState;
import com.google.gson.Gson;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.TextDecoration;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.Style;
import com.google.gson.TypeAdapter;

final class StyleSerializer extends TypeAdapter<Style>
{
    private static final TextDecoration[] DECORATIONS;
    private final LegacyHoverEventSerializer legacyHover;
    private final boolean emitLegacyHover;
    private final boolean emitModernHover;
    private final boolean strictEventValues;
    private final Gson gson;
    
    static TypeAdapter<Style> create(final LegacyHoverEventSerializer legacyHover, final OptionState features, final Gson gson) {
        final JSONOptions.HoverEventValueMode hoverMode = features.value(JSONOptions.EMIT_HOVER_EVENT_TYPE);
        return (TypeAdapter<Style>)new StyleSerializer(legacyHover, hoverMode == JSONOptions.HoverEventValueMode.LEGACY_ONLY || hoverMode == JSONOptions.HoverEventValueMode.BOTH, hoverMode == JSONOptions.HoverEventValueMode.MODERN_ONLY || hoverMode == JSONOptions.HoverEventValueMode.BOTH, features.value(JSONOptions.VALIDATE_STRICT_EVENTS), gson).nullSafe();
    }
    
    private StyleSerializer(final LegacyHoverEventSerializer legacyHover, final boolean emitLegacyHover, final boolean emitModernHover, final boolean strictEventValues, final Gson gson) {
        this.legacyHover = legacyHover;
        this.emitLegacyHover = emitLegacyHover;
        this.emitModernHover = emitModernHover;
        this.strictEventValues = strictEventValues;
        this.gson = gson;
    }
    
    public Style read(final JsonReader in) throws IOException {
        in.beginObject();
        final Style.Builder style = Style.style();
        while (in.hasNext()) {
            final String fieldName = in.nextName();
            if (fieldName.equals((Object)"font")) {
                style.font((Key)this.gson.fromJson(in, (Type)SerializerFactory.KEY_TYPE));
            }
            else if (fieldName.equals((Object)"color")) {
                final TextColorWrapper color = (TextColorWrapper)this.gson.fromJson(in, (Type)SerializerFactory.COLOR_WRAPPER_TYPE);
                if (color.color != null) {
                    style.color(color.color);
                }
                else {
                    if (color.decoration == null) {
                        continue;
                    }
                    style.decoration(color.decoration, TextDecoration.State.TRUE);
                }
            }
            else if (TextDecoration.NAMES.keys().contains((Object)fieldName)) {
                style.decoration((TextDecoration)TextDecoration.NAMES.value(fieldName), GsonHacks.readBoolean(in));
            }
            else if (fieldName.equals((Object)"insertion")) {
                style.insertion(in.nextString());
            }
            else if (fieldName.equals((Object)"clickEvent")) {
                in.beginObject();
                ClickEvent.Action action = null;
                String value = null;
                while (in.hasNext()) {
                    final String clickEventField = in.nextName();
                    if (clickEventField.equals((Object)"action")) {
                        action = (ClickEvent.Action)this.gson.fromJson(in, (Type)SerializerFactory.CLICK_ACTION_TYPE);
                    }
                    else if (clickEventField.equals((Object)"value")) {
                        if (in.peek() == JsonToken.NULL && this.strictEventValues) {
                            throw ComponentSerializerImpl.notSureHowToDeserialize("value");
                        }
                        value = ((in.peek() == JsonToken.NULL) ? null : in.nextString());
                    }
                    else {
                        in.skipValue();
                    }
                }
                if (action != null && action.readable() && value != null) {
                    style.clickEvent(ClickEvent.clickEvent(action, value));
                }
                in.endObject();
            }
            else if (fieldName.equals((Object)"hoverEvent")) {
                final JsonObject hoverEventObject = (JsonObject)this.gson.fromJson(in, (Type)JsonObject.class);
                if (hoverEventObject == null) {
                    continue;
                }
                final JsonPrimitive serializedAction = hoverEventObject.getAsJsonPrimitive("action");
                if (serializedAction == null) {
                    continue;
                }
                final HoverEvent.Action<Object> action2 = (HoverEvent.Action<Object>)this.gson.fromJson((JsonElement)serializedAction, (Class)SerializerFactory.HOVER_ACTION_TYPE);
                if (!action2.readable()) {
                    continue;
                }
                final Class<?> actionType = action2.type();
                Object value2;
                if (hoverEventObject.has("contents")) {
                    final JsonElement rawValue = hoverEventObject.get("contents");
                    if (GsonHacks.isNullOrEmpty(rawValue)) {
                        if (this.strictEventValues) {
                            throw ComponentSerializerImpl.notSureHowToDeserialize(rawValue);
                        }
                        value2 = null;
                    }
                    else if (SerializerFactory.COMPONENT_TYPE.isAssignableFrom(actionType)) {
                        value2 = this.gson.fromJson(rawValue, (Class)SerializerFactory.COMPONENT_TYPE);
                    }
                    else if (SerializerFactory.SHOW_ITEM_TYPE.isAssignableFrom(actionType)) {
                        value2 = this.gson.fromJson(rawValue, (Class)SerializerFactory.SHOW_ITEM_TYPE);
                    }
                    else if (SerializerFactory.SHOW_ENTITY_TYPE.isAssignableFrom(actionType)) {
                        value2 = this.gson.fromJson(rawValue, (Class)SerializerFactory.SHOW_ENTITY_TYPE);
                    }
                    else {
                        value2 = null;
                    }
                }
                else if (hoverEventObject.has("value")) {
                    final JsonElement element = hoverEventObject.get("value");
                    if (GsonHacks.isNullOrEmpty(element)) {
                        if (this.strictEventValues) {
                            throw ComponentSerializerImpl.notSureHowToDeserialize(element);
                        }
                        value2 = null;
                    }
                    else if (SerializerFactory.COMPONENT_TYPE.isAssignableFrom(actionType)) {
                        final Component rawValue2 = (Component)this.gson.fromJson(element, (Class)SerializerFactory.COMPONENT_TYPE);
                        value2 = this.legacyHoverEventContents(action2, rawValue2);
                    }
                    else if (SerializerFactory.STRING_TYPE.isAssignableFrom(actionType)) {
                        value2 = this.gson.fromJson(element, (Class)SerializerFactory.STRING_TYPE);
                    }
                    else {
                        value2 = null;
                    }
                }
                else {
                    if (this.strictEventValues) {
                        throw ComponentSerializerImpl.notSureHowToDeserialize(hoverEventObject);
                    }
                    value2 = null;
                }
                if (value2 == null) {
                    continue;
                }
                style.hoverEvent((HoverEventSource<?>)HoverEvent.hoverEvent(action2, value2));
            }
            else {
                in.skipValue();
            }
        }
        in.endObject();
        return style.build();
    }
    
    private Object legacyHoverEventContents(final HoverEvent.Action<?> action, final Component rawValue) {
        if (action == HoverEvent.Action.SHOW_TEXT) {
            return rawValue;
        }
        if (this.legacyHover != null) {
            try {
                if (action == HoverEvent.Action.SHOW_ENTITY) {
                    return this.legacyHover.deserializeShowEntity(rawValue, (Codec.Decoder<Component, String, ? extends RuntimeException>)this.decoder());
                }
                if (action == HoverEvent.Action.SHOW_ITEM) {
                    return this.legacyHover.deserializeShowItem(rawValue);
                }
            }
            catch (final IOException ex) {
                throw new JsonParseException((Throwable)ex);
            }
        }
        throw new UnsupportedOperationException();
    }
    
    private Codec.Decoder<Component, String, JsonParseException> decoder() {
        return (Codec.Decoder<Component, String, JsonParseException>)(string -> this.gson.fromJson(string, (Class)SerializerFactory.COMPONENT_TYPE));
    }
    
    private Codec.Encoder<Component, String, JsonParseException> encoder() {
        return (Codec.Encoder<Component, String, JsonParseException>)(component -> this.gson.toJson((Object)component, (Type)SerializerFactory.COMPONENT_TYPE));
    }
    
    public void write(final JsonWriter out, final Style value) throws IOException {
        out.beginObject();
        for (int i = 0, length = StyleSerializer.DECORATIONS.length; i < length; ++i) {
            final TextDecoration decoration = StyleSerializer.DECORATIONS[i];
            final TextDecoration.State state = value.decoration(decoration);
            if (state != TextDecoration.State.NOT_SET) {
                final String name = TextDecoration.NAMES.key(decoration);
                assert name != null;
                out.name(name);
                out.value(state == TextDecoration.State.TRUE);
            }
        }
        final TextColor color = value.color();
        if (color != null) {
            out.name("color");
            this.gson.toJson((Object)color, (Type)SerializerFactory.COLOR_TYPE, out);
        }
        final String insertion = value.insertion();
        if (insertion != null) {
            out.name("insertion");
            out.value(insertion);
        }
        final ClickEvent clickEvent = value.clickEvent();
        if (clickEvent != null) {
            out.name("clickEvent");
            out.beginObject();
            out.name("action");
            this.gson.toJson((Object)clickEvent.action(), (Type)SerializerFactory.CLICK_ACTION_TYPE, out);
            out.name("value");
            out.value(clickEvent.value());
            out.endObject();
        }
        final HoverEvent<?> hoverEvent = value.hoverEvent();
        if (hoverEvent != null && ((this.emitModernHover && hoverEvent.action() != HoverEvent.Action.SHOW_ACHIEVEMENT) || this.emitLegacyHover)) {
            out.name("hoverEvent");
            out.beginObject();
            out.name("action");
            final HoverEvent.Action<?> action = hoverEvent.action();
            this.gson.toJson((Object)action, (Type)SerializerFactory.HOVER_ACTION_TYPE, out);
            if (this.emitModernHover && action != HoverEvent.Action.SHOW_ACHIEVEMENT) {
                out.name("contents");
                if (action == HoverEvent.Action.SHOW_ITEM) {
                    this.gson.toJson((Object)hoverEvent.value(), (Type)SerializerFactory.SHOW_ITEM_TYPE, out);
                }
                else if (action == HoverEvent.Action.SHOW_ENTITY) {
                    this.gson.toJson((Object)hoverEvent.value(), (Type)SerializerFactory.SHOW_ENTITY_TYPE, out);
                }
                else {
                    if (action != HoverEvent.Action.SHOW_TEXT) {
                        throw new JsonParseException("Don't know how to serialize " + (Object)hoverEvent.value());
                    }
                    this.gson.toJson((Object)hoverEvent.value(), (Type)SerializerFactory.COMPONENT_TYPE, out);
                }
            }
            if (this.emitLegacyHover) {
                out.name("value");
                this.serializeLegacyHoverEvent(hoverEvent, out);
            }
            out.endObject();
        }
        final Key font = value.font();
        if (font != null) {
            out.name("font");
            this.gson.toJson((Object)font, (Type)SerializerFactory.KEY_TYPE, out);
        }
        out.endObject();
    }
    
    private void serializeLegacyHoverEvent(final HoverEvent<?> hoverEvent, final JsonWriter out) throws IOException {
        if (hoverEvent.action() == HoverEvent.Action.SHOW_TEXT) {
            this.gson.toJson((Object)hoverEvent.value(), (Type)SerializerFactory.COMPONENT_TYPE, out);
        }
        else if (hoverEvent.action() == HoverEvent.Action.SHOW_ACHIEVEMENT) {
            this.gson.toJson((Object)hoverEvent.value(), (Type)String.class, out);
        }
        else if (this.legacyHover != null) {
            Component serialized = null;
            try {
                if (hoverEvent.action() == HoverEvent.Action.SHOW_ENTITY) {
                    serialized = this.legacyHover.serializeShowEntity((HoverEvent.ShowEntity)hoverEvent.value(), (Codec.Encoder<Component, String, ? extends RuntimeException>)this.encoder());
                }
                else if (hoverEvent.action() == HoverEvent.Action.SHOW_ITEM) {
                    serialized = this.legacyHover.serializeShowItem((HoverEvent.ShowItem)hoverEvent.value());
                }
            }
            catch (final IOException ex) {
                throw new JsonSyntaxException((Throwable)ex);
            }
            if (serialized != null) {
                this.gson.toJson((Object)serialized, (Type)SerializerFactory.COMPONENT_TYPE, out);
            }
            else {
                out.nullValue();
            }
        }
        else {
            out.nullValue();
        }
    }
    
    static {
        DECORATIONS = new TextDecoration[] { TextDecoration.BOLD, TextDecoration.ITALIC, TextDecoration.UNDERLINED, TextDecoration.STRIKETHROUGH, TextDecoration.OBFUSCATED };
        final Set<TextDecoration> knownDecorations = (Set<TextDecoration>)EnumSet.allOf((Class)TextDecoration.class);
        for (final TextDecoration decoration : StyleSerializer.DECORATIONS) {
            knownDecorations.remove((Object)decoration);
        }
        if (!knownDecorations.isEmpty()) {
            throw new IllegalStateException("Gson serializer is missing some text decorations: " + (Object)knownDecorations);
        }
    }
}
