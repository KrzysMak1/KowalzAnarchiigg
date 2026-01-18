package cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.List;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.standard.ObjectToStringTransformer;
import lombok.NonNull;
import java.util.concurrent.ConcurrentHashMap;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsPair;
import java.util.Set;
import java.lang.annotation.Annotation;
import java.util.Map;

public class SerdesRegistry
{
    private final Map<Class<? extends Annotation>, SerdesAnnotationResolver<Annotation, SerdesContextAttachment>> annotationResolverMap;
    private final Set<ObjectSerializer> serializerSet;
    private final Map<GenericsPair, ObjectTransformer> transformerMap;
    
    public SerdesRegistry() {
        this.annotationResolverMap = (Map<Class<? extends Annotation>, SerdesAnnotationResolver<Annotation, SerdesContextAttachment>>)new ConcurrentHashMap();
        this.serializerSet = (Set<ObjectSerializer>)ConcurrentHashMap.newKeySet();
        this.transformerMap = (Map<GenericsPair, ObjectTransformer>)new ConcurrentHashMap();
    }
    
    public void register(@NonNull final ObjectTransformer transformer) {
        if (transformer == null) {
            throw new NullPointerException("transformer is marked non-null but is null");
        }
        this.transformerMap.put((Object)transformer.getPair(), (Object)transformer);
    }
    
    public void register(@NonNull final OkaeriSerdesPack serdesPack) {
        if (serdesPack == null) {
            throw new NullPointerException("serdesPack is marked non-null but is null");
        }
        serdesPack.register(this);
    }
    
    public <L, R> void register(@NonNull final BidirectionalTransformer<L, R> transformer) {
        if (transformer == null) {
            throw new NullPointerException("transformer is marked non-null but is null");
        }
        this.register(new ObjectTransformer<L, R>() {
            @Override
            public GenericsPair<L, R> getPair() {
                return transformer.getPair();
            }
            
            @Override
            public R transform(@NonNull final L data, @NonNull final SerdesContext serdesContext) {
                if (data == null) {
                    throw new NullPointerException("data is marked non-null but is null");
                }
                if (serdesContext == null) {
                    throw new NullPointerException("serdesContext is marked non-null but is null");
                }
                return transformer.leftToRight(data, serdesContext);
            }
        });
        this.register(new ObjectTransformer<R, L>() {
            @Override
            public GenericsPair<R, L> getPair() {
                return transformer.getPair().reverse();
            }
            
            @Override
            public L transform(@NonNull final R data, @NonNull final SerdesContext serdesContext) {
                if (data == null) {
                    throw new NullPointerException("data is marked non-null but is null");
                }
                if (serdesContext == null) {
                    throw new NullPointerException("serdesContext is marked non-null but is null");
                }
                return transformer.rightToLeft(data, serdesContext);
            }
        });
    }
    
    public void registerWithReversedToString(@NonNull final ObjectTransformer transformer) {
        if (transformer == null) {
            throw new NullPointerException("transformer is marked non-null but is null");
        }
        this.transformerMap.put((Object)transformer.getPair(), (Object)transformer);
        this.transformerMap.put((Object)transformer.getPair().reverse(), (Object)new ObjectToStringTransformer());
    }
    
    public void register(@NonNull final ObjectSerializer serializer) {
        if (serializer == null) {
            throw new NullPointerException("serializer is marked non-null but is null");
        }
        this.serializerSet.add((Object)serializer);
    }
    
    public void registerExclusive(@NonNull final Class<?> type, @NonNull final ObjectSerializer serializer) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (serializer == null) {
            throw new NullPointerException("serializer is marked non-null but is null");
        }
        this.serializerSet.removeIf(ser -> ser.supports(type));
        this.serializerSet.add((Object)serializer);
    }
    
    public ObjectTransformer getTransformer(@NonNull final GenericsDeclaration from, @NonNull final GenericsDeclaration to) {
        if (from == null) {
            throw new NullPointerException("from is marked non-null but is null");
        }
        if (to == null) {
            throw new NullPointerException("to is marked non-null but is null");
        }
        final GenericsPair pair = new GenericsPair(from, to);
        return (ObjectTransformer)this.transformerMap.get((Object)pair);
    }
    
    public List<ObjectTransformer> getTransformersFrom(@NonNull final GenericsDeclaration from) {
        if (from == null) {
            throw new NullPointerException("from is marked non-null but is null");
        }
        return (List<ObjectTransformer>)this.transformerMap.entrySet().stream().filter(entry -> from.equals(((GenericsPair)entry.getKey()).getFrom())).map(Map.Entry::getValue).collect(Collectors.toList());
    }
    
    public List<ObjectTransformer> getTransformersTo(@NonNull final GenericsDeclaration to) {
        if (to == null) {
            throw new NullPointerException("to is marked non-null but is null");
        }
        return (List<ObjectTransformer>)this.transformerMap.entrySet().stream().filter(entry -> to.equals(((GenericsPair)entry.getKey()).getTo())).map(Map.Entry::getValue).collect(Collectors.toList());
    }
    
    public boolean canTransform(@NonNull final GenericsDeclaration from, @NonNull final GenericsDeclaration to) {
        if (from == null) {
            throw new NullPointerException("from is marked non-null but is null");
        }
        if (to == null) {
            throw new NullPointerException("to is marked non-null but is null");
        }
        return this.getTransformer(from, to) != null;
    }
    
    public ObjectSerializer getSerializer(@NonNull final Class<?> clazz) {
        if (clazz == null) {
            throw new NullPointerException("clazz is marked non-null but is null");
        }
        return (ObjectSerializer)this.serializerSet.stream().filter(serializer -> serializer.supports(clazz)).findFirst().orElse((Object)null);
    }
    
    public void register(@NonNull final SerdesAnnotationResolver<? extends Annotation, ? extends SerdesContextAttachment> annotationResolver) {
        if (annotationResolver == null) {
            throw new NullPointerException("annotationResolver is marked non-null but is null");
        }
        this.annotationResolverMap.put((Object)annotationResolver.getAnnotationType(), (Object)annotationResolver);
    }
    
    public SerdesAnnotationResolver<Annotation, SerdesContextAttachment> getAnnotationResolver(@NonNull final Class<? extends Annotation> annotationType) {
        if (annotationType == null) {
            throw new NullPointerException("annotationType is marked non-null but is null");
        }
        return (SerdesAnnotationResolver)this.annotationResolverMap.get((Object)annotationType);
    }
    
    public SerdesAnnotationResolver<Annotation, SerdesContextAttachment> getAnnotationResolver(@NonNull final Annotation annotation) {
        if (annotation == null) {
            throw new NullPointerException("annotation is marked non-null but is null");
        }
        return (SerdesAnnotationResolver)this.annotationResolverMap.get((Object)annotation.annotationType());
    }
    
    public OkaeriSerdesPack allSerdes() {
        return registry -> {
            this.transformerMap.values();
            Objects.requireNonNull((Object)registry);
            final Collection collection;
            collection.forEach(registry::register);
            final Set<ObjectSerializer> serializerSet = this.serializerSet;
            Objects.requireNonNull((Object)registry);
            serializerSet.forEach(registry::register);
            this.annotationResolverMap.values();
            Objects.requireNonNull((Object)registry);
            final Collection collection2;
            collection2.forEach(registry::register);
        };
    }
}
