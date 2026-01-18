package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal;

import java.util.Locale;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.TagPattern;
import java.util.regex.Pattern;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class TagInternals
{
    @RegExp
    public static final String TAG_NAME_REGEX = "[!?#]?[a-z0-9_-]*";
    private static final Pattern TAG_NAME_PATTERN;
    
    private TagInternals() {
    }
    
    public static void assertValidTagName(@TagPattern @NotNull final String tagName) {
        if (!TagInternals.TAG_NAME_PATTERN.matcher((CharSequence)Objects.requireNonNull((Object)tagName)).matches()) {
            throw new IllegalArgumentException("Tag name must match pattern " + TagInternals.TAG_NAME_PATTERN.pattern() + ", was " + tagName);
        }
    }
    
    public static boolean sanitizeAndCheckValidTagName(@TagPattern @NotNull final String tagName) {
        return TagInternals.TAG_NAME_PATTERN.matcher((CharSequence)((String)Objects.requireNonNull((Object)tagName)).toLowerCase(Locale.ROOT)).matches();
    }
    
    public static void sanitizeAndAssertValidTagName(@TagPattern @NotNull final String tagName) {
        assertValidTagName(((String)Objects.requireNonNull((Object)tagName)).toLowerCase(Locale.ROOT));
    }
    
    static {
        TAG_NAME_PATTERN = Pattern.compile("[!?#]?[a-z0-9_-]*");
    }
}
