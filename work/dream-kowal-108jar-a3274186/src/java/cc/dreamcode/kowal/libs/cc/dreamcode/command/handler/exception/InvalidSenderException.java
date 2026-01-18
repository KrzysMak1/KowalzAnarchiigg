package cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.exception;

import lombok.Generated;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.DreamSender;
import java.util.List;

public class InvalidSenderException extends RuntimeException
{
    private final List<DreamSender.Type> requireType;
    
    public InvalidSenderException(@NonNull final List<DreamSender.Type> requireType, @NonNull final String cause) {
        super(cause);
        if (requireType == null) {
            throw new NullPointerException("requireType is marked non-null but is null");
        }
        if (cause == null) {
            throw new NullPointerException("cause is marked non-null but is null");
        }
        this.requireType = requireType;
    }
    
    @Generated
    public List<DreamSender.Type> getRequireType() {
        return this.requireType;
    }
}
