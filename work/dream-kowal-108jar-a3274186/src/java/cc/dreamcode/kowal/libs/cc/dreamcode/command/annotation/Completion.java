package cc.dreamcode.kowal.libs.cc.dreamcode.command.annotation;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Repeatable;

@Repeatable(Completions.class)
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Completion {
    String arg();
    
    String[] value();
    
    CompletionFilter[] filter() default {};
}
