package cc.dreamcode.kowal.libs.eu.okaeri.placeholders.context;

import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.message.part.MessageStatic;
import java.util.Iterator;
import java.util.List;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.message.part.MessageElement;
import java.util.LinkedHashMap;
import java.util.Collections;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.message.part.MessageField;
import org.jetbrains.annotations.Nullable;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.Placeholders;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.message.CompiledMessage;
import java.util.Map;

public class PlaceholderContext
{
    private final Map<String, Placeholder> fields;
    private final CompiledMessage message;
    private final FailMode failMode;
    private Placeholders placeholders;
    
    public static PlaceholderContext create() {
        return create(FailMode.FAIL_SAFE);
    }
    
    public static PlaceholderContext create(@NonNull final FailMode failMode) {
        if (failMode == null) {
            throw new NullPointerException("failMode is marked non-null but is null");
        }
        return new PlaceholderContext(null, failMode);
    }
    
    public static PlaceholderContext of(@NonNull final CompiledMessage message) {
        if (message == null) {
            throw new NullPointerException("message is marked non-null but is null");
        }
        return of(null, message);
    }
    
    public static PlaceholderContext of(@Nullable final Placeholders placeholders, @NonNull final CompiledMessage message) {
        if (message == null) {
            throw new NullPointerException("message is marked non-null but is null");
        }
        return of(placeholders, message, FailMode.FAIL_SAFE);
    }
    
    public static PlaceholderContext of(@Nullable final Placeholders placeholders, @NonNull final CompiledMessage message, @NonNull final FailMode failMode) {
        if (message == null) {
            throw new NullPointerException("message is marked non-null but is null");
        }
        if (failMode == null) {
            throw new NullPointerException("failMode is marked non-null but is null");
        }
        final PlaceholderContext context = new PlaceholderContext(message, failMode);
        context.setPlaceholders(placeholders);
        return context;
    }
    
    public PlaceholderContext with(@NonNull final String field, @Nullable final Object value) {
        if (field == null) {
            throw new NullPointerException("field is marked non-null but is null");
        }
        if (this.placeholders != null && this.placeholders.isFastMode() && this.message != null && (!this.message.isWithFields() || !this.message.hasField(field))) {
            return this;
        }
        this.fields.put((Object)field, (Object)Placeholder.of(this.placeholders, value, this));
        return this;
    }
    
    public PlaceholderContext with(@NonNull final Map<String, Object> fields) {
        if (fields == null) {
            throw new NullPointerException("fields is marked non-null but is null");
        }
        fields.forEach(this::with);
        return this;
    }
    
    public Map<MessageField, String> renderFields() {
        return this.renderFields(this.message);
    }
    
    public Map<MessageField, String> renderFields(@NonNull final CompiledMessage message) {
        if (message == null) {
            throw new NullPointerException("message is marked non-null but is null");
        }
        if (message != this.message && this.message != null) {
            throw new IllegalArgumentException("cannot apply another message for context created with prepacked message: if you intended to use this context as shared please use empty context from #create(), if you're just trying to send a message use of(message)");
        }
        if (!message.isWithFields()) {
            return (Map<MessageField, String>)Collections.emptyMap();
        }
        final String state = message.getRaw();
        final List<MessageElement> parts = message.getParts();
        final Map<MessageField, String> rendered = (Map<MessageField, String>)new LinkedHashMap();
        for (final MessageElement part : parts) {
            if (!(part instanceof MessageField)) {
                continue;
            }
            final MessageField field = (MessageField)part;
            final String name = field.getName();
            final String alreadyRendered = (String)rendered.get((Object)field);
            if (alreadyRendered != null) {
                continue;
            }
            Placeholder placeholder = (Placeholder)this.fields.get((Object)name);
            if (placeholder == null || placeholder.getValue() == null) {
                if (field.getDefaultValue() != null) {
                    placeholder = Placeholder.of(null, field.getDefaultValue(), this);
                }
                else {
                    if (this.failMode == FailMode.FAIL_FAST) {
                        throw new IllegalArgumentException("missing placeholder '" + name + "' for message '" + state + "'");
                    }
                    if (this.failMode != FailMode.FAIL_SAFE) {
                        throw new RuntimeException("unknown fail mode: " + (Object)this.failMode);
                    }
                    placeholder = Placeholder.of(null, "<missing:" + field.getLastSubPath() + ">", this);
                }
            }
            String render = placeholder.render(field);
            if (render == null) {
                if (field.getDefaultValue() != null) {
                    render = field.getDefaultValue();
                }
                else {
                    if (this.failMode == FailMode.FAIL_FAST) {
                        throw new IllegalArgumentException("rendered null for placeholder '" + name + "' for message '" + state + "'");
                    }
                    if (this.failMode != FailMode.FAIL_SAFE) {
                        throw new RuntimeException("unknown fail mode: " + (Object)this.failMode);
                    }
                    render = "<null:" + field.getLastSubPath() + ">";
                }
            }
            rendered.put((Object)field, (Object)render);
        }
        return rendered;
    }
    
