package cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.commons.duration;

import java.time.temporal.ChronoUnit;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DurationSpec {
    ChronoUnit fallbackUnit() default ChronoUnit.SECONDS;
    
    DurationFormat format() default DurationFormat.SIMPLIFIED;
}
