package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.bungeecord;

import com.google.gson.stream.JsonReader;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.TypeAdapterFactory;
import java.io.IOException;
import com.google.gson.stream.JsonWriter;

interface SelfSerializable
{
    void write(final JsonWriter out) throws IOException;
    
    public static class AdapterFactory implements TypeAdapterFactory
    {
        public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
            if (!SelfSerializable.class.isAssignableFrom(type.getRawType())) {
                return null;
            }
            return new SelfSerializableTypeAdapter<T>(type);
        }
        
        static {
            SelfSerializableTypeAdapter.class.getName();
        }
        
        static class SelfSerializableTypeAdapter<T> extends TypeAdapter<T>
        {
            private final TypeToken<T> type;
            
            SelfSerializableTypeAdapter(final TypeToken<T> type) {
                this.type = type;
            }
            
            public void write(final JsonWriter out, final T value) throws IOException {
                ((SelfSerializable)value).write(out);
            }
            
            public T read(final JsonReader in) {
                throw new UnsupportedOperationException("Cannot load values of type " + this.type.getType().getTypeName());
            }
        }
    }
}
