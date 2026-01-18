package cc.dreamcode.kowal.libs.cc.dreamcode.menu.setup;

import lombok.NonNull;

public interface MenuPlayerSetup<M, H>
{
    M build(@NonNull final H p0);
}
