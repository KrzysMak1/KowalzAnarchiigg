package cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver;

import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array.ShortArrayTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array.LongArrayTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array.DoubleArrayTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array.IntegerArrayTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array.FloatArrayTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array.DurationArrayTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array.CharacterArrayTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array.ByteArrayTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array.BooleanArrayTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array.BigIntegerArrayTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array.BigDecimalArrayTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array.ArrayTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array.StringArrayTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.ShortTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.LongTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.DoubleTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.IntegerTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.FloatTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.EnumTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.DurationTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.CharacterTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.ByteTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.BooleanTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.BigIntegerTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.BigDecimalTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.ObjectTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.StringTransformer;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandProvider;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandExtension;

public class DefaultTransformers implements CommandExtension
{
    @Override
    public void register(@NonNull final CommandProvider commandProvider) {
        if (commandProvider == null) {
            throw new NullPointerException("commandProvider is marked non-null but is null");
        }
        commandProvider.registerTransformer(new StringTransformer());
        commandProvider.registerTransformer(new BigDecimalTransformer());
        commandProvider.registerTransformer(new BigIntegerTransformer());
        commandProvider.registerTransformer(new BooleanTransformer());
        commandProvider.registerAssignableClass(Boolean.class, Boolean.TYPE);
        commandProvider.registerTransformer(new ByteTransformer());
        commandProvider.registerAssignableClass(Byte.class, Byte.TYPE);
        commandProvider.registerTransformer(new CharacterTransformer());
        commandProvider.registerAssignableClass(Character.class, Character.TYPE);
        commandProvider.registerTransformer(new DurationTransformer());
        commandProvider.registerTransformer(new EnumTransformer());
        commandProvider.registerTransformer(new FloatTransformer());
        commandProvider.registerAssignableClass(Float.class, Float.TYPE);
        commandProvider.registerTransformer(new IntegerTransformer());
        commandProvider.registerAssignableClass(Integer.class, Integer.TYPE);
        commandProvider.registerTransformer(new DoubleTransformer());
        commandProvider.registerAssignableClass(Double.class, Double.TYPE);
        commandProvider.registerTransformer(new LongTransformer());
        commandProvider.registerAssignableClass(Long.class, Long.TYPE);
        commandProvider.registerTransformer(new ShortTransformer());
        commandProvider.registerAssignableClass(Short.class, Short.TYPE);
        commandProvider.registerTransformer(new StringArrayTransformer());
        commandProvider.registerTransformer(new BigDecimalArrayTransformer());
        commandProvider.registerTransformer(new BigIntegerArrayTransformer());
        commandProvider.registerTransformer(new BooleanArrayTransformer());
        commandProvider.registerAssignableClass(Boolean[].class, boolean[].class);
        commandProvider.registerTransformer(new ByteArrayTransformer());
        commandProvider.registerAssignableClass(Byte[].class, byte[].class);
        commandProvider.registerTransformer(new CharacterArrayTransformer());
        commandProvider.registerAssignableClass(Character[].class, char[].class);
        commandProvider.registerTransformer(new DurationArrayTransformer());
        commandProvider.registerTransformer(new FloatArrayTransformer());
        commandProvider.registerAssignableClass(Float[].class, float[].class);
        commandProvider.registerTransformer(new IntegerArrayTransformer());
        commandProvider.registerAssignableClass(Integer[].class, int[].class);
        commandProvider.registerTransformer(new DoubleArrayTransformer());
        commandProvider.registerAssignableClass(Double[].class, double[].class);
        commandProvider.registerTransformer(new LongArrayTransformer());
        commandProvider.registerAssignableClass(Long[].class, long[].class);
        commandProvider.registerTransformer(new ShortArrayTransformer());
        commandProvider.registerAssignableClass(Short[].class, short[].class);
    }
}
