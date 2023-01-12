package net.cps.common.utils;

import net.cps.common.messages.RequestMessage;
import net.cps.common.messages.ResponseMessage;

import java.io.Serializable;
import java.util.function.BiConsumer;

/**
 * Callback interface for the RequestMessage to pass a callback from the caller.
 * This callback will be called when the response is received back from the server.
 * The callback method must be implemented by the caller, and must take two parameters: RequestMessage object and ResponseMessage object.
 */
@FunctionalInterface
public interface RequestCallback extends BiConsumer<RequestMessage, ResponseMessage>, Serializable {}

