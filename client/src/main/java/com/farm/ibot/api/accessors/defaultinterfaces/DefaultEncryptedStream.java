package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.IsaacWrapper;
import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IEncryptedStream;

public class DefaultEncryptedStream implements IEncryptedStream {
    public IsaacWrapper getIsaac(Object instance) {
        return (IsaacWrapper) Wrapper.get("EncryptedStream.isaac", IsaacWrapper.class, instance);
    }

    public void writePacket(Object instance, Object value) {
        Wrapper.invoke(instance, "EncryptedStream.writePacket", value);
    }
}
