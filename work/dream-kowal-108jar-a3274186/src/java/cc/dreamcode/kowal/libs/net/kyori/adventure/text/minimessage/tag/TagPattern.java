package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag;

import org.intellij.lang.annotations.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE })
@Pattern("[!?#]?[a-z0-9_-]*")
public @interface TagPattern {
}
