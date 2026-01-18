package cc.dreamcode.kowal.libs.eu.okaeri.configs.exception;

public class ValidationException extends OkaeriException
{
    public ValidationException(final String message) {
        super(message);
    }
    
    public ValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ValidationException(final Throwable cause) {
        super(cause);
    }
}
