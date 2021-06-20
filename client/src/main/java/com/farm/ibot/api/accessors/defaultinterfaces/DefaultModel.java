package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IModel;

public class DefaultModel implements IModel {
    public int getIndicesLength(Object instance) {
        return (Integer) Wrapper.get("Model.IndicesLength", instance);
    }

    public int[] getIndicesX(Object instance) {
        return (int[]) Wrapper.get("Model.IndicesX", instance);
    }

    public int[] getIndicesY(Object instance) {
        return (int[]) Wrapper.get("Model.IndicesY", instance);
    }

    public int[] getIndicesZ(Object instance) {
        return (int[]) Wrapper.get("Model.IndicesZ", instance);
    }

    public int getVerticesLength(Object instance) {
        return (Integer) Wrapper.get("Model.VerticesLength", instance);
    }

    public int[] getVerticesX(Object instance) {
        return (int[]) Wrapper.get("Model.VerticesX", instance);
    }

    public int[] getVerticesY(Object instance) {
        return (int[]) Wrapper.get("Model.VerticesY", instance);
    }

    public int[] getVerticesZ(Object instance) {
        return (int[]) Wrapper.get("Model.VerticesZ", instance);
    }
}
