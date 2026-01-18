package cc.dreamcode.kowal.libs.cc.dreamcode.platform.exception;

public class PlatformException extends RuntimeException
{
    public PlatformException(final String text) {
        super(text);
    }
    
    public PlatformException(final Throwable throwable) {
        super(throwable);
    }
    
    public PlatformException(final String text, final Throwable throwable) {
        super(text, throwable);
    }
}
