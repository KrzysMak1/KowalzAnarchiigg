package cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.exception;

import lombok.Generated;
import lombok.NonNull;

public class InvalidInputException extends RuntimeException
{
    private final Class<?> requiringClass;
    private final String input;
    
    public InvalidInputException(@NonNull final Class<?> requiringClass, @NonNull final String input, @NonNull final String cause) {
        super(cause);
        if (requiringClass == null) {
            throw new NullPointerException("requiringClass is marked non-null but is null");
        }
        if (input == null) {
            throw new NullPointerException("input is marked non-null but is null");
        }
        if (cause == null) {
            throw new NullPointerException("cause is marked non-null but is null");
        }
        this.requiringClass = requiringClass;
        this.input = input;
    }
    
    @Generated
    public Class<?> getRequiringClass() {
        return this.requiringClass;
    }
    
    @Generated
    public String getInput() {
        return this.input;
    }
}
