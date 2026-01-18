package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.gson;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.ComponentLike;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.lang.reflect.Type;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import com.google.gson.stream.JsonWriter;
import com.google.gson.Gson;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.TranslationArgument;
import com.google.gson.TypeAdapter;

final class TranslationArgumentSerializer extends TypeAdapter<TranslationArgument>
{
    private final Gson gson;
    
    static TypeAdapter<TranslationArgument> create(final Gson gson) {
        return (TypeAdapter<TranslationArgument>)new TranslationArgumentSerializer(gson).nullSafe();
    }
    
    private TranslationArgumentSerializer(final Gson gson) {
        this.gson = gson;
    }
    
    public void write(final JsonWriter out, final TranslationArgument value) throws IOException {
        final Object raw = value.value();
        if (raw instanceof Boolean) {
            out.value((Boolean)raw);
        }
        else if (raw instanceof Number) {
            out.value((Number)raw);
        }
        else {
            if (!(raw instanceof Component)) {
                throw new IllegalStateException("Unable to serialize translatable argument of type " + (Object)raw.getClass() + ": " + raw);
            }
            this.gson.toJson(raw, (Type)SerializerFactory.COMPONENT_TYPE, out);
        }
    }
    
    public TranslationArgument read(final JsonReader in) throws IOException {
        switch (in.peek()) {
            case BOOLEAN: {
                return TranslationArgument.bool(in.nextBoolean());
            }
            case NUMBER: {
                return TranslationArgument.numeric((Number)this.gson.fromJson(in, (Type)Number.class));
            }
            default: {
                return TranslationArgument.component((ComponentLike)this.gson.fromJson(in, (Type)SerializerFactory.COMPONENT_TYPE));
            }
        }
    }
}
