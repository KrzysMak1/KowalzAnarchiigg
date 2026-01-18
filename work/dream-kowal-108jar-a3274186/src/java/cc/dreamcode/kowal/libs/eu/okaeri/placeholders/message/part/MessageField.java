package cc.dreamcode.kowal.libs.eu.okaeri.placeholders.message.part;

import java.util.Arrays;
import java.util.regex.Matcher;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import java.util.Locale;
import java.util.regex.Pattern;

public class MessageField implements MessageElement, MessageFieldAccessor
{
    private static final Pattern PATH_ELEMENT_PATTERN;
    private final Locale locale;
    private final String name;
    private final String source;
    @Nullable
    private final MessageField sub;
    @Nullable
    private String defaultValue;
    @Nullable
    private String metadataRaw;
    @Nullable
    private String paramsRaw;
    @Nullable
    private String raw;
    private String lastSubPath;
    private MessageField lastSub;
    private String[] metadataOptions;
    private FieldParams params;
    
    @Deprecated
    public static MessageField unknown() {
        return of("unknown");
    }
    
    public static MessageField of(@NonNull final String source) {
        if (source == null) {
            throw new NullPointerException("source is marked non-null but is null");
        }
        return of(Locale.ENGLISH, source);
    }
    
    public static MessageField of(@NonNull final Locale locale, @NonNull final String source) {
        if (locale == null) {
            throw new NullPointerException("locale is marked non-null but is null");
        }
        if (source == null) {
            throw new NullPointerException("source is marked non-null but is null");
        }
        final String[] parts = source.split("\\.");
        MessageField field = null;
        for (int i = parts.length - 1; i >= 0; --i) {
            final String pathElement = parts[i];
            final Matcher matcher = MessageField.PATH_ELEMENT_PATTERN.matcher((CharSequence)pathElement);
            if (!matcher.find()) {
                throw new RuntimeException("invalid field path element: " + pathElement);
            }
            final String fieldRealName = matcher.group("name");
            final String fieldParams = matcher.group("params");
            field = new MessageField(locale, fieldRealName, source, field);
            field.setParamsRaw(fieldParams);
        }
        if (field != null) {
            final MessageField lastSub = field.getLastSub();
            final String lastSubPath = field.getLastSubPath();
            field.getParams();
        }
        return field;
    }
    
    private static String lastSubPath(@NonNull final MessageField field) {
        if (field == null) {
            throw new NullPointerException("field is marked non-null but is null");
        }
        MessageField last = field;
        final StringBuilder out = new StringBuilder(field.getName());
        while (last.getSub() != null) {
            last = last.getSub();
            out.append(".").append(last.getName());
        }
        return out.toString();
    }
    
    public void setDefaultValue(@Nullable final String defaultValue) {
        this.defaultValue = defaultValue;
        MessageField sub;
        for (MessageField field = this; field.getSub() != null; field = sub) {
            sub = field.getSub();
            sub.setDefaultValue(defaultValue);
        }
    }
    
    public boolean hasSub() {
        return this.sub != null;
    }
    
    @Nullable
    public MessageField getLastSub() {
        if (this.sub == null) {
            return null;
        }
        if (this.lastSub == null) {
            MessageField last;
            for (last = this.sub; last.getSub() != null; last = last.getSub()) {}
            this.lastSub = last;
        }
        return this.lastSub;
    }
    
    public String getLastSubPath() {
        if (this.lastSubPath == null) {
            this.lastSubPath = lastSubPath(this);
        }
        return this.lastSubPath;
    }
    
    public void setMetadataRaw(@Nullable final String metadataRaw) {
        this.metadataRaw = metadataRaw;
        MessageField sub;
        for (MessageField field = this; field.getSub() != null; field = sub) {
            sub = field.getSub();
            sub.setMetadataRaw(metadataRaw);
            sub.updateMetadataOptionsCache();
        }
        this.updateMetadataOptionsCache();
    }
    
