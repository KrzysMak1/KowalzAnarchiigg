package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag;

import cc.dreamcode.kowal.libs.net.kyori.adventure.internal.Internals;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;

abstract class AbstractTag implements Tag, Examinable
{
    @Override
    public final String toString() {
        return Internals.toString(this);
    }
}
