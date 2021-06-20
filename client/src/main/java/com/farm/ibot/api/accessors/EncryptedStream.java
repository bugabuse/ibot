package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.IEncryptedStream;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

public class EncryptedStream extends Stream {
    public EncryptedStream(Object instance) {
        super(instance);
    }

    public static IEncryptedStream getEncryptedStreamInterface() {
        return Bot.get().accessorInterface.encryptedStreamInterface;
    }

    @HookName("EncryptedStream.isaac")
    public IsaacWrapper getIsaac() {
        return getEncryptedStreamInterface().getIsaac(this.instance);
    }

    @HookName("EncryptedStream.writePacket")
    public void writePacket(int i) {
        getEncryptedStreamInterface().writePacket(this.instance, i);
    }
}
