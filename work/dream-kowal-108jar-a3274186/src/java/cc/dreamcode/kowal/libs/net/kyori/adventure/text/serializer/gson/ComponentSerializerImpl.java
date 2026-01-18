package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.gson;

import com.google.gson.reflect.TypeToken;
import java.util.Iterator;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.KeybindComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.SelectorComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.ScoreComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.TranslatableComponent;
import java.util.Map;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.TextComponent;
import com.google.gson.stream.JsonWriter;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.NBTComponentBuilder;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.NBTComponent;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.TranslationArgument;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.ComponentBuilder;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.Style;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.StorageNBTComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.EntityNBTComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.ComponentLike;
import com.google.gson.JsonElement;
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.BlockNBTComponent;
import com.google.gson.JsonParseException;
import java.util.List;
import java.util.Collections;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonToken;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.BuildableComponent;
import com.google.gson.stream.JsonReader;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.json.JSONOptions;
import cc.dreamcode.kowal.libs.net.kyori.option.OptionState;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import com.google.gson.TypeAdapter;

final class ComponentSerializerImpl extends TypeAdapter<Component>
{
    static final Type COMPONENT_LIST_TYPE;
    static final Type TRANSLATABLE_ARGUMENT_LIST_TYPE;
    private final boolean emitCompactTextComponent;
    private final Gson gson;
    
    static TypeAdapter<Component> create(final OptionState features, final Gson gson) {
        return (TypeAdapter<Component>)new ComponentSerializerImpl(features.value(JSONOptions.EMIT_COMPACT_TEXT_COMPONENT), gson).nullSafe();
    }
    
    private ComponentSerializerImpl(final boolean emitCompactTextComponent, final Gson gson) {
        this.emitCompactTextComponent = emitCompactTextComponent;
        this.gson = gson;
    }
    
