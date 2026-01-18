package cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer;

import java.util.Optional;
import lombok.NonNull;
import java.math.BigDecimal;

public class BigDecimalTransformer implements ObjectTransformer<BigDecimal>
{
    @Override
    public Class<?> getGeneric() {
        return BigDecimal.class;
    }
    
    @Override
    public Optional<BigDecimal> transform(@NonNull final Class<?> type, @NonNull final String input) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (input == null) {
            throw new NullPointerException("input is marked non-null but is null");
        }
        try {
            final BigDecimal bigDecimal = new BigDecimal(input);
            return (Optional<BigDecimal>)Optional.of((Object)bigDecimal);
        }
        catch (final NumberFormatException e) {
            return (Optional<BigDecimal>)Optional.empty();
        }
    }
}
