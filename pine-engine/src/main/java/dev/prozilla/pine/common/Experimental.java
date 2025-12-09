package dev.prozilla.pine.common;

import java.lang.annotation.*;

/**
 * Indicates that this part of the API is experimental, meaning there might be breaking changes in the next minor update.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD, ElementType.MODULE, ElementType.PACKAGE})
public @interface Experimental {}
