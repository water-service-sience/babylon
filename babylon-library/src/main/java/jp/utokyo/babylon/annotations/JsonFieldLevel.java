package jp.utokyo.babylon.annotations;

/**
 * Created by takezoux2 on 2014/02/04.
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.FIELD,ElementType.METHOD,ElementType.TYPE})
public @interface JsonFieldLevel {
    int value();
}
