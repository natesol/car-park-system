package net.cps.common.utils;
import java.lang.annotation.*;


/**
 * Request Callback Annotation - this annotation is used to mark a method that is supposed to be passed as a callback for a server request.
 * This annotation is intended to complement the use of `RequestMessageCallback` interface.
 **/
@Documented
@Target(ElementType.METHOD)
public @interface RequestCallback {}