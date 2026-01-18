package cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes;

import java.util.Optional;
import lombok.NonNull;
import java.lang.reflect.Field;
import java.lang.annotation.Annotation;

public interface SerdesAnnotationResolver<A extends Annotation, D extends SerdesContextAttachment>
{
    Class<A> getAnnotationType();
    
    Optional<D> resolveAttachment(@NonNull final Field field, @NonNull final A annotation);
}
