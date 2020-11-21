package ioc.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)//标注在类上
@Documented
public @interface Component {
    String value() default "";
}