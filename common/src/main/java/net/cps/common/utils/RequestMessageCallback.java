package net.cps.common.utils;

import net.cps.common.messages.RequestMessage;
import net.cps.common.messages.ResponseMessage;

/**
 * Callback interface for the RequestMessage to pass a callback from the caller.
 */
@FunctionalInterface
public interface RequestMessageCallback {
    void callback(RequestMessage request, ResponseMessage response, Object data);
}

