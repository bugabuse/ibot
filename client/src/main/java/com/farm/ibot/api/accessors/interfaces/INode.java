package com.farm.ibot.api.accessors.interfaces;

import com.farm.ibot.api.accessors.Node;

public interface INode {
    long getUid(Object var1);

    Node getNext(Object var1);

    Node getPrevious(Object var1);
}
