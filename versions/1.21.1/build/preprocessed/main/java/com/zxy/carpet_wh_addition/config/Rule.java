package com.zxy.carpet_wh_addition.config;

import carpet.api.settings.Validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Rule {
    String[] categories();

    String[] options() default {};

    boolean strict() default true;

    Class<? extends Validator>[] validators() default {};

    Class<? extends RuleObserver>[] observer() default {};
}
