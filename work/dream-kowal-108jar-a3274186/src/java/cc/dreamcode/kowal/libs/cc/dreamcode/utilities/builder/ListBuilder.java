package cc.dreamcode.kowal.libs.cc.dreamcode.utilities.builder;

import lombok.Generated;
import java.util.ArrayList;
import lombok.NonNull;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ListBuilder<T>
{
    private final List<T> list;
    
    public static <T> ListBuilder<T> builder() {
        return new ListBuilder<T>();
    }
    
    @SafeVarargs
    public static <T> List<T> of(final T... values) {
        return new ListBuilder<T>().addAll((java.util.Collection<? extends T>)Arrays.stream((Object[])values).collect(Collectors.toList())).build();
    }
    
    public ListBuilder<T> add(final T t) {
        this.list.add((Object)t);
        return this;
    }
    
    public ListBuilder<T> addAll(@NonNull final Collection<? extends T> list) {
        if (list == null) {
            throw new NullPointerException("list is marked non-null but is null");
        }
        this.list.addAll((Collection)list);
        return this;
    }
    
    public List<T> build() {
        return this.list;
    }
    
    @Generated
    public ListBuilder() {
        this.list = (List<T>)new ArrayList();
    }
}
