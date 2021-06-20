package com.farm.ibot.api.accessors.interfaces;

import com.farm.ibot.api.accessors.IsaacWrapper;

public interface IEncryptedStream {
    IsaacWrapper getIsaac(Object var1);

    void writePacket(Object var1, Object var2);
}
