package cc.dreamcode.kowal.libs.cc.dreamcode.platform.component;

import lombok.NonNull;

public interface ComponentExtension
{
    void register(@NonNull final ComponentService componentService);
}
