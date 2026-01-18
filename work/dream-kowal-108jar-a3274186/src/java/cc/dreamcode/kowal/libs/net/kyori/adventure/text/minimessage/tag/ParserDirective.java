package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface ParserDirective extends Tag
{
    public static final Tag RESET = new ParserDirective() {
        @Override
        public String toString() {
            return "ParserDirective.RESET";
        }
    };
}
