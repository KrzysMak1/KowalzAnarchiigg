package cc.dreamcode.kowal.libs.eu.okaeri.placeholders.context;

import java.util.Map;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.schema.meta.SchemaMeta;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.schema.resolver.PlaceholderResolver;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.schema.resolver.DefaultSchemaResolver;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.message.part.MessageFieldAccessor;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.message.part.MessageField;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.Placeholders;

public class Placeholder
{
    private final Object value;
    private Placeholders placeholders;
    private PlaceholderContext context;
    
    public static Placeholder of(@Nullable final Object value) {
        return new Placeholder(value);
    }
    
    public static Placeholder of(@Nullable final Placeholders placeholders, @Nullable final Object value) {
        return of(placeholders, value, null);
    }
    
    public static Placeholder of(@Nullable final Placeholders placeholders, @Nullable final Object value, @Nullable final PlaceholderContext context) {
        final Placeholder placeholder = new Placeholder(value);
        placeholder.setPlaceholders(placeholders);
        placeholder.setContext(context);
        return placeholder;
    }
    
    @Nullable
    public String render(@NonNull final MessageField field) {
        if (field == null) {
            throw new NullPointerException("field is marked non-null but is null");
        }
        return this.render(this.value, field);
    }
    
    @Nullable
    private String render(@Nullable Object object, @NonNull final MessageField field) {
        if (field == null) {
            throw new NullPointerException("field is marked non-null but is null");
        }
        if (object == null) {
            return null;
        }
        if (this.placeholders != null) {
            if (field.getSub() != null) {
                final MessageField fieldSub = field.getSub();
                final PlaceholderResolver resolver = this.placeholders.getResolver(object, fieldSub.getName());
                if (resolver == null) {
                    if (object.getClass().getAnnotation(cc.dreamcode.kowal.libs.eu.okaeri.placeholders.schema.annotation.Placeholder.class) != null) {
                        return this.renderUsingPlaceholderSchema(object, field);
                    }
                    return "<noresolver:" + field.getName() + "@" + fieldSub.getName() + ">";
                }
                else {
                    object = resolver.resolve(object, fieldSub, this.context);
                    if (fieldSub.hasSub()) {
                        return this.render(object, fieldSub);
                    }
                }
            }
            else {
                final PlaceholderResolver resolver2 = this.placeholders.getResolver(object, null);
                if (resolver2 != null) {
                    object = resolver2.resolve(object, field, this.context);
                }
            }
        }
        if (object == null) {
            return null;
        }
        if (DefaultSchemaResolver.INSTANCE.supports(object.getClass())) {
            return DefaultSchemaResolver.INSTANCE.resolve(object, field);
        }
        if (object.getClass().getAnnotation(cc.dreamcode.kowal.libs.eu.okaeri.placeholders.schema.annotation.Placeholder.class) != null) {
            return this.renderUsingPlaceholderSchema(object, field);
        }
        return "<norenderer:" + field.getLastSubPath() + "(" + object.getClass().getSimpleName() + ")>";
    }
    
    private String renderUsingPlaceholderSchema(@NonNull final Object object, @NonNull final MessageField field) {
        if (object == null) {
            throw new NullPointerException("object is marked non-null but is null");
        }
        if (field == null) {
            throw new NullPointerException("field is marked non-null but is null");
        }
        final SchemaMeta meta = SchemaMeta.of(object.getClass());
        if (field.getSub() == null) {
            throw new RuntimeException("rendering PlaceholderSchema itself not supported at the moment");
        }
        final MessageField fieldSub = field.getSub();
        final Map<String, PlaceholderResolver> placeholders = meta.getPlaceholders();
        final PlaceholderResolver resolver = (PlaceholderResolver)placeholders.get((Object)fieldSub.getName());
        if (resolver == null) {
            throw new RuntimeException("resolver cannot be null: " + fieldSub.getName());
        }
        final Object resolved = resolver.resolve(object, fieldSub, this.context);
        return this.render(resolved, fieldSub);
    }
    
    public Object getValue() {
        return this.value;
    }
    
    public Placeholders getPlaceholders() {
        return this.placeholders;
    }
    
    public PlaceholderContext getContext() {
        return this.context;
    }
    
    public void setPlaceholders(final Placeholders placeholders) {
        this.placeholders = placeholders;
    }
    
    public void setContext(final PlaceholderContext context) {
        this.context = context;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Placeholder)) {
            return false;
        }
        final Placeholder other = (Placeholder)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$value = this.getValue();
        final Object other$value = other.getValue();
        Label_0065: {
            if (this$value == null) {
                if (other$value == null) {
                    break Label_0065;
                }
            }
            else if (this$value.equals(other$value)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$placeholders = this.getPlaceholders();
        final Object other$placeholders = other.getPlaceholders();
        Label_0102: {
            if (this$placeholders == null) {
                if (other$placeholders == null) {
                    break Label_0102;
                }
            }
            else if (this$placeholders.equals(other$placeholders)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$context = this.getContext();
        final Object other$context = other.getContext();
        if (this$context == null) {
            if (other$context == null) {
                return true;
            }
        }
        else if (this$context.equals(other$context)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Placeholder;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $value = this.getValue();
        result = result * 59 + (($value == null) ? 43 : $value.hashCode());
        final Object $placeholders = this.getPlaceholders();
        result = result * 59 + (($placeholders == null) ? 43 : $placeholders.hashCode());
        final Object $context = this.getContext();
        result = result * 59 + (($context == null) ? 43 : $context.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Placeholder(value=" + this.getValue() + ", placeholders=" + (Object)this.getPlaceholders() + ")";
    }
    
    private Placeholder(final Object value) {
        this.value = value;
    }
}
