package dev._2lstudios.advancedauth.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Command {
    public String name();

    public String usageKey() default "";

    public String permission() default "";

    public Argument[] arguments() default {};

    public String[] aliases() default {};

    public boolean silent() default false;

    public boolean requireAuth() default true;

    public int minArguments() default Integer.MIN_VALUE;
}