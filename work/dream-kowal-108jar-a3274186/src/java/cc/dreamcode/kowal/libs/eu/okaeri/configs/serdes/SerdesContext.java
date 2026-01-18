package cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes;

import java.util.Optional;
import java.lang.annotation.Annotation;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.FieldDeclaration;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.configurer.Configurer;

public class SerdesContext
{
    @NonNull
    private final Configurer configurer;
    private final FieldDeclaration field;
    private final SerdesContextAttachments attachments;
    
    public static SerdesContext of(@NonNull final Configurer configurer) {
        if (configurer == null) {
            throw new NullPointerException("configurer is marked non-null but is null");
        }
        return of(configurer, null, new SerdesContextAttachments());
    }
    
    public static SerdesContext of(@NonNull final Configurer configurer, final FieldDeclaration field) {
        if (configurer == null) {
            throw new NullPointerException("configurer is marked non-null but is null");
        }
        return of(configurer, field, (field == null) ? new SerdesContextAttachments() : field.readStaticAnnotations(configurer));
    }
    
    public static SerdesContext of(@NonNull final Configurer configurer, final FieldDeclaration field, @NonNull final SerdesContextAttachments attachments) {
        if (configurer == null) {
            throw new NullPointerException("configurer is marked non-null but is null");
        }
        if (attachments == null) {
            throw new NullPointerException("attachments is marked non-null but is null");
        }
        return new SerdesContext(configurer, field, attachments);
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public <T extends Annotation> Optional<T> getConfigAnnotation(@NonNull final Class<T> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return (this.getConfigurer().getParent() == null) ? Optional.empty() : Optional.ofNullable(this.getConfigurer().getParent().getClass().getAnnotation(type));
    }
    
    public <T extends Annotation> Optional<T> getFieldAnnotation(@NonNull final Class<T> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return (this.getField() == null) ? Optional.empty() : this.getField().getAnnotation(type);
    }
    
    public <T extends SerdesContextAttachment> Optional<T> getAttachment(final Class<T> type) {
        final T attachment = this.attachments.get(type);
        return Optional.ofNullable(attachment);
    }
    
    public <T extends SerdesContextAttachment> T getAttachment(final Class<T> type, final T defaultValue) {
        return this.getAttachment(type).orElse(defaultValue);
    }
    
    @NonNull
    public Configurer getConfigurer() {
        return this.configurer;
    }
    
    public FieldDeclaration getField() {
        return this.field;
    }
    
    public SerdesContextAttachments getAttachments() {
        return this.attachments;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SerdesContext)) {
            return false;
        }
        final SerdesContext other = (SerdesContext)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$configurer = this.getConfigurer();
        final Object other$configurer = other.getConfigurer();
        Label_0065: {
            if (this$configurer == null) {
                if (other$configurer == null) {
                    break Label_0065;
                }
            }
            else if (this$configurer.equals(other$configurer)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$field = this.getField();
        final Object other$field = other.getField();
        Label_0102: {
            if (this$field == null) {
                if (other$field == null) {
                    break Label_0102;
                }
            }
            else if (this$field.equals(other$field)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$attachments = this.getAttachments();
        final Object other$attachments = other.getAttachments();
        if (this$attachments == null) {
            if (other$attachments == null) {
                return true;
            }
        }
        else if (this$attachments.equals(other$attachments)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SerdesContext;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $configurer = this.getConfigurer();
        result = result * 59 + (($configurer == null) ? 43 : $configurer.hashCode());
        final Object $field = this.getField();
        result = result * 59 + (($field == null) ? 43 : $field.hashCode());
        final Object $attachments = this.getAttachments();
        result = result * 59 + (($attachments == null) ? 43 : $attachments.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SerdesContext(configurer=" + (Object)this.getConfigurer() + ", field=" + (Object)this.getField() + ", attachments=" + (Object)this.getAttachments() + ")";
    }
    
    private SerdesContext(@NonNull final Configurer configurer, final FieldDeclaration field, final SerdesContextAttachments attachments) {
        if (configurer == null) {
            throw new NullPointerException("configurer is marked non-null but is null");
        }
        this.configurer = configurer;
        this.field = field;
        this.attachments = attachments;
    }
    
    private static class Builder
    {
        private final SerdesContextAttachments attachments;
        private Configurer configurer;
        private FieldDeclaration field;
        
        private Builder() {
            this.attachments = new SerdesContextAttachments();
        }
        
        public void configurer(final Configurer configurer) {
            this.configurer = configurer;
        }
        
        public void field(final FieldDeclaration field) {
            this.field = field;
        }
        
        public <A extends SerdesContextAttachment> Builder attach(final Class<A> type, final A attachment) {
            if (this.attachments.containsKey((Object)type)) {
                throw new IllegalArgumentException("cannot override SerdesContext attachment of type " + (Object)type);
            }
            this.attachments.put(type, attachment);
            return this;
        }
        
        public SerdesContext create() {
            return SerdesContext.of(this.configurer, this.field, this.attachments);
        }
    }
}
