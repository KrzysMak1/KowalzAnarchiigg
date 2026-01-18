package cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.exception;

import lombok.Generated;
import lombok.NonNull;

public class InvalidPermissionException extends RuntimeException
{
    private final String permission;
    
    public InvalidPermissionException(@NonNull final String permission, @NonNull final String cause) {
        super(cause);
        if (permission == null) {
            throw new NullPointerException("permission is marked non-null but is null");
        }
        if (cause == null) {
            throw new NullPointerException("cause is marked non-null but is null");
        }
        this.permission = permission;
    }
    
    @Generated
    public String getPermission() {
        return this.permission;
    }
}
