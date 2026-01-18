package cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer;

import java.util.Optional;
import lombok.NonNull;
import java.math.BigInteger;

public class BigIntegerTransformer implements ObjectTransformer<BigInteger>
{
    @Override
    public Class<?> getGeneric() {
        return BigInteger.class;
    }
    
    @Override
    public Optional<BigInteger> transform(@NonNull final Class<?> type, @NonNull final String input) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (input == null) {
            throw new NullPointerException("input is marked non-null but is null");
        }
        try {
            final BigInteger bigInteger = new BigInteger(input);
            return (Optional<BigInteger>)Optional.of((Object)bigInteger);
        }
        catch (final NumberFormatException e) {
            return (Optional<BigInteger>)Optional.empty();
        }
    }
}
