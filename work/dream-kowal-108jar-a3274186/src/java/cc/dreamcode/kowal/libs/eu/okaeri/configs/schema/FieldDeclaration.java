package cc.dreamcode.kowal.libs.eu.okaeri.configs.schema;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.annotation.NameStrategy;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.annotation.Names;
import java.util.Locale;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.annotation.NameModifier;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.annotation.CustomKey;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.annotation.Exclude;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesAnnotationResolver;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesContextAttachment;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesContextAttachments;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.configurer.Configurer;
import java.util.Optional;
import java.lang.annotation.Annotation;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.exception.OkaeriException;
import java.lang.reflect.Modifier;
import java.util.List;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.annotation.Comment;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.annotation.Comments;
import lombok.NonNull;
import java.lang.reflect.Field;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.annotation.Variable;
import java.util.Set;
import java.util.Map;
import java.util.logging.Logger;

public class FieldDeclaration
{
    private static final Logger LOGGER;
    private static final Map<CacheEntry, FieldDeclaration> DECLARATION_CACHE;
    private static final Set<String> FINAL_WARNS;
    private Object startingValue;
    private String name;
    private String[] comment;
    private GenericsDeclaration type;
    private Variable variable;
    private boolean variableHide;
    private Field field;
    private Object object;
    
    public static FieldDeclaration of(@NonNull final ConfigDeclaration config, @NonNull final Field field, final Object object) {
        try {
            if (config == null) {
                throw new NullPointerException("config is marked non-null but is null");
            }
            if (field == null) {
                throw new NullPointerException("field is marked non-null but is null");
            }
            final CacheEntry cache = new CacheEntry(config.getType(), field.getName());
            final FieldDeclaration template = (FieldDeclaration)FieldDeclaration.DECLARATION_CACHE.computeIfAbsent((Object)cache, entry -> {
                final FieldDeclaration declaration = new FieldDeclaration();
                field.setAccessible(true);
                if (field.getAnnotation((Class)Exclude.class) != null) {
                    return null;
                }
                if (Modifier.isTransient(field.getModifiers())) {
                    return null;
                }
                final CustomKey customKey = (CustomKey)field.getAnnotation((Class)CustomKey.class);
                if (customKey != null) {
                    declaration.setName("".equals((Object)customKey.value()) ? field.getName() : customKey.value());
                }
                else if (config.getNameStrategy() != null) {
                    final Names nameStrategy = config.getNameStrategy();
                    final NameStrategy strategy = nameStrategy.strategy();
                    final NameModifier modifier = nameStrategy.modifier();
                    String name = strategy.getRegex().matcher((CharSequence)field.getName()).replaceAll(strategy.getReplacement());
                    if (modifier == NameModifier.TO_UPPER_CASE) {
                        name = name.toUpperCase(Locale.ROOT);
                    }
                    else if (modifier == NameModifier.TO_LOWER_CASE) {
                        name = name.toLowerCase(Locale.ROOT);
                    }
                    declaration.setName(name);
                }
                else {
                    declaration.setName(field.getName());
                }
                final Variable variable = (Variable)field.getAnnotation((Class)Variable.class);
                declaration.setVariable(variable);
                declaration.setComment(readComments(field));
                declaration.setType(GenericsDeclaration.of(field.getGenericType()));
                declaration.setField(field);
                return declaration;
            });
            if (template == null) {
                return null;
            }
            final FieldDeclaration declaration = new FieldDeclaration();
            final Object startingValue = (object == null) ? null : template.getField().get(object);
            declaration.setStartingValue(startingValue);
            declaration.setName(template.getName());
            declaration.setComment(template.getComment());
            declaration.setType(template.getType());
            declaration.setVariable(template.getVariable());
            declaration.setField(template.getField());
            declaration.setObject(object);
            return declaration;
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    private static String[] readComments(final Field field) {
        final Comments comments = (Comments)field.getAnnotation((Class)Comments.class);
        if (comments != null) {
            final List<String> commentList = (List<String>)new ArrayList();
            for (final Comment comment : comments.value()) {
                commentList.addAll((Collection)Arrays.asList((Object[])comment.value()));
            }
            return (String[])commentList.toArray((Object[])new String[0]);
        }
        final Comment comment2 = (Comment)field.getAnnotation((Class)Comment.class);
        if (comment2 != null) {
            return comment2.value();
        }
        return null;
    }
    
    public void updateValue(final Object value) throws OkaeriException {
        try {
            if (Modifier.isFinal(this.getField().getModifiers()) && FieldDeclaration.FINAL_WARNS.add((Object)this.getField().toString())) {
                FieldDeclaration.LOGGER.warning((Object)this.getField() + ": final fields (especially with default value) may prevent loading of the data. Removal of the final modifier is strongly advised.");
            }
            this.getField().setAccessible(true);
            this.getField().set(this.getObject(), value);
        }
        catch (final IllegalAccessException exception) {
            throw new OkaeriException("failed to #updateValue", (Throwable)exception);
        }
    }
    
    public Object getValue() throws OkaeriException {
        if (this.isVariableHide()) {
            return this.getStartingValue();
        }
        try {
            this.getField().setAccessible(true);
            return this.getField().get(this.getObject());
        }
        catch (final IllegalAccessException exception) {
            throw new OkaeriException("failed to #getValue", (Throwable)exception);
        }
    }
    
    public <T extends Annotation> Optional<T> getAnnotation(@NonNull final Class<T> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return (Optional<T>)Optional.ofNullable((Object)this.getField().getAnnotation((Class)type));
    }
    
    public SerdesContextAttachments readStaticAnnotations(@NonNull final Configurer configurer) {
        if (configurer == null) {
            throw new NullPointerException("configurer is marked non-null but is null");
        }
        final SerdesContextAttachments attachments = new SerdesContextAttachments();
        for (final Annotation annotation : this.getField().getAnnotations()) {
            final SerdesAnnotationResolver<Annotation, SerdesContextAttachment> annotationResolver = configurer.getRegistry().getAnnotationResolver(annotation);
            if (annotationResolver != null) {
                final Optional<? extends SerdesContextAttachment> attachmentOptional = annotationResolver.resolveAttachment(this.getField(), annotation);
                if (attachmentOptional.isPresent()) {
                    final SerdesContextAttachment attachment = (SerdesContextAttachment)attachmentOptional.get();
                    final Class<? extends SerdesContextAttachment> attachmentType = attachment.getClass();
                    attachments.put(attachmentType, attachment);
                }
            }
        }
        return attachments;
    }
    
    public Object getStartingValue() {
        return this.startingValue;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String[] getComment() {
        return this.comment;
    }
    
    public GenericsDeclaration getType() {
        return this.type;
    }
    
    public Variable getVariable() {
        return this.variable;
    }
    
    public boolean isVariableHide() {
        return this.variableHide;
    }
    
    public Field getField() {
        return this.field;
    }
    
    public Object getObject() {
        return this.object;
    }
    
    public void setStartingValue(final Object startingValue) {
        this.startingValue = startingValue;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setComment(final String[] comment) {
        this.comment = comment;
    }
    
    public void setType(final GenericsDeclaration type) {
        this.type = type;
    }
    
    public void setVariable(final Variable variable) {
        this.variable = variable;
    }
    
    public void setVariableHide(final boolean variableHide) {
        this.variableHide = variableHide;
    }
    
    public void setField(final Field field) {
        this.field = field;
    }
    
    public void setObject(final Object object) {
        this.object = object;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FieldDeclaration)) {
            return false;
        }
        final FieldDeclaration other = (FieldDeclaration)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isVariableHide() != other.isVariableHide()) {
            return false;
        }
        final Object this$startingValue = this.getStartingValue();
        final Object other$startingValue = other.getStartingValue();
        Label_0078: {
            if (this$startingValue == null) {
                if (other$startingValue == null) {
                    break Label_0078;
                }
            }
            else if (this$startingValue.equals(other$startingValue)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0115: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0115;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0115;
            }
            return false;
        }
        if (!Arrays.deepEquals((Object[])this.getComment(), (Object[])other.getComment())) {
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        Label_0168: {
            if (this$type == null) {
                if (other$type == null) {
                    break Label_0168;
                }
            }
            else if (this$type.equals(other$type)) {
                break Label_0168;
            }
            return false;
        }
        final Object this$variable = this.getVariable();
        final Object other$variable = other.getVariable();
        Label_0205: {
            if (this$variable == null) {
                if (other$variable == null) {
                    break Label_0205;
                }
            }
            else if (this$variable.equals(other$variable)) {
                break Label_0205;
            }
            return false;
        }
        final Object this$field = this.getField();
        final Object other$field = other.getField();
        Label_0242: {
            if (this$field == null) {
                if (other$field == null) {
                    break Label_0242;
                }
            }
            else if (this$field.equals(other$field)) {
                break Label_0242;
            }
            return false;
        }
        final Object this$object = this.getObject();
        final Object other$object = other.getObject();
        if (this$object == null) {
            if (other$object == null) {
                return true;
            }
        }
        else if (this$object.equals(other$object)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof FieldDeclaration;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isVariableHide() ? 79 : 97);
        final Object $startingValue = this.getStartingValue();
        result = result * 59 + (($startingValue == null) ? 43 : $startingValue.hashCode());
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + Arrays.deepHashCode((Object[])this.getComment());
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final Object $variable = this.getVariable();
        result = result * 59 + (($variable == null) ? 43 : $variable.hashCode());
        final Object $field = this.getField();
        result = result * 59 + (($field == null) ? 43 : $field.hashCode());
        final Object $object = this.getObject();
        result = result * 59 + (($object == null) ? 43 : $object.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "FieldDeclaration(startingValue=" + this.getStartingValue() + ", name=" + this.getName() + ", comment=" + Arrays.deepToString((Object[])this.getComment()) + ", type=" + (Object)this.getType() + ", variable=" + (Object)this.getVariable() + ", variableHide=" + this.isVariableHide() + ", field=" + (Object)this.getField() + ", object=" + this.getObject() + ")";
    }
    
    static {
        LOGGER = Logger.getLogger(FieldDeclaration.class.getSimpleName());
        DECLARATION_CACHE = (Map)new ConcurrentHashMap();
        FINAL_WARNS = (Set)new HashSet();
    }
    
    private static class CacheEntry
    {
        private final Class<?> type;
        private final String fieldName;
        
        public Class<?> getType() {
            return this.type;
        }
        
        public String getFieldName() {
            return this.fieldName;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof CacheEntry)) {
                return false;
            }
            final CacheEntry other = (CacheEntry)o;
            if (!other.canEqual(this)) {
                return false;
            }
            final Object this$type = this.getType();
            final Object other$type = other.getType();
            Label_0065: {
                if (this$type == null) {
                    if (other$type == null) {
                        break Label_0065;
                    }
                }
                else if (this$type.equals(other$type)) {
                    break Label_0065;
                }
                return false;
            }
            final Object this$fieldName = this.getFieldName();
            final Object other$fieldName = other.getFieldName();
            if (this$fieldName == null) {
                if (other$fieldName == null) {
                    return true;
                }
            }
            else if (this$fieldName.equals(other$fieldName)) {
                return true;
            }
            return false;
        }
        
        protected boolean canEqual(final Object other) {
            return other instanceof CacheEntry;
        }
        
        @Override
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $type = this.getType();
            result = result * 59 + (($type == null) ? 43 : $type.hashCode());
            final Object $fieldName = this.getFieldName();
            result = result * 59 + (($fieldName == null) ? 43 : $fieldName.hashCode());
            return result;
        }
        
        @Override
        public String toString() {
            return "FieldDeclaration.CacheEntry(type=" + (Object)this.getType() + ", fieldName=" + this.getFieldName() + ")";
        }
        
        public CacheEntry(final Class<?> type, final String fieldName) {
            this.type = type;
            this.fieldName = fieldName;
        }
    }
}
