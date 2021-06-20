package com.farm.ibot.core.script.manifest;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ScriptManifest {
    boolean isP2p() default false;
}