    public BuildableComponent<?, ?> read(final JsonReader in) throws IOException {
        final JsonToken token = in.peek();
        if (token == JsonToken.STRING || token == JsonToken.NUMBER || token == JsonToken.BOOLEAN) {
            return Component.text(GsonHacks.readString(in));
        }
        if (token == JsonToken.BEGIN_ARRAY) {
            ComponentBuilder<?, ?> parent = null;
            in.beginArray();
            while (in.hasNext()) {
                final BuildableComponent<?, ?> child = this.read(in);
                if (parent == null) {
                    parent = (ComponentBuilder<?, ?>)child.toBuilder();
                }
                else {
                    parent.append(child);
                }
            }
            if (parent == null) {
                throw notSureHowToDeserialize(in.getPath());
            }
            in.endArray();
            return (BuildableComponent<?, ?>)parent.build();
        }
        else {
            if (token != JsonToken.BEGIN_OBJECT) {
                throw notSureHowToDeserialize(in.getPath());
            }
            final JsonObject style = new JsonObject();
            List<Component> extra = (List<Component>)Collections.emptyList();
            String text = null;
            String translate = null;
            String translateFallback = null;
            List<TranslationArgument> translateWith = null;
            String scoreName = null;
            String scoreObjective = null;
            String scoreValue = null;
            String selector = null;
            String keybind = null;
            String nbt = null;
            boolean nbtInterpret = false;
            BlockNBTComponent.Pos nbtBlock = null;
            String nbtEntity = null;
            Key nbtStorage = null;
            Component separator = null;
            in.beginObject();
            while (in.hasNext()) {
                final String fieldName = in.nextName();
                if (fieldName.equals((Object)"text")) {
                    text = GsonHacks.readString(in);
                }
                else if (fieldName.equals((Object)"translate")) {
                    translate = in.nextString();
                }
                else if (fieldName.equals((Object)"fallback")) {
                    translateFallback = in.nextString();
                }
                else if (fieldName.equals((Object)"with")) {
                    translateWith = (List<TranslationArgument>)this.gson.fromJson(in, ComponentSerializerImpl.TRANSLATABLE_ARGUMENT_LIST_TYPE);
                }
                else if (fieldName.equals((Object)"score")) {
                    in.beginObject();
                    while (in.hasNext()) {
                        final String scoreFieldName = in.nextName();
                        if (scoreFieldName.equals((Object)"name")) {
                            scoreName = in.nextString();
                        }
                        else if (scoreFieldName.equals((Object)"objective")) {
                            scoreObjective = in.nextString();
                        }
                        else if (scoreFieldName.equals((Object)"value")) {
                            scoreValue = in.nextString();
                        }
                        else {
                            in.skipValue();
                        }
                    }
                    if (scoreName == null || scoreObjective == null) {
                        throw new JsonParseException("A score component requires a name and objective");
                    }
                    in.endObject();
                }
                else if (fieldName.equals((Object)"selector")) {
                    selector = in.nextString();
                }
                else if (fieldName.equals((Object)"keybind")) {
                    keybind = in.nextString();
                }
                else if (fieldName.equals((Object)"nbt")) {
                    nbt = in.nextString();
                }
                else if (fieldName.equals((Object)"interpret")) {
                    nbtInterpret = in.nextBoolean();
                }
                else if (fieldName.equals((Object)"block")) {
                    nbtBlock = (BlockNBTComponent.Pos)this.gson.fromJson(in, (Type)SerializerFactory.BLOCK_NBT_POS_TYPE);
                }
                else if (fieldName.equals((Object)"entity")) {
                    nbtEntity = in.nextString();
                }
                else if (fieldName.equals((Object)"storage")) {
                    nbtStorage = (Key)this.gson.fromJson(in, (Type)SerializerFactory.KEY_TYPE);
                }
                else if (fieldName.equals((Object)"extra")) {
                    extra = (List<Component>)this.gson.fromJson(in, ComponentSerializerImpl.COMPONENT_LIST_TYPE);
                }
                else if (fieldName.equals((Object)"separator")) {
                    separator = this.read(in);
                }
                else {
                    style.add(fieldName, (JsonElement)this.gson.fromJson(in, (Type)JsonElement.class));
                }
            }
            ComponentBuilder<?, ?> builder;
            if (text != null) {
                builder = Component.text().content(text);
            }
            else if (translate != null) {
                if (translateWith != null) {
                    builder = Component.translatable().key(translate).fallback(translateFallback).arguments(translateWith);
                }
                else {
                    builder = Component.translatable().key(translate).fallback(translateFallback);
                }
            }
            else if (scoreName != null && scoreObjective != null) {
                if (scoreValue == null) {
                    builder = Component.score().name(scoreName).objective(scoreObjective);
                }
                else {
                    builder = Component.score().name(scoreName).objective(scoreObjective).value(scoreValue);
                }
            }
            else if (selector != null) {
                builder = Component.selector().pattern(selector).separator(separator);
            }
            else if (keybind != null) {
                builder = Component.keybind().keybind(keybind);
            }
            else {
                if (nbt == null) {
                    throw notSureHowToDeserialize(in.getPath());
                }
                if (nbtBlock != null) {
                    builder = nbt(Component.blockNBT(), nbt, nbtInterpret, separator).pos(nbtBlock);
                }
                else if (nbtEntity != null) {
                    builder = nbt(Component.entityNBT(), nbt, nbtInterpret, separator).selector(nbtEntity);
                }
                else {
                    if (nbtStorage == null) {
                        throw notSureHowToDeserialize(in.getPath());
                    }
                    builder = nbt(Component.storageNBT(), nbt, nbtInterpret, separator).storage(nbtStorage);
                }
            }
            ((ComponentBuilder<BuildableComponent, ComponentBuilder>)builder.style((Style)this.gson.fromJson((JsonElement)style, (Class)SerializerFactory.STYLE_TYPE))).append((Iterable<? extends ComponentLike>)extra);
            in.endObject();
            return (BuildableComponent<?, ?>)builder.build();
        }
    }
    
    private static <C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> B nbt(final B builder, final String nbt, final boolean interpret, @Nullable final Component separator) {
        return ((NBTComponentBuilder<C, B>)((NBTComponentBuilder<C, NBTComponentBuilder<C, B>>)builder).nbtPath(nbt).interpret(interpret)).separator(separator);
    }
    
