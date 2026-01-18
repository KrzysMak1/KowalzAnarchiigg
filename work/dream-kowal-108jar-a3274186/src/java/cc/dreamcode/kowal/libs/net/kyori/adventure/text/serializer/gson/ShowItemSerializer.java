package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.gson;

import java.util.Iterator;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.DataComponentValue;
import java.util.Map;
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Keyed;
import java.util.HashMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import cc.dreamcode.kowal.libs.net.kyori.adventure.nbt.api.BinaryTagHolder;
import com.google.gson.stream.JsonToken;
import java.lang.reflect.Type;
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;
import com.google.gson.stream.JsonReader;
import cc.dreamcode.kowal.libs.net.kyori.option.OptionState;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.json.JSONOptions;
import com.google.gson.Gson;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.HoverEvent;
import com.google.gson.TypeAdapter;

final class ShowItemSerializer extends TypeAdapter<HoverEvent.ShowItem>
{
    private static final String LEGACY_SHOW_ITEM_TAG = "tag";
    private final Gson gson;
    private final boolean emitDefaultQuantity;
    private final JSONOptions.ShowItemHoverDataMode itemDataMode;
    
    static TypeAdapter<HoverEvent.ShowItem> create(final Gson gson, final OptionState opt) {
        return (TypeAdapter<HoverEvent.ShowItem>)new ShowItemSerializer(gson, opt.value(JSONOptions.EMIT_DEFAULT_ITEM_HOVER_QUANTITY), opt.value(JSONOptions.SHOW_ITEM_HOVER_DATA_MODE)).nullSafe();
    }
    
    private ShowItemSerializer(final Gson gson, final boolean emitDefaultQuantity, final JSONOptions.ShowItemHoverDataMode itemDataMode) {
        this.gson = gson;
        this.emitDefaultQuantity = emitDefaultQuantity;
        this.itemDataMode = itemDataMode;
    }
    
    public HoverEvent.ShowItem read(final JsonReader in) throws IOException {
        in.beginObject();
        Key key = null;
        int count = 1;
        BinaryTagHolder nbt = null;
        Map<Key, DataComponentValue> dataComponents = null;
        while (in.hasNext()) {
            final String fieldName = in.nextName();
            if (fieldName.equals((Object)"id")) {
                key = (Key)this.gson.fromJson(in, (Type)SerializerFactory.KEY_TYPE);
            }
            else if (fieldName.equals((Object)"count")) {
                count = in.nextInt();
            }
            else if (fieldName.equals((Object)"tag")) {
                final JsonToken token = in.peek();
                if (token == JsonToken.STRING || token == JsonToken.NUMBER) {
                    nbt = BinaryTagHolder.binaryTagHolder(in.nextString());
                }
                else if (token == JsonToken.BOOLEAN) {
                    nbt = BinaryTagHolder.binaryTagHolder(String.valueOf(in.nextBoolean()));
                }
                else {
                    if (token != JsonToken.NULL) {
                        throw new JsonParseException("Expected tag to be a string");
                    }
                    in.nextNull();
                }
            }
            else if (fieldName.equals((Object)"components")) {
                in.beginObject();
                while (in.peek() != JsonToken.END_OBJECT) {
                    final Key id = Key.key(in.nextName());
                    final JsonElement tree = (JsonElement)this.gson.fromJson(in, (Type)JsonElement.class);
                    if (dataComponents == null) {
                        dataComponents = (Map<Key, DataComponentValue>)new HashMap();
                    }
                    dataComponents.put((Object)id, (Object)GsonDataComponentValue.gsonDataComponentValue(tree));
                }
                in.endObject();
            }
            else {
                in.skipValue();
            }
        }
        if (key == null) {
            throw new JsonParseException("Not sure how to deserialize show_item hover event");
        }
        in.endObject();
        if (dataComponents != null) {
            return HoverEvent.ShowItem.showItem(key, count, dataComponents);
        }
        return HoverEvent.ShowItem.showItem(key, count, nbt);
    }
    
    public void write(final JsonWriter out, final HoverEvent.ShowItem value) throws IOException {
        out.beginObject();
        out.name("id");
        this.gson.toJson((Object)value.item(), (Type)SerializerFactory.KEY_TYPE, out);
        final int count = value.count();
        if (count != 1 || this.emitDefaultQuantity) {
            out.name("count");
            out.value((long)count);
        }
        final Map<Key, DataComponentValue> dataComponents = value.dataComponents();
        if (!dataComponents.isEmpty() && this.itemDataMode != JSONOptions.ShowItemHoverDataMode.EMIT_LEGACY_NBT) {
            out.name("components");
            out.beginObject();
            for (final Map.Entry<Key, GsonDataComponentValue> entry : value.dataComponentsAs(GsonDataComponentValue.class).entrySet()) {
                out.name(((Key)entry.getKey()).asString());
                this.gson.toJson(((GsonDataComponentValue)entry.getValue()).element(), out);
            }
            out.endObject();
        }
        else if (this.itemDataMode != JSONOptions.ShowItemHoverDataMode.EMIT_DATA_COMPONENTS) {
            maybeWriteLegacy(out, value);
        }
        out.endObject();
    }
    
    private static void maybeWriteLegacy(final JsonWriter out, final HoverEvent.ShowItem value) throws IOException {
        final BinaryTagHolder nbt = value.nbt();
        if (nbt != null) {
            out.name("tag");
            out.value(nbt.string());
        }
    }
}
