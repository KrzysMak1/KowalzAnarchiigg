package cc.dreamcode.kowal.libs.net.kyori.adventure.platform.bukkit;

import org.bukkit.Bukkit;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandle;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;
import java.util.Arrays;
import org.jetbrains.annotations.Nullable;
import java.lang.invoke.MethodHandles;

final class MinecraftReflection
{
    private static final MethodHandles.Lookup LOOKUP;
    private static final String PREFIX_NMS = "net.minecraft.server";
    private static final String PREFIX_MC = "net.minecraft.";
    private static final String PREFIX_CRAFTBUKKIT = "org.bukkit.craftbukkit";
    private static final String CRAFT_SERVER = "CraftServer";
    @Nullable
    private static final String VERSION;
    
    private MinecraftReflection() {
    }
    
    @Nullable
    public static Class<?> findClass(@Nullable final String... classNames) {
        for (final String clazz : classNames) {
            if (clazz != null) {
                try {
                    final Class<?> classObj = Class.forName(clazz);
                    return classObj;
                }
                catch (final ClassNotFoundException ex) {}
            }
        }
        return null;
    }
    
    @NotNull
    public static Class<?> needClass(@Nullable final String... className) {
        return (Class)Objects.requireNonNull((Object)findClass(className), "Could not find class from candidates" + Arrays.toString((Object[])className));
    }
    
    public static boolean hasClass(@NotNull final String... classNames) {
        return findClass(classNames) != null;
    }
    
    @Nullable
    public static MethodHandle findMethod(@Nullable final Class<?> holderClass, final String methodName, @Nullable final Class<?> returnClass, final Class<?>... parameterClasses) {
        return findMethod(holderClass, new String[] { methodName }, returnClass, parameterClasses);
    }
    
    @Nullable
    public static MethodHandle findMethod(@Nullable final Class<?> holderClass, @Nullable final String[] methodNames, @Nullable final Class<?> returnClass, final Class<?>... parameterClasses) {
        if (holderClass == null || returnClass == null) {
            return null;
        }
        for (final Class<?> parameterClass : parameterClasses) {
            if (parameterClass == null) {
                return null;
            }
        }
        for (final String methodName : methodNames) {
            if (methodName != null) {
                try {
                    return MinecraftReflection.LOOKUP.findVirtual((Class)holderClass, methodName, MethodType.methodType((Class)returnClass, (Class[])parameterClasses));
                }
                catch (final NoSuchMethodException | IllegalAccessException ex) {}
            }
        }
        return null;
    }
    
    public static MethodHandle searchMethod(@Nullable final Class<?> holderClass, @Nullable final Integer modifier, final String methodName, @Nullable final Class<?> returnClass, final Class<?>... parameterClasses) {
        return searchMethod(holderClass, modifier, new String[] { methodName }, returnClass, parameterClasses);
    }
    
    public static MethodHandle searchMethod(@Nullable final Class<?> holderClass, @Nullable final Integer modifier, @Nullable final String[] methodNames, @Nullable final Class<?> returnClass, final Class<?>... parameterClasses) {
        if (holderClass == null || returnClass == null) {
            return null;
        }
        for (final Class<?> parameterClass : parameterClasses) {
            if (parameterClass == null) {
                return null;
            }
        }
        for (final String methodName : methodNames) {
            if (methodName != null) {
                try {
                    if (modifier != null && Modifier.isStatic((int)modifier)) {
                        return MinecraftReflection.LOOKUP.findStatic((Class)holderClass, methodName, MethodType.methodType((Class)returnClass, (Class[])parameterClasses));
                    }
                    return MinecraftReflection.LOOKUP.findVirtual((Class)holderClass, methodName, MethodType.methodType((Class)returnClass, (Class[])parameterClasses));
                }
                catch (final NoSuchMethodException | IllegalAccessException ex) {}
            }
        }
        for (final Method method : holderClass.getDeclaredMethods()) {
            if (modifier != null && (method.getModifiers() & modifier) != 0x0) {
                if (Arrays.equals((Object[])method.getParameterTypes(), (Object[])parameterClasses)) {
                    try {
                        if (Modifier.isStatic((int)modifier)) {
                            return MinecraftReflection.LOOKUP.findStatic((Class)holderClass, method.getName(), MethodType.methodType((Class)returnClass, (Class[])parameterClasses));
                        }
                        return MinecraftReflection.LOOKUP.findVirtual((Class)holderClass, method.getName(), MethodType.methodType((Class)returnClass, (Class[])parameterClasses));
                    }
                    catch (final NoSuchMethodException | IllegalAccessException ex2) {}
                }
            }
        }
        return null;
    }
    