    public void write(final JsonWriter out, final Component value) throws IOException {
        if (value instanceof TextComponent && value.children().isEmpty() && !value.hasStyling() && this.emitCompactTextComponent) {
            out.value(((TextComponent)value).content());
            return;
        }
        out.beginObject();
        if (value.hasStyling()) {
            final JsonElement style = this.gson.toJsonTree((Object)value.style(), (Type)SerializerFactory.STYLE_TYPE);
            if (style.isJsonObject()) {
                for (final Map.Entry<String, JsonElement> entry : style.getAsJsonObject().entrySet()) {
                    out.name((String)entry.getKey());
                    this.gson.toJson((JsonElement)entry.getValue(), out);
                }
            }
        }
        if (!value.children().isEmpty()) {
            out.name("extra");
            this.gson.toJson((Object)value.children(), ComponentSerializerImpl.COMPONENT_LIST_TYPE, out);
        }
        if (value instanceof TextComponent) {
            out.name("text");
            out.value(((TextComponent)value).content());
        }
        else if (value instanceof TranslatableComponent) {
            final TranslatableComponent translatable = (TranslatableComponent)value;
            out.name("translate");
            out.value(translatable.key());
            final String fallback = translatable.fallback();
            if (fallback != null) {
                out.name("fallback");
                out.value(fallback);
            }
            if (!translatable.arguments().isEmpty()) {
                out.name("with");
                this.gson.toJson((Object)translatable.arguments(), ComponentSerializerImpl.TRANSLATABLE_ARGUMENT_LIST_TYPE, out);
            }
        }
        else if (value instanceof ScoreComponent) {
            final ScoreComponent score = (ScoreComponent)value;
            out.name("score");
            out.beginObject();
            out.name("name");
            out.value(score.name());
            out.name("objective");
            out.value(score.objective());
            if (score.value() != null) {
                out.name("value");
                out.value(score.value());
            }
            out.endObject();
        }
        else if (value instanceof SelectorComponent) {
            final SelectorComponent selector = (SelectorComponent)value;
            out.name("selector");
            out.value(selector.pattern());
            this.serializeSeparator(out, selector.separator());
        }
        else if (value instanceof KeybindComponent) {
            out.name("keybind");
            out.value(((KeybindComponent)value).keybind());
        }
        else {
            if (!(value instanceof NBTComponent)) {
                throw notSureHowToSerialize(value);
            }
            final NBTComponent<?, ?> nbt = (NBTComponent<?, ?>)value;
            out.name("nbt");
            out.value(nbt.nbtPath());
            out.name("interpret");
            out.value(nbt.interpret());
            this.serializeSeparator(out, nbt.separator());
            if (value instanceof BlockNBTComponent) {
                out.name("block");
                this.gson.toJson((Object)((BlockNBTComponent)value).pos(), (Type)SerializerFactory.BLOCK_NBT_POS_TYPE, out);
            }
            else if (value instanceof EntityNBTComponent) {
                out.name("entity");
                out.value(((EntityNBTComponent)value).selector());
            }
            else {
                if (!(value instanceof StorageNBTComponent)) {
                    throw notSureHowToSerialize(value);
                }
                out.name("storage");
                this.gson.toJson((Object)((StorageNBTComponent)value).storage(), (Type)SerializerFactory.KEY_TYPE, out);
            }
        }
        out.endObject();
    }
    
    private void serializeSeparator(final JsonWriter out, @Nullable final Component separator) throws IOException {
        if (separator != null) {
            out.name("separator");
            this.write(out, separator);
        }
    }
    
    static JsonParseException notSureHowToDeserialize(final Object element) {
        return new JsonParseException("Don't know how to turn " + element + " into a Component");
    }
    
    private static IllegalArgumentException notSureHowToSerialize(final Component component) {
        return new IllegalArgumentException("Don't know how to serialize " + (Object)component + " as a Component");
    }
    
    static {
        COMPONENT_LIST_TYPE = new TypeToken<List<Component>>() {}.getType();
        TRANSLATABLE_ARGUMENT_LIST_TYPE = new TypeToken<List<TranslationArgument>>() {}.getType();
    }
}
