package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.bungeecord;

import com.google.gson.internal.Excluder;
import java.util.Iterator;
import com.google.gson.TypeAdapterFactory;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.GsonBuilder;
import java.util.function.Consumer;
import com.google.gson.Gson;
import java.lang.reflect.Field;
import org.jetbrains.annotations.NotNull;

final class GsonInjections
{
    private GsonInjections() {
    }
    
    public static Field field(@NotNull final Class<?> klass, @NotNull final String name) throws NoSuchFieldException {
        final Field field = klass.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }
    
    public static boolean injectGson(@NotNull final Gson existing, @NotNull final Consumer<GsonBuilder> accepter) {
        try {
            final Field factoriesField = field(Gson.class, "factories");
            final Field builderFactoriesField = field(GsonBuilder.class, "factories");
            final Field builderHierarchyFactoriesField = field(GsonBuilder.class, "hierarchyFactories");
            final GsonBuilder builder = new GsonBuilder();
            accepter.accept((Object)builder);
            final List<TypeAdapterFactory> existingFactories = (List<TypeAdapterFactory>)factoriesField.get((Object)existing);
            final List<TypeAdapterFactory> newFactories = (List<TypeAdapterFactory>)new ArrayList();
            newFactories.addAll((Collection)builderFactoriesField.get((Object)builder));
            Collections.reverse((List)newFactories);
            newFactories.addAll((Collection)builderHierarchyFactoriesField.get((Object)builder));
            final List<TypeAdapterFactory> modifiedFactories = (List<TypeAdapterFactory>)new ArrayList((Collection)existingFactories);
            final int index = findExcluderIndex(modifiedFactories);
            Collections.reverse((List)newFactories);
            for (final TypeAdapterFactory newFactory : newFactories) {
                modifiedFactories.add(index, (Object)newFactory);
            }
            factoriesField.set((Object)existing, (Object)modifiedFactories);
            return true;
        }
        catch (final NoSuchFieldException | IllegalAccessException ex) {
            return false;
        }
    }
    
    private static int findExcluderIndex(@NotNull final List<TypeAdapterFactory> factories) {
        for (int i = 0, size = factories.size(); i < size; ++i) {
            final TypeAdapterFactory factory = (TypeAdapterFactory)factories.get(i);
            if (factory instanceof Excluder) {
                return i + 1;
            }
        }
        return 0;
    }
}