    public String apply() {
        return this.apply(this.message);
    }
    
    public String apply(@NonNull final CompiledMessage message) {
        if (message == null) {
            throw new NullPointerException("message is marked non-null but is null");
        }
        final List<MessageElement> parts = message.getParts();
        final Map<MessageField, String> rendered = this.renderFields(message);
        final StringBuilder builder = new StringBuilder();
        for (final MessageElement part : parts) {
            if (part instanceof MessageStatic) {
                builder.append(((MessageStatic)part).getValue());
            }
            else {
                if (!(part instanceof MessageField)) {
                    throw new IllegalArgumentException("unknown message part: " + (Object)part);
                }
                final MessageField field = (MessageField)part;
                final String render = (String)rendered.get((Object)field);
                builder.append(render);
            }
        }
        return builder.toString();
    }
    
    public PlaceholderContext(final CompiledMessage message, final FailMode failMode) {
        this.fields = (Map<String, Placeholder>)new LinkedHashMap();
        this.message = message;
        this.failMode = failMode;
    }
    
    public Map<String, Placeholder> getFields() {
        return this.fields;
    }
    
    public CompiledMessage getMessage() {
        return this.message;
    }
    
    public FailMode getFailMode() {
        return this.failMode;
    }
    
    public Placeholders getPlaceholders() {
        return this.placeholders;
    }
    
    public void setPlaceholders(final Placeholders placeholders) {
        this.placeholders = placeholders;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlaceholderContext)) {
            return false;
        }
        final PlaceholderContext other = (PlaceholderContext)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$fields = this.getFields();
        final Object other$fields = other.getFields();
        Label_0065: {
            if (this$fields == null) {
                if (other$fields == null) {
                    break Label_0065;
                }
            }
            else if (this$fields.equals(other$fields)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$message = this.getMessage();
        final Object other$message = other.getMessage();
        Label_0102: {
            if (this$message == null) {
                if (other$message == null) {
                    break Label_0102;
                }
            }
            else if (this$message.equals(other$message)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$failMode = this.getFailMode();
        final Object other$failMode = other.getFailMode();
        Label_0139: {
            if (this$failMode == null) {
                if (other$failMode == null) {
                    break Label_0139;
                }
            }
            else if (this$failMode.equals(other$failMode)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$placeholders = this.getPlaceholders();
        final Object other$placeholders = other.getPlaceholders();
        if (this$placeholders == null) {
            if (other$placeholders == null) {
                return true;
            }
        }
        else if (this$placeholders.equals(other$placeholders)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PlaceholderContext;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $fields = this.getFields();
        result = result * 59 + (($fields == null) ? 43 : $fields.hashCode());
        final Object $message = this.getMessage();
        result = result * 59 + (($message == null) ? 43 : $message.hashCode());
        final Object $failMode = this.getFailMode();
        result = result * 59 + (($failMode == null) ? 43 : $failMode.hashCode());
        final Object $placeholders = this.getPlaceholders();
        result = result * 59 + (($placeholders == null) ? 43 : $placeholders.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "PlaceholderContext(fields=" + (Object)this.getFields() + ", message=" + (Object)this.getMessage() + ", failMode=" + (Object)this.getFailMode() + ", placeholders=" + (Object)this.getPlaceholders() + ")";
    }
}