    public void updateMetadataOptionsCache() {
        if (this.metadataRaw == null) {
            return;
        }
        this.metadataOptions = splitPartsWithEscape(this.metadataRaw);
    }
    
    public FieldParams getParams() {
        if (this.paramsRaw == null) {
            this.params = FieldParams.empty(this.name);
        }
        if (this.params == null) {
            this.params = FieldParams.of(this.name, splitPartsWithEscape(this.paramsRaw));
        }
        return this.params;
    }
    
    private static String[] splitPartsWithEscape(final String text) {
        final String[] options = text.split("(?<!\\\\)(?:;|,)");
        for (int i = 0; i < options.length; ++i) {
            options[i] = options[i].replace((CharSequence)"\\,", (CharSequence)",").replace((CharSequence)"\\;", (CharSequence)";");
        }
        return options;
    }
    
    @Override
    public Locale locale() {
        return this.getLocale();
    }
    
    @Override
    public FieldParams params() {
        return this.getParams();
    }
    
    @Override
    public MessageField unsafe() {
        return this;
    }
    
    public Locale getLocale() {
        return this.locale;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getSource() {
        return this.source;
    }
    
    @Nullable
    public MessageField getSub() {
        return this.sub;
    }
    
    @Nullable
    public String getDefaultValue() {
        return this.defaultValue;
    }
    
    @Nullable
    public String getMetadataRaw() {
        return this.metadataRaw;
    }
    
    @Nullable
    public String getParamsRaw() {
        return this.paramsRaw;
    }
    
    @Nullable
    public String getRaw() {
        return this.raw;
    }
    
    public String[] getMetadataOptions() {
        return this.metadataOptions;
    }
    
    public void setParamsRaw(@Nullable final String paramsRaw) {
        this.paramsRaw = paramsRaw;
    }
    
    public void setRaw(@Nullable final String raw) {
        this.raw = raw;
    }
    
    public void setLastSubPath(final String lastSubPath) {
        this.lastSubPath = lastSubPath;
    }
    
    public void setLastSub(final MessageField lastSub) {
        this.lastSub = lastSub;
    }
    
    public void setMetadataOptions(final String[] metadataOptions) {
        this.metadataOptions = metadataOptions;
    }
    
    public void setParams(final FieldParams params) {
        this.params = params;
    }
    
    @Override
    public String toString() {
        return "MessageField(locale=" + (Object)this.getLocale() + ", name=" + this.getName() + ", source=" + this.getSource() + ", sub=" + (Object)this.getSub() + ", defaultValue=" + this.getDefaultValue() + ", metadataRaw=" + this.getMetadataRaw() + ", paramsRaw=" + this.getParamsRaw() + ", raw=" + this.getRaw() + ", lastSubPath=" + this.getLastSubPath() + ", lastSub=" + (Object)this.getLastSub() + ", metadataOptions=" + Arrays.deepToString((Object[])this.getMetadataOptions()) + ", params=" + (Object)this.getParams() + ")";
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MessageField)) {
            return false;
        }
        final MessageField other = (MessageField)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$locale = this.getLocale();
        final Object other$locale = other.getLocale();
        Label_0065: {
            if (this$locale == null) {
                if (other$locale == null) {
                    break Label_0065;
                }
            }
            else if (this$locale.equals(other$locale)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0102: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0102;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$source = this.getSource();
        final Object other$source = other.getSource();
        Label_0139: {
            if (this$source == null) {
                if (other$source == null) {
                    break Label_0139;
                }
            }
            else if (this$source.equals(other$source)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$sub = this.getSub();
        final Object other$sub = other.getSub();
        Label_0176: {
            if (this$sub == null) {
                if (other$sub == null) {
                    break Label_0176;
                }
            }
            else if (this$sub.equals(other$sub)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$defaultValue = this.getDefaultValue();
        final Object other$defaultValue = other.getDefaultValue();
        Label_0213: {
            if (this$defaultValue == null) {
                if (other$defaultValue == null) {
                    break Label_0213;
                }
            }
            else if (this$defaultValue.equals(other$defaultValue)) {
                break Label_0213;
            }
            return false;
        }
        final Object this$metadataRaw = this.getMetadataRaw();
        final Object other$metadataRaw = other.getMetadataRaw();
        Label_0250: {
            if (this$metadataRaw == null) {
                if (other$metadataRaw == null) {
                    break Label_0250;
                }
            }
            else if (this$metadataRaw.equals(other$metadataRaw)) {
                break Label_0250;
            }
            return false;
        }
        final Object this$paramsRaw = this.getParamsRaw();
        final Object other$paramsRaw = other.getParamsRaw();
        Label_0287: {
            if (this$paramsRaw == null) {
                if (other$paramsRaw == null) {
                    break Label_0287;
                }
            }
            else if (this$paramsRaw.equals(other$paramsRaw)) {
                break Label_0287;
            }
            return false;
        }
        final Object this$lastSubPath = this.getLastSubPath();
        final Object other$lastSubPath = other.getLastSubPath();
        Label_0324: {
            if (this$lastSubPath == null) {
                if (other$lastSubPath == null) {
                    break Label_0324;
                }
            }
            else if (this$lastSubPath.equals(other$lastSubPath)) {
                break Label_0324;
            }
            return false;
        }
        final Object this$lastSub = this.getLastSub();
        final Object other$lastSub = other.getLastSub();
        Label_0361: {
            if (this$lastSub == null) {
                if (other$lastSub == null) {
                    break Label_0361;
                }
            }
            else if (this$lastSub.equals(other$lastSub)) {
                break Label_0361;
            }
            return false;
        }
        if (!Arrays.deepEquals((Object[])this.getMetadataOptions(), (Object[])other.getMetadataOptions())) {
            return false;
        }
        final Object this$params = this.getParams();
        final Object other$params = other.getParams();
        if (this$params == null) {
            if (other$params == null) {
                return true;
            }
        }
        else if (this$params.equals(other$params)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof MessageField;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $locale = this.getLocale();
        result = result * 59 + (($locale == null) ? 43 : $locale.hashCode());
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $source = this.getSource();
        result = result * 59 + (($source == null) ? 43 : $source.hashCode());
        final Object $sub = this.getSub();
        result = result * 59 + (($sub == null) ? 43 : $sub.hashCode());
        final Object $defaultValue = this.getDefaultValue();
        result = result * 59 + (($defaultValue == null) ? 43 : $defaultValue.hashCode());
        final Object $metadataRaw = this.getMetadataRaw();
        result = result * 59 + (($metadataRaw == null) ? 43 : $metadataRaw.hashCode());
        final Object $paramsRaw = this.getParamsRaw();
        result = result * 59 + (($paramsRaw == null) ? 43 : $paramsRaw.hashCode());
        final Object $lastSubPath = this.getLastSubPath();
        result = result * 59 + (($lastSubPath == null) ? 43 : $lastSubPath.hashCode());
        final Object $lastSub = this.getLastSub();
        result = result * 59 + (($lastSub == null) ? 43 : $lastSub.hashCode());
        result = result * 59 + Arrays.deepHashCode((Object[])this.getMetadataOptions());
        final Object $params = this.getParams();
        result = result * 59 + (($params == null) ? 43 : $params.hashCode());
        return result;
    }
    
    private MessageField(final Locale locale, final String name, final String source, @Nullable final MessageField sub) {
        this.locale = locale;
        this.name = name;
        this.source = source;
        this.sub = sub;
    }
    
    static {
        PATH_ELEMENT_PATTERN = Pattern.compile("^(?<name>[^\\s(]+)(?:\\((?<params>.*)\\))?$");
    }
}
