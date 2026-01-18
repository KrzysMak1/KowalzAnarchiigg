package cc.dreamcode.kowal.libs.cc.dreamcode.notice;

import java.util.Map;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.StringUtil;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.message.CompiledMessage;
import lombok.NonNull;
import java.util.Optional;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.context.PlaceholderContext;
import java.util.Locale;

public abstract class Notice<R extends Notice<?>>
{
    private Locale locale;
    private PlaceholderContext placeholderContext;
    
    public Notice() {
        this.locale = Locale.forLanguageTag("pl");
        this.placeholderContext = null;
    }
    
    public abstract String getRaw();
    
    public abstract Enum<?> getNoticeType();
    
    public Optional<PlaceholderContext> getPlaceholderContext() {
        return (Optional<PlaceholderContext>)Optional.ofNullable((Object)this.placeholderContext);
    }
    
    public String getRender() {
        return (String)this.getPlaceholderContext().map(PlaceholderContext::apply).orElse((Object)this.getRaw());
    }
    
    public R setLocale(@NonNull final Locale locale) {
        if (locale == null) {
            throw new NullPointerException("locale is marked non-null but is null");
        }
        this.locale = locale;
        return (R)this;
    }
    
    public R with(@NonNull final String from, @NonNull final Object to) {
        if (from == null) {
            throw new NullPointerException("from is marked non-null but is null");
        }
        if (to == null) {
            throw new NullPointerException("to is marked non-null but is null");
        }
        if (this.placeholderContext == null) {
            final CompiledMessage compiledMessage = CompiledMessage.of(this.locale, this.getRaw());
            this.placeholderContext = StringUtil.getPlaceholders().contextOf(compiledMessage);
        }
        this.placeholderContext.with(from, to);
        return (R)this;
    }
    
    public R with(@NonNull final Map<String, Object> replaceMap) {
        if (replaceMap == null) {
            throw new NullPointerException("replaceMap is marked non-null but is null");
        }
        if (this.placeholderContext == null) {
            final CompiledMessage compiledMessage = CompiledMessage.of(this.locale, this.getRaw());
            this.placeholderContext = StringUtil.getPlaceholders().contextOf(compiledMessage);
        }
        this.placeholderContext.with(replaceMap);
        return (R)this;
    }
    
    public R clearRender() {
        this.placeholderContext = null;
        return (R)this;
    }
}
