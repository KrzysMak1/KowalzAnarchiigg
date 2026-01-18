package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer;

import org.jetbrains.annotations.NotNull;

public interface ClaimConsumer
{
    void style(@NotNull final String claimKey, @NotNull final Emitable styleClaim);
    
    boolean component(@NotNull final Emitable componentClaim);
    
    boolean styleClaimed(@NotNull final String claimId);
    
    boolean componentClaimed();
}