    @Nullable
    public static MethodHandle findStaticMethod(@Nullable final Class<?> holderClass, final String methodNames, @Nullable final Class<?> returnClass, final Class<?>... parameterClasses) {
        return findStaticMethod(holderClass, new String[] { methodNames }, returnClass, parameterClasses);
    }
    
    @Nullable
    public static MethodHandle findStaticMethod(@Nullable final Class<?> holderClass, final String[] methodNames, @Nullable final Class<?> returnClass, final Class<?>... parameterClasses) {
        if (holderClass == null || returnClass == null) {
            return null;
        }
        for (final Class<?> parameterClass : parameterClasses) {
            if (parameterClass == null) {
                return null;
            }
        }
        final int length2 = methodNames.length;
        int j = 0;
        while (j < length2) {
            final String methodName = methodNames[j];
            try {
                return MinecraftReflection.LOOKUP.findStatic((Class)holderClass, methodName, MethodType.methodType((Class)returnClass, (Class[])parameterClasses));
            }
            catch (final NoSuchMethodException | IllegalAccessException ex) {
                ++j;
                continue;
            }
            break;
        }
        return null;
    }
    
    public static boolean hasField(@Nullable final Class<?> holderClass, final Class<?> type, final String... names) {
        if (holderClass == null) {
            return false;
        }
        for (final String name : names) {
            try {
                final Field field = holderClass.getDeclaredField(name);
                if (field.getType() == type) {
                    return true;
                }
            }
            catch (final NoSuchFieldException ex) {}
        }
        return false;
    }
    
    public static boolean hasMethod(@Nullable final Class<?> holderClass, final String methodName, final Class<?>... parameterClasses) {
        return hasMethod(holderClass, new String[] { methodName }, parameterClasses);
    }
    
    public static boolean hasMethod(@Nullable final Class<?> holderClass, final String[] methodNames, final Class<?>... parameterClasses) {
        if (holderClass == null) {
            return false;
        }
        for (final Class<?> parameterClass : parameterClasses) {
            if (parameterClass == null) {
                return false;
            }
        }
        final int length2 = methodNames.length;
        int j = 0;
        while (j < length2) {
            final String methodName = methodNames[j];
            try {
                holderClass.getMethod(methodName, parameterClasses);
                return true;
            }
            catch (final NoSuchMethodException ex) {
                ++j;
                continue;
            }
            break;
        }
        return false;
    }
    
    @Nullable
    public static MethodHandle findConstructor(@Nullable final Class<?> holderClass, @Nullable final Class<?>... parameterClasses) {
        if (holderClass == null) {
            return null;
        }
        for (final Class<?> parameterClass : parameterClasses) {
            if (parameterClass == null) {
                return null;
            }
        }
        try {
            return MinecraftReflection.LOOKUP.findConstructor((Class)holderClass, MethodType.methodType(Void.TYPE, (Class[])parameterClasses));
        }
        catch (final NoSuchMethodException | IllegalAccessException e) {
            return null;
        }
    }
    
    @NotNull
    public static Field needField(@NotNull final Class<?> holderClass, @NotNull final String fieldName) throws NoSuchFieldException {
        final Field field = holderClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }
    
    @Nullable
    public static Field findField(@Nullable final Class<?> holderClass, @NotNull final String... fieldName) {
        return findField(holderClass, (Class<?>)null, fieldName);
    }
    
    @Nullable
    public static Field findField(@Nullable final Class<?> holderClass, @Nullable final Class<?> expectedType, @NotNull final String... fieldNames) {
        if (holderClass == null) {
            return null;
        }
        for (final String fieldName : fieldNames) {
            Label_0071: {
                Field field;
                try {
                    field = holderClass.getDeclaredField(fieldName);
                }
                catch (final NoSuchFieldException ex) {
                    break Label_0071;
                }
                field.setAccessible(true);
                if (expectedType == null || expectedType.isAssignableFrom(field.getType())) {
                    return field;
                }
            }
        }
        return null;
    }
    
    @Nullable
    public static MethodHandle findSetterOf(@Nullable final Field field) {
        if (field == null) {
            return null;
        }
        try {
            return MinecraftReflection.LOOKUP.unreflectSetter(field);
        }
        catch (final IllegalAccessException e) {
            return null;
        }
    }
    
