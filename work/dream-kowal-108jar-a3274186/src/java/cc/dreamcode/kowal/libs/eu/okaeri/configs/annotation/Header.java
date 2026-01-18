package cc.dreamcode.kowal.libs.eu.okaeri.configs.annotation;

import java.lang.annotation.Repeatable;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Headers.class)
public @interface Header {
    String[] value();
    
    boolean raw() default false;
}
