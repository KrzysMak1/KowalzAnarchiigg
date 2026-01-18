package cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsPair;

public abstract class ObjectTransformer<S, D>
{
    public abstract GenericsPair<S, D> getPair();
    
    public abstract D transform(final S data, final SerdesContext context);
    
    protected GenericsPair<S, D> genericsPair(@NonNull final Class<S> from, @NonNull final Class<D> to) {
        if (from == null) {
            throw new NullPointerException("from is marked non-null but is null");
        }
        if (to == null) {
            throw new NullPointerException("to is marked non-null but is null");
        }
        return new GenericsPair<S, D>(GenericsDeclaration.of(from), GenericsDeclaration.of(to));
    }
}
