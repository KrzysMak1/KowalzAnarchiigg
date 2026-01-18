package cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.serdes;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.serdes.transformer.StringTagTransformer;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.serdes.transformer.StringWorldTransformer;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.serdes.transformer.StringPotionEffectTypeTransformer;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.BidirectionalTransformer;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.serdes.transformer.StringEnchantmentTransformer;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.serdes.serializer.VectorSerializer;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.serdes.serializer.PotionEffectSerializer;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.serdes.serializer.LocationSerializer;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesContextAttachment;
import java.lang.annotation.Annotation;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesAnnotationResolver;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.serdes.itemstack.ItemStackAttachmentResolver;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.serdes.serializer.ItemStackSerializer;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.ObjectSerializer;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.serdes.serializer.ItemMetaSerializer;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesRegistry;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.OkaeriSerdesPack;

public class SerdesBukkit implements OkaeriSerdesPack
{
    @Override
    public void register(@NonNull final SerdesRegistry registry) {
        if (registry == null) {
            throw new NullPointerException("registry is marked non-null but is null");
        }
        registry.register(new ItemMetaSerializer());
        registry.register(new ItemStackSerializer());
        registry.register((SerdesAnnotationResolver<? extends Annotation, ? extends SerdesContextAttachment>)new ItemStackAttachmentResolver());
        registry.register(new LocationSerializer());
        registry.register(new PotionEffectSerializer());
        registry.register(new VectorSerializer());
        registry.register((BidirectionalTransformer<Object, Object>)new StringEnchantmentTransformer());
        registry.register((BidirectionalTransformer<Object, Object>)new StringPotionEffectTypeTransformer());
        whenClass("org.bukkit.Tag", () -> registry.register((BidirectionalTransformer<Object, Object>)new StringTagTransformer()));
        registry.register((BidirectionalTransformer<Object, Object>)new StringWorldTransformer());
    }
    
    private static void whenClass(@NonNull final String name, @NonNull final Runnable runnable) {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        if (runnable == null) {
            throw new NullPointerException("runnable is marked non-null but is null");
        }
        try {
            Class.forName(name);
            runnable.run();
        }
        catch (final ClassNotFoundException ex) {}
    }
}
