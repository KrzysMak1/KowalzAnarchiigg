package cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import lombok.NonNull;

public interface ObjectSerializer<T>
{
    public static final String VALUE = "$$__value__$$";
    
    boolean supports(@NonNull final Class<? super T> type);
    
    void serialize(@NonNull final T object, @NonNull final SerializationData data, @NonNull final GenericsDeclaration generics);
    
    T deserialize(@NonNull final DeserializationData data, @NonNull final GenericsDeclaration generics);
}
