package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.gson;

import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import com.google.gson.JsonParseException;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import java.util.UUID;
import java.lang.reflect.Type;
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;
import com.google.gson.stream.JsonReader;
import com.google.gson.Gson;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.HoverEvent;
import com.google.gson.TypeAdapter;

final class ShowEntitySerializer extends TypeAdapter<HoverEvent.ShowEntity>
{
    private final Gson gson;
    
    static TypeAdapter<HoverEvent.ShowEntity> create(final Gson gson) {
        return (TypeAdapter<HoverEvent.ShowEntity>)new ShowEntitySerializer(gson).nullSafe();
    }
    
    private ShowEntitySerializer(final Gson gson) {
        this.gson = gson;
    }
    
    public HoverEvent.ShowEntity read(final JsonReader in) throws IOException {
        in.beginObject();
        Key type = null;
        UUID id = null;
        Component name = null;
        while (in.hasNext()) {
            final String fieldName = in.nextName();
            if (fieldName.equals((Object)"type")) {
                type = (Key)this.gson.fromJson(in, (Type)SerializerFactory.KEY_TYPE);
            }
            else if (fieldName.equals((Object)"id")) {
                id = (UUID)this.gson.fromJson(in, (Type)SerializerFactory.UUID_TYPE);
            }
            else if (fieldName.equals((Object)"name")) {
                name = (Component)this.gson.fromJson(in, (Type)SerializerFactory.COMPONENT_TYPE);
            }
            else {
                in.skipValue();
            }
        }
        if (type == null || id == null) {
            throw new JsonParseException("A show entity hover event needs type and id fields to be deserialized");
        }
        in.endObject();
        return HoverEvent.ShowEntity.showEntity(type, id, name);
    }
    
    public void write(final JsonWriter out, final HoverEvent.ShowEntity value) throws IOException {
        out.beginObject();
        out.name("type");
        this.gson.toJson((Object)value.type(), (Type)SerializerFactory.KEY_TYPE, out);
        out.name("id");
        this.gson.toJson((Object)value.id(), (Type)SerializerFactory.UUID_TYPE, out);
        final Component name = value.name();
        if (name != null) {
            out.name("name");
            this.gson.toJson((Object)name, (Type)SerializerFactory.COMPONENT_TYPE, out);
        }
        out.endObject();
    }
}
