package cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.serdes.itemstack;

import java.lang.annotation.Annotation;
import java.util.Optional;
import lombok.NonNull;
import java.lang.reflect.Field;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesAnnotationResolver;

public class ItemStackAttachmentResolver implements SerdesAnnotationResolver<ItemStackSpec, ItemStackSpecData>
{
    @Override
    public Class<ItemStackSpec> getAnnotationType() {
        return ItemStackSpec.class;
    }
    
    @Override
    public Optional<ItemStackSpecData> resolveAttachment(@NonNull final Field field, @NonNull final ItemStackSpec annotation) {
        if (field == null) {
            throw new NullPointerException("field is marked non-null but is null");
        }
        if (annotation == null) {
            throw new NullPointerException("annotation is marked non-null but is null");
        }
        return (Optional<ItemStackSpecData>)Optional.of((Object)ItemStackSpecData.of(annotation.format()));
    }
}
