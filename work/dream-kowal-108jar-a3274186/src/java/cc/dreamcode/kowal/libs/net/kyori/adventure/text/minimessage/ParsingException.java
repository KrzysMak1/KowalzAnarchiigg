package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public abstract class ParsingException extends RuntimeException
{
    private static final long serialVersionUID = 4502774670340827070L;
    public static final int LOCATION_UNKNOWN = -1;
    
    protected ParsingException(@Nullable final String message) {
        super(message);
    }
    
    protected ParsingException(@Nullable final String message, @Nullable final Throwable cause) {
        super(message, cause);
    }
    
    protected ParsingException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    @NotNull
    public abstract String originalText();
    
    @Nullable
    public abstract String detailMessage();
    
    public abstract int startIndex();
    
    public abstract int endIndex();
}
