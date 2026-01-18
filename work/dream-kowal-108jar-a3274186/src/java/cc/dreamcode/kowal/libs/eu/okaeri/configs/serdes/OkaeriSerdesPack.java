package cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes;

import lombok.NonNull;

public interface OkaeriSerdesPack
{
    void register(@NonNull final SerdesRegistry registry);
}
