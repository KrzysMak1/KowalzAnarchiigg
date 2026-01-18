package cc.dreamcode.kowal.libs.net.kyori.adventure.key;

import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.Arrays;
import java.nio.charset.StandardCharsets;
import java.util.OptionalInt;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.intellij.lang.annotations.RegExp;
import java.util.Comparator;

final class KeyImpl implements Key
{
    static final Comparator<? super Key> COMPARATOR;
    @RegExp
    static final String NAMESPACE_PATTERN = "[a-z0-9_\\-.]+";
    @RegExp
    static final String VALUE_PATTERN = "[a-z0-9_\\-./]+";
    private final String namespace;
    private final String value;
    
    KeyImpl(@NotNull final String namespace, @NotNull final String value) {
        checkError("namespace", namespace, value, Key.checkNamespace(namespace));
        checkError("value", namespace, value, Key.checkValue(value));
        this.namespace = (String)Objects.requireNonNull((Object)namespace, "namespace");
        this.value = (String)Objects.requireNonNull((Object)value, "value");
    }
    
    private static void checkError(final String name, final String namespace, final String value, final OptionalInt index) {
        if (index.isPresent()) {
            final int indexValue = index.getAsInt();
            final char character = value.charAt(indexValue);
            throw new InvalidKeyException(namespace, value, String.format("Non [a-z0-9_.-] character in %s of Key[%s] at index %d ('%s', bytes: %s)", new Object[] { name, asString(namespace, value), indexValue, character, Arrays.toString(String.valueOf(character).getBytes(StandardCharsets.UTF_8)) }));
        }
    }
    
    static boolean allowedInNamespace(final char character) {
        return character == '_' || character == '-' || (character >= 'a' && character <= 'z') || (character >= '0' && character <= '9') || character == '.';
    }
    
    static boolean allowedInValue(final char character) {
        return character == '_' || character == '-' || (character >= 'a' && character <= 'z') || (character >= '0' && character <= '9') || character == '.' || character == '/';
    }
    
    @NotNull
    @Override
    public String namespace() {
        return this.namespace;
    }
    
    @NotNull
    @Override
    public String value() {
        return this.value;
    }
    
    @NotNull
    @Override
    public String asString() {
        return asString(this.namespace, this.value);
    }
    
    @NotNull
    private static String asString(@NotNull final String namespace, @NotNull final String value) {
        return namespace + ':' + value;
    }
    
    @NotNull
    @Override
    public String toString() {
        return this.asString();
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object[])new ExaminableProperty[] { ExaminableProperty.of("namespace", this.namespace), ExaminableProperty.of("value", this.value) });
    }
    
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Key)) {
            return false;
        }
        final Key that = (Key)other;
        return Objects.equals((Object)this.namespace, (Object)that.namespace()) && Objects.equals((Object)this.value, (Object)that.value());
    }
    
    @Override
    public int hashCode() {
        int result = this.namespace.hashCode();
        result = 31 * result + this.value.hashCode();
        return result;
    }
    
    @Override
    public int compareTo(@NotNull final Key that) {
        return super.compareTo(that);
    }
    
    static {
        COMPARATOR = Comparator.comparing(Key::value).thenComparing(Key::namespace);
    }
}
