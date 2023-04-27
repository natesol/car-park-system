package net.cps.common.utils;

import net.cps.common.messages.RequestMessage;
import net.cps.common.messages.ResponseMessage;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.io.Serializable;
import java.lang.annotation.*;


/**
 * Callback 'interface' and 'Annotation' for the `RequestMessage` instances to pass as a callback function from the caller.
 *
 * <p>
 * RequestCallback interface:<br>
 *  - The callback will be called when the response is received back from the server.<br>
 *  - The callback method must be implemented before its reference, and it will be passed two parameters: the original request, and the response.<br>
 *  - A lambda expression can be used to implement the callback method. but on general, a callback method has to be annotated with the `@RequestCallback` annotation.<br>
 * </p>
 * <br>
 * <p>
 * RequestCallback Annotation:<br>
 *  - This annotation meant to be used on a methods with `RequestMessageCallback` interface signature.<br>
 * </p>
 **/
@FunctionalInterface
public interface RequestCallback extends BiConsumer<@NotNull RequestMessage, @NotNull ResponseMessage>, Serializable {
    
    /**
     * Annotation to mark methods that are meant to be passed as a callback for a server `RequestMessage`.
     **/
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    static public @interface Method {
        int numOfParams() default 2;
        Class<?> firstParamType() default RequestMessage.class;
        Class<?> secondParamType() default ResponseMessage.class;
    }
}





