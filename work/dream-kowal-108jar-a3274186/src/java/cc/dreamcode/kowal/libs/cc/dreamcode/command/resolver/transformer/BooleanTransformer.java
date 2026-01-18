package cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer;

import java.util.Optional;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.builder.ListBuilder;
import java.util.List;

public class BooleanTransformer implements ObjectTransformer<Boolean>
{
    private final List<String> trueValues;
    private final List<String> falseValues;
    
    public BooleanTransformer() {
        this.trueValues = ListBuilder.of("true", "yes", "y", "1", "tak", "si");
        this.falseValues = ListBuilder.of("false", "no", "n", "0", "nie", "nah");
    }
    
    @Override
    public Class<?> getGeneric() {
        return Boolean.class;
    }
    
    @Override
    public Optional<Boolean> transform(@NonNull final Class<?> type, @NonNull final String input) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (input == null) {
            throw new NullPointerException("input is marked non-null but is null");
        }
        if (this.trueValues.contains((Object)input)) {
            return (Optional<Boolean>)Optional.of((Object)Boolean.TRUE);
        }
        if (this.falseValues.contains((Object)input)) {
            return (Optional<Boolean>)Optional.of((Object)Boolean.FALSE);
        }
        return (Optional<Boolean>)Optional.empty();
    }
}
