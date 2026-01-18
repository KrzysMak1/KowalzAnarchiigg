package cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.commons.duration;

import java.lang.annotation.Annotation;
import java.time.temporal.TemporalUnit;
import java.util.Optional;
import lombok.NonNull;
import java.lang.reflect.Field;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesAnnotationResolver;

public class DurationAttachmentResolver implements SerdesAnnotationResolver<DurationSpec, DurationSpecData>
{
    @Override
    public Class<DurationSpec> getAnnotationType() {
        return DurationSpec.class;
    }
    
    @Override
    public Optional<DurationSpecData> resolveAttachment(@NonNull final Field field, @NonNull final DurationSpec annotation) {
        if (field == null) {
            throw new NullPointerException("field is marked non-null but is null");
        }
        if (annotation == null) {
            throw new NullPointerException("annotation is marked non-null but is null");
        }
        return (Optional<DurationSpecData>)Optional.of((Object)DurationSpecData.of((TemporalUnit)annotation.fallbackUnit(), annotation.format()));
    }
}
