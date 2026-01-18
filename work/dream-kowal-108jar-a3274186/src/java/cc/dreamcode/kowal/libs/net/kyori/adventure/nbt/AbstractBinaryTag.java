package cc.dreamcode.kowal.libs.net.kyori.adventure.nbt;

import cc.dreamcode.kowal.libs.net.kyori.examination.Examiner;
import cc.dreamcode.kowal.libs.net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;

abstract class AbstractBinaryTag implements BinaryTag
{
    @NotNull
    @Override
    public final String examinableName() {
        return this.type().toString();
    }
    
    @Override
    public final String toString() {
        return this.examine((Examiner<String>)StringExaminer.simpleEscaping());
    }
}
