package cc.dreamcode.kowal.libs.eu.okaeri.placeholders.message;

import org.jetbrains.annotations.Nullable;
import java.util.regex.Matcher;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.message.part.MessageStatic;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Iterator;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.message.part.MessageField;
import java.util.HashSet;
import lombok.NonNull;
import java.util.Set;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.message.part.MessageElement;
import java.util.List;
import java.util.regex.Pattern;

public class CompiledMessage
{
    private static final Pattern FIELD_PATTERN;
    private final String raw;
    private final List<MessageElement> parts;
    private final Set<String> usedFields;
    
    public static CompiledMessage of(@NonNull final String raw, @NonNull final List<MessageElement> parts) {
        if (raw == null) {
            throw new NullPointerException("raw is marked non-null but is null");
        }
        if (parts == null) {
            throw new NullPointerException("parts is marked non-null but is null");
        }
        final Set<String> usedFields = (Set<String>)new HashSet();
        for (final MessageElement part : parts) {
            if (!(part instanceof MessageField)) {
                continue;
            }
            final MessageField field = (MessageField)part;
            final String fieldName = field.getName();
            usedFields.add((Object)fieldName);
            usedFields.add((Object)fieldName.split("(\\.|\\()", 2)[0]);
        }
        return new CompiledMessage(raw, parts, usedFields);
    }
    
    public static CompiledMessage of(@NonNull final String source) {
        if (source == null) {
            throw new NullPointerException("source is marked non-null but is null");
        }
        return of(Locale.ENGLISH, source);
    }
    
    public static CompiledMessage of(@NonNull final Locale locale, @NonNull final String source) {
        if (locale == null) {
            throw new NullPointerException("locale is marked non-null but is null");
        }
        if (source == null) {
            throw new NullPointerException("source is marked non-null but is null");
        }
        if (source.isEmpty()) {
            return new CompiledMessage(source, (List<MessageElement>)Collections.emptyList(), (Set<String>)Collections.emptySet());
        }
        final Matcher matcher = CompiledMessage.FIELD_PATTERN.matcher((CharSequence)source);
        final List<MessageElement> parts = (List<MessageElement>)new ArrayList();
        final Set<String> usedFields = (Set<String>)new HashSet();
        int lastIndex = 0;
        final int rawLength = source.length();
        int fieldsLength = 0;
        while (matcher.find()) {
            parts.add((Object)MessageStatic.of(source.substring(lastIndex, matcher.start())));
            final String content = matcher.group("content");
            final String[] fieldElements = parseFieldToArray(content);
            final String metaElement = fieldElements[0];
            final String fieldName = fieldElements[1];
            final String defaultValue = fieldElements[2];
            final MessageField messageField = MessageField.of(locale, fieldName);
            messageField.setDefaultValue(defaultValue);
            messageField.setMetadataRaw(metaElement);
            messageField.setRaw(content);
            parts.add((Object)messageField);
            usedFields.add((Object)fieldName);
            usedFields.add((Object)fieldName.split("(\\.|\\()", 2)[0]);
            lastIndex = matcher.end();
            fieldsLength += matcher.group().length();
        }
        if (lastIndex != source.length()) {
            parts.add((Object)MessageStatic.of(source.substring(lastIndex)));
        }
        final boolean withFields = fieldsLength > 0;
        if (!withFields && parts.size() > 1) {
            throw new RuntimeException("noticed message without fields with more than one element: " + (Object)parts);
        }
        return new CompiledMessage(source, (List<MessageElement>)Collections.unmodifiableList((List)parts), (Set<String>)Collections.unmodifiableSet((Set)usedFields));
    }
    
    private static String[] parseFieldToArray(@NonNull String raw) {
        if (raw == null) {
            throw new NullPointerException("raw is marked non-null but is null");
        }
        final String[] arr = new String[3];
        final int commentIndex = raw.indexOf("#");
        if (commentIndex != -1) {
            arr[0] = raw.substring(0, commentIndex);
            raw = raw.substring(commentIndex + 1);
        }
        final int fallbackIndex = raw.lastIndexOf("|");
        final int argumentsEndIndex = raw.lastIndexOf(")");
        if (fallbackIndex != -1 && fallbackIndex > argumentsEndIndex) {
            arr[2] = raw.substring(fallbackIndex + 1);
            raw = raw.substring(0, fallbackIndex);
        }
        arr[1] = raw;
        return arr;
    }
    
    public boolean hasField(@Nullable final String name) {
        return this.usedFields.contains((Object)name);
    }
    
    public boolean isWithFields() {
        return !this.usedFields.isEmpty();
    }
    
    public String getRaw() {
        return this.raw;
    }
    
    public List<MessageElement> getParts() {
        return this.parts;
    }
    
    public Set<String> getUsedFields() {
        return this.usedFields;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CompiledMessage)) {
            return false;
        }
        final CompiledMessage other = (CompiledMessage)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$raw = this.getRaw();
        final Object other$raw = other.getRaw();
        Label_0065: {
            if (this$raw == null) {
                if (other$raw == null) {
                    break Label_0065;
                }
            }
            else if (this$raw.equals(other$raw)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$parts = this.getParts();
        final Object other$parts = other.getParts();
        Label_0102: {
            if (this$parts == null) {
                if (other$parts == null) {
                    break Label_0102;
                }
            }
            else if (this$parts.equals(other$parts)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$usedFields = this.getUsedFields();
        final Object other$usedFields = other.getUsedFields();
        if (this$usedFields == null) {
            if (other$usedFields == null) {
                return true;
            }
        }
        else if (this$usedFields.equals(other$usedFields)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof CompiledMessage;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $raw = this.getRaw();
        result = result * 59 + (($raw == null) ? 43 : $raw.hashCode());
        final Object $parts = this.getParts();
        result = result * 59 + (($parts == null) ? 43 : $parts.hashCode());
        final Object $usedFields = this.getUsedFields();
        result = result * 59 + (($usedFields == null) ? 43 : $usedFields.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "CompiledMessage(raw=" + this.getRaw() + ", parts=" + (Object)this.getParts() + ", usedFields=" + (Object)this.getUsedFields() + ")";
    }
    
    private CompiledMessage(final String raw, final List<MessageElement> parts, final Set<String> usedFields) {
        this.raw = raw;
        this.parts = parts;
        this.usedFields = usedFields;
    }
    
    static {
        FIELD_PATTERN = Pattern.compile("\\{(?<content>[^}]+)\\}");
    }
}
