package cc.dreamcode.kowal.libs.cc.dreamcode.command;

import lombok.NonNull;

public interface DreamSender<T>
{
    Type getType();
    
    String getName();
    
    boolean hasPermission(@NonNull final String permission);
    
    T getHandler();
    
    public enum Type
    {
        CONSOLE, 
        CLIENT;
    }
}
