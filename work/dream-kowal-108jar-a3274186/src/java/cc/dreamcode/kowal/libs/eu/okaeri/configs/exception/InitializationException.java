package cc.dreamcode.kowal.libs.eu.okaeri.configs.exception;

public class InitializationException extends OkaeriException
{
    public InitializationException(final String message) {
        super(message);
    }
    
    public InitializationException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public InitializationException(final Throwable cause) {
        super(cause);
    }
}
