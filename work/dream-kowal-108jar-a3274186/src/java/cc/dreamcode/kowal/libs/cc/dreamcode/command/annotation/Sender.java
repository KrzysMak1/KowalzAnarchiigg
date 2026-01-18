package cc.dreamcode.kowal.libs.cc.dreamcode.command.annotation;

import cc.dreamcode.kowal.libs.cc.dreamcode.command.DreamSender;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Repeatable;

@Repeatable(Senders.class)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Sender {
    DreamSender.Type value();
}
