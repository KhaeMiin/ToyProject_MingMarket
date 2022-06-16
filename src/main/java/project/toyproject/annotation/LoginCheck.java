package project.toyproject.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) //파라미터에 선언
@Retention(RetentionPolicy.RUNTIME) // 컴파일 이후에도 JVM에 의해 계속 참조가 가능함
public @interface LoginCheck {
}
