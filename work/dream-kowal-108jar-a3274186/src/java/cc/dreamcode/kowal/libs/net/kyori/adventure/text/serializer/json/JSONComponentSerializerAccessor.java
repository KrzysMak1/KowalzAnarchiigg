package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.json;

import java.util.function.Supplier;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.Services;
import java.util.Optional;

final class JSONComponentSerializerAccessor
{
    private static final Optional<JSONComponentSerializer.Provider> SERVICE;
    
    private JSONComponentSerializerAccessor() {
    }
    
    static {
        SERVICE = Services.serviceWithFallback(JSONComponentSerializer.Provider.class);
    }
    
    static final class Instances
    {
        static final JSONComponentSerializer INSTANCE;
        static final Supplier<JSONComponentSerializer.Builder> BUILDER_SUPPLIER;
        
        static {
            INSTANCE = (JSONComponentSerializer)JSONComponentSerializerAccessor.SERVICE.map(JSONComponentSerializer.Provider::instance).orElse((Object)DummyJSONComponentSerializer.INSTANCE);
            BUILDER_SUPPLIER = (Supplier)JSONComponentSerializerAccessor.SERVICE.map(JSONComponentSerializer.Provider::builder).orElse((Object)DummyJSONComponentSerializer.BuilderImpl::new);
        }
    }
}
