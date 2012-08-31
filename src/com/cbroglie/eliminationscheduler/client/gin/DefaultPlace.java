package com.cbroglie.eliminationscheduler.client.gin;

import com.google.inject.BindingAnnotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation is used in {@link EliminationSchedulerPlaceManager} and is bind
 * in {@link ClientModule}. It's purpose is to bind the default
 * place to a default presenter.
 * 
 * @author cbroglie
 */
@BindingAnnotation
@Target({FIELD, PARAMETER, METHOD})
@Retention(RUNTIME)
public @interface DefaultPlace {
}
