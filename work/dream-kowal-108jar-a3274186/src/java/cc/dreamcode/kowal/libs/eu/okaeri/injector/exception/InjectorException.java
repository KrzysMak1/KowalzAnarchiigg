package cc.dreamcode.kowal.libs.eu.okaeri.injector.exception;

public class InjectorException extends RuntimeException
{
    public InjectorException(final String message) {
        super(message);
    }
    
    public InjectorException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
