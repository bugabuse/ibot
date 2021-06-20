package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IStream;

public class DefaultStream implements IStream {
    public byte[] getByteArray(Object instance) {
        return (byte[]) Wrapper.get("Stream.byteArray", instance);
    }

    public int getPosition(Object instance) {
        return (Integer) Wrapper.get("Stream.position", instance);
    }

    public void setPosition(Object instance, Object value) {
        Wrapper.set("Stream.position", instance, value);
    }
}
