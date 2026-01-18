package cc.dreamcode.kowal.libs.eu.okaeri.configs.exception;

public class OkaeriException extends RuntimeException
{
    public OkaeriException(final String message) {
        super(message);
    }
    
    public OkaeriException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public OkaeriException(final Throwable cause) {
        super(cause);
    }
}