    @Nullable
    public static MethodHandle findGetterOf(@Nullable final Field field) {
        if (field == null) {
            return null;
        }
        try {
            return MinecraftReflection.LOOKUP.unreflectGetter(field);
        }
        catch (final IllegalAccessException e) {
            return null;
        }
    }
    
    @Nullable
    public static Object findEnum(@Nullable final Class<?> enumClass, @NotNull final String enumName) {
        return findEnum(enumClass, enumName, Integer.MAX_VALUE);
    }
    
    @Nullable
    public static Object findEnum(@Nullable final Class<?> enumClass, @NotNull final String enumName, final int enumFallbackOrdinal) {
        if (enumClass == null || !Enum.class.isAssignableFrom(enumClass)) {
            return null;
        }
        try {
            return Enum.valueOf((Class)enumClass.asSubclass(Enum.class), enumName);
        }
        catch (final IllegalArgumentException e) {
            final Object[] constants = (Object[])enumClass.getEnumConstants();
            if (constants.length > enumFallbackOrdinal) {
                return constants[enumFallbackOrdinal];
            }
            return null;
        }
    }
    
    public static boolean isCraftBukkit() {
        return MinecraftReflection.VERSION != null;
    }
    
    @Nullable
    public static String findCraftClassName(@NotNull final String className) {
        return isCraftBukkit() ? ("org.bukkit.craftbukkit" + MinecraftReflection.VERSION + className) : null;
    }
    
    @Nullable
    public static Class<?> findCraftClass(@NotNull final String className) {
        final String craftClassName = findCraftClassName(className);
        if (craftClassName == null) {
            return null;
        }
        return findClass(craftClassName);
    }
    
    @Nullable
    public static <T> Class<? extends T> findCraftClass(@NotNull final String className, @NotNull final Class<T> superClass) {
        final Class<?> craftClass = findCraftClass(className);
        if (craftClass == null || !((Class)Objects.requireNonNull((Object)superClass, "superClass")).isAssignableFrom(craftClass)) {
            return null;
        }
        return craftClass.asSubclass(superClass);
    }
    
    @NotNull
    public static Class<?> needCraftClass(@NotNull final String className) {
        return (Class)Objects.requireNonNull((Object)findCraftClass(className), "Could not find org.bukkit.craftbukkit class " + className);
    }
    
    @Nullable
    public static String findNmsClassName(@NotNull final String className) {
        return isCraftBukkit() ? ("net.minecraft.server" + MinecraftReflection.VERSION + className) : null;
    }
    
    @Nullable
    public static Class<?> findNmsClass(@NotNull final String className) {
        final String nmsClassName = findNmsClassName(className);
        if (nmsClassName == null) {
            return null;
        }
        return findClass(nmsClassName);
    }
    
    @NotNull
    public static Class<?> needNmsClass(@NotNull final String className) {
        return (Class)Objects.requireNonNull((Object)findNmsClass(className), "Could not find net.minecraft.server class " + className);
    }
    
    @Nullable
    public static String findMcClassName(@NotNull final String className) {
        return isCraftBukkit() ? ("net.minecraft." + className) : null;
    }
    
    @Nullable
    public static Class<?> findMcClass(@NotNull final String... classNames) {
        for (final String clazz : classNames) {
            final String nmsClassName = findMcClassName(clazz);
            if (nmsClassName != null) {
                final Class<?> candidate = findClass(nmsClassName);
                if (candidate != null) {
                    return candidate;
                }
            }
        }
        return null;
    }
    
    @NotNull
    public static Class<?> needMcClass(@NotNull final String... className) {
        return (Class)Objects.requireNonNull((Object)findMcClass(className), "Could not find net.minecraft class from candidates" + Arrays.toString((Object[])className));
    }
    
    public static MethodHandles.Lookup lookup() {
        return MinecraftReflection.LOOKUP;
    }
    
    static {
        LOOKUP = MethodHandles.lookup();
        final Class<?> serverClass = Bukkit.getServer().getClass();
        if (!serverClass.getSimpleName().equals((Object)"CraftServer")) {
            VERSION = null;
        }
        else if (serverClass.getName().equals((Object)"org.bukkit.craftbukkit.CraftServer")) {
            VERSION = ".";
        }
        else {
            String name = serverClass.getName();
            name = name.substring("org.bukkit.craftbukkit".length());
            name = (VERSION = name.substring(0, name.length() - "CraftServer".length()));
        }
    }
}
