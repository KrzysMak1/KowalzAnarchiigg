package cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.commons;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesContextAttachment;
import java.lang.annotation.Annotation;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesAnnotationResolver;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.commons.duration.DurationAttachmentResolver;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.BidirectionalTransformer;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.commons.duration.DurationTransformer;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesRegistry;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.OkaeriSerdesPack;

public class SerdesCommons implements OkaeriSerdesPack
{
    @Override
    public void register(@NonNull final SerdesRegistry registry) {
        if (registry == null) {
            throw new NullPointerException("registry is marked non-null but is null");
        }
        registry.register((BidirectionalTransformer<Object, Object>)new DurationTransformer());
        registry.register((SerdesAnnotationResolver<? extends Annotation, ? extends SerdesContextAttachment>)new DurationAttachmentResolver());
        registry.register((BidirectionalTransformer<Object, Object>)new InstantTransformer());
        registry.register((BidirectionalTransformer<Object, Object>)new LocaleTransformer());
        registry.register((BidirectionalTransformer<Object, Object>)new PatternTransformer());
    }
}
