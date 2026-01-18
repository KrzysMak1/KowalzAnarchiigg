package cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsPair;

public abstract class BidirectionalTransformer<L, R>
{
    public abstract GenericsPair<L, R> getPair();
    
    public abstract R leftToRight(@NonNull final L data, @NonNull final SerdesContext serdesContext);
    
    public abstract L rightToLeft(@NonNull final R data, @NonNull final SerdesContext serdesContext);
    
    protected GenericsPair<L, R> generics(@NonNull final GenericsDeclaration from, @NonNull final GenericsDeclaration to) {
        if (from == null) {
            throw new NullPointerException("from is marked non-null but is null");
        }
        if (to == null) {
            throw new NullPointerException("to is marked non-null but is null");
        }
        return new GenericsPair<L, R>(from, to);
    }
    
    protected GenericsPair<L, R> genericsPair(@NonNull final Class<L> from, @NonNull final Class<R> to) {
        if (from == null) {
            throw new NullPointerException("from is marked non-null but is null");
        }
        if (to == null) {
            throw new NullPointerException("to is marked non-null but is null");
        }
        return new GenericsPair<L, R>(GenericsDeclaration.of(from), GenericsDeclaration.of(to));
    }
}
