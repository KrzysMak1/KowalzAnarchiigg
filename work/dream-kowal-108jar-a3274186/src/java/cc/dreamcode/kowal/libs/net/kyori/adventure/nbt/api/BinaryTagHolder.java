package cc.dreamcode.kowal.libs.net.kyori.adventure.nbt.api;

import org.jetbrains.annotations.ApiStatus;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.Codec;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.DataComponentValue;

public interface BinaryTagHolder extends DataComponentValue.TagSerializable
{
    @NotNull
    default <T, EX extends Exception> BinaryTagHolder encode(@NotNull final T nbt, @NotNull final Codec<? super T, String, ?, EX> codec) throws EX, Exception {
        return new BinaryTagHolderImpl(codec.encode(nbt));
    }
    
    @NotNull
    default BinaryTagHolder binaryTagHolder(@NotNull final String string) {
        return new BinaryTagHolderImpl(string);
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    @NotNull
    default BinaryTagHolder of(@NotNull final String string) {
        return new BinaryTagHolderImpl(string);
    }
    
    @NotNull
    String string();
    
    @NotNull
    default BinaryTagHolder asBinaryTag() {
        return this;
    }
    
    @NotNull
     <T, DX extends Exception> T get(@NotNull final Codec<T, String, DX, ?> codec) throws DX, Exception;
}
