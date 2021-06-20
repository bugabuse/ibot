package com.farm.ibot.core.reflection.classloader;

import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.StringUtils;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.reflection.Hook;
import com.farm.ibot.init.ConsoleParams;
import com.farm.ibot.init.Hooks;
import com.farm.ibot.init.Settings;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import javassist.*;
import net.openhft.compiler.CachedCompiler;
import net.openhft.compiler.CompilerUtils;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;

public class Injector {
    public ClassPool cp;
    private static final CachedCompiler JCC = new CachedCompiler(new File("./","./accessors/"), new File("./","./accessors/"));

    public Injector(ClassPool cp) {
        this.cp = cp;
    }

    private static String getBrackets(int dimensions, boolean empty) {
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < dimensions + 1; ++i) {
            if (!empty) {
                sb.append("[x" + i + "]");
            } else {
                sb.append("[]");
            }
        }

        return sb.toString();
    }

    public static CtClass getComponentType(CtClass array) throws NotFoundException {
        CtClass temp;
        for (temp = array; temp.isArray(); temp = temp.getComponentType()) {
        }

        return temp;
    }

    public void loadGeneratedAccessorsToApi(ClassLoader loader, Bot bot) {
        try {
            UnmodifiableIterator var3 = ClassPath.from(this.cp.getClassLoader()).getAllClasses().iterator();

            while (var3.hasNext()) {
                ClassInfo classInfo = (ClassInfo) var3.next();
                if (classInfo.getName().startsWith("com.farm.ibot.api.accessors.defaultinterfaces.")) {
                    try {
                        Class interfaceClass = loader.loadClass("Generated" + classInfo.getSimpleName().replace("Default", ""));
                        Object newInterface = interfaceClass.newInstance();
                        Field f = this.getApiFieldForInterface(loader, interfaceClass);
                        f.set(bot.accessorInterface, newInterface);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Field getApiFieldForInterface(ClassLoader loader, Class interfaceClass) throws Exception {
        Field[] var3 = loader.loadClass("com.farm.ibot.core.AccessorInterface").getFields();
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Field f = var3[var5];
            if (f.getType().getName().startsWith("com.farm.ibot.api.accessors.interfaces.I" + interfaceClass.getSimpleName().replace("Generated", ""))) {
                return f;
            }
        }

        return null;
    }

    public void injectEventConsumer() throws NotFoundException, CannotCompileException {
        if (Settings.actionInteract) {
            CtClass clientClass = this.cp.getCtClass("client");
            clientClass.addField(CtField.make("public static boolean allowInput = true;", clientClass));
            Hook processMenuAction = Hooks.forName("Client.processMenuAction");
            this.cp.getCtClass(processMenuAction.getObfuscatedClass()).getDeclaredMethod(processMenuAction.getObfuscatedField()).insertBefore("{ System.out.println(\"OnMenuAction [\" + $1 + \", \" + $2 + \", \" + $3 + \", \" + $4 + \", \" + $5 + \", \" + $6 + \", \" + $7 + \", \" + $8  + \", \" + $9 + \"]\" ); if($9 != (byte)111 && !client.allowInput) {System.out.println(\"Blocking processMenuAction\"); client.allowInput = true; return;} System.out.println(\"New multipler \" + " + processMenuAction.getterMultipler + "); $9 = (byte)" + processMenuAction.getterMultipler + ";}");
        }
    }

    public void injectLowCpuMode() throws NotFoundException, CannotCompileException {
        if (Settings.lowCpuEnabled) {

            CtClass clientClass = this.cp.getCtClass("client");
            clientClass.addField(CtField.make("public static boolean lowCpuEnabled = false;", clientClass));
            clientClass.addField(CtField.make("public static long lockLowCpuUntil = 0L;", clientClass));
            clientClass.addField(CtField.make("public static long lastUpdate = 0L;", clientClass));
            Hook menuOpen = Hooks.forName("Scene.menuOpen");
            Hook drawTile = Hooks.forName("Scene.drawTile");
            Hook renderGame = Hooks.forName("GameShell.renderGame");
            this.cp.getCtClass(drawTile.getObfuscatedClass()).getDeclaredMethod(drawTile.getObfuscatedField()).insertBefore("if(client.lowCpuEnabled) {return;}");
            this.cp.getCtClass(menuOpen.getObfuscatedClass()).getDeclaredMethod(menuOpen.getObfuscatedField()).insertBefore("if(client.lowCpuEnabled) {return;}");
            if (Settings.actionInteract) {
                this.cp.getCtClass(renderGame.getObfuscatedClass()).getDeclaredMethod(renderGame.getObfuscatedField()).insertBefore("if(client.lowCpuEnabled) {    if(System.getProperty(\"longSleep\").equals(\"2\")) {Thread.sleep(3000L);}      if(System.getProperty(\"longSleep\").equals(\"1\")) {Thread.sleep(700L);}   Thread.sleep(" + Settings.renderDelayTime + "L); }");
            } else {
                this.cp.getCtClass(renderGame.getObfuscatedClass()).getDeclaredMethod(renderGame.getObfuscatedField()).insertBefore("if(client.lowCpuEnabled) {    Thread.sleep(" + Settings.renderDelayTime + "L); }");
            }

        }
    }

    public void makeFieldsAccessible() throws NotFoundException {
        Iterator var1 = Hooks.hooks.values().iterator();

        while (var1.hasNext()) {
            Hook hook = (Hook) var1.next();
            if (!hook.obfuscatedName.contains("broken")) {
                try {
                    CtClass c2;
                    if (hook.isField()) {
                        c2 = this.cp.getCtClass(hook.getObfuscatedClass());
                        CtField f = c2.getField(hook.getObfuscatedField());
                        f.setModifiers(Modifier.setPublic(f.getModifiers()));
                    } else if (hook.isMethod()) {
                        c2 = this.cp.getCtClass(hook.getObfuscatedClass());
                        CtMethod f = c2.getDeclaredMethod(hook.getObfuscatedField());
                        f.setModifiers(Modifier.setPublic(f.getModifiers()));
                    }
                } catch (Exception var5) {
                    var5.printStackTrace();
                }
            }
        }
    }

    public void generateInjectedApiAccessors() {
        try {
            if (Settings.useInjection) {
                this.injectLowCpuMode();
                this.injectEventConsumer();
                this.makeFieldsAccessible();
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        if (ConsoleParams.contains("inject_api")) {
            Debug.log("Generating accessor classes");
            try {

                UnmodifiableIterator var1 = ClassPath.from(this.cp.getClassLoader()).getAllClasses().iterator();

                while (var1.hasNext()) {
                    ClassInfo classInfo = (ClassInfo) var1.next();
                    if (classInfo.getName().startsWith("com.farm.ibot.api.accessors.defaultinterfaces.")) {
                        CtClass interfaceClass = this.cp.getCtClass(classInfo.getName());
                        this.createInjectionInterface(interfaceClass);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void injectAntiBrowserOpen() throws Exception {
        Hook hook = Hooks.forName("Client.openUrl");
        CtClass c2 = this.cp.getCtClass(hook.getObfuscatedClass());
        CtMethod m2 = c2.getDeclaredMethods(hook.getObfuscatedField())[0];
        m2.insertBefore("if(true)return;");
    }

    private void injectClassGetterDebug() throws Exception {
        CtClass c2 = this.cp.getCtClass("ch");
        CtMethod m2 = c2.getMethod("s", "(Ljava/lang/String;I)Ljava/lang/Class;");
        m2.insertBefore("System.out.println(\"Requested class: \" + $1);");
    }

    private void createInjectionInterface(CtClass interfaceClass) throws Exception {
        String classFileBody = "";
        CtMethod[] var3 = interfaceClass.getDeclaredMethods();
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            CtMethod methodToOverride = var3[var5];
            Hook hook = this.getHookForMethod(methodToOverride);
            if (hook != null && !hook.obfuscatedName.contains("broken")) {
                try {
                    CtClass gameClass = this.cp.getCtClass(hook.getObfuscatedClass());
                    CtField gameField = gameClass.getField(hook.getObfuscatedField());
                    String body;
                    if (gameField.getType().isArray()) {
                        body = this.createArray(hook, methodToOverride, Modifier.isStatic(gameField.getModifiers()));
                    } else if (Modifier.isStatic(gameField.getModifiers())) {
                        if (methodToOverride.getName().startsWith("set")) {
                            body = this.createStaticSetter(hook, methodToOverride);
                        } else {
                            body = this.createStaticGetter(hook, methodToOverride);
                        }
                    } else if (methodToOverride.getName().startsWith("set")) {
                        body = this.createInstanceSetter(hook, methodToOverride);
                    } else {
                        body = this.createInstanceGetter(hook, methodToOverride);
                    }

                    classFileBody = classFileBody + body + "\n\n";
                } catch (Exception var11) {
                    var11.printStackTrace();
                    Debug.log("Could not inject: " + methodToOverride.getName());
                }
            } else {
                Debug.log("Hook not found for: " + methodToOverride.getName());
            }
        }

        String className = "Generated" + interfaceClass.getSimpleName().replace("Default", "");
        String classFileString = "public class " + className + " extends " + interfaceClass.getName() + " {\n";
        classFileString = classFileString + classFileBody;
        classFileString = classFileString + "\n }";

        FileWriter writer = new FileWriter(new File("accessors/" + className + ".java"));
        writer.write(classFileString);
        writer.close();

        /*
         * i thought i might be able to compile this crap at run-time but i haven't figured out
         * the dir stuff yet. so gamepack.jar is always missing.
         *
         * https://github.com/OpenHFT/Java-Runtime-Compiler
         *
        try {
            URLClassLoader loader = new URLClassLoader(new URL[]{
                    (new File(Settings.SCRIPTS_DIRECTORY.getAbsolutePath() + File.separator)).toURI().toURL(),
                    (new File(Settings.BOT_DATA_PATH + "gamepack.jar")).toURI().toURL(),
            });
            JCC.loadFromJava(loader, className, classFileString);
        }
        catch(Exception e){
            e.printStackTrace();
        }*/

    }

    private String createInstanceGetter(Hook hook, CtMethod method) throws NotFoundException {
        String returnBody;
        if (this.isWrapper(method)) {
            returnBody = "((HOOK_TYPE)instance).HOOK_ACCESSOR != null ? new TYPE(((HOOK_TYPE)instance).HOOK_ACCESSOR) : null";
        } else {
            returnBody = "((HOOK_TYPE)instance).HOOK_ACCESSOR";
            if (hook.getterMultipler != null && hook.getterMultipler.longValue() != -1L) {
                returnBody = returnBody + " * HOOK_MULTIPLER";
            }
        }

        returnBody = returnBody.replace("HOOK_TYPE", hook.getObfuscatedClass());
        returnBody = returnBody.replace("HOOK_ACCESSOR", hook.getObfuscatedField());
        returnBody = returnBody.replace("HOOK_MULTIPLER", StringUtils.numberToString(hook.getterMultipler));
        String bodyStr = "public TYPE NAME(Object instance) { return RETURN_BODY;}";
        bodyStr = bodyStr.replace("RETURN_BODY", returnBody);
        bodyStr = bodyStr.replace("TYPE", method.getReturnType().getName());
        bodyStr = bodyStr.replace("NAME", method.getName());
        return bodyStr;
    }

    private String createStaticSetter(Hook hook, CtMethod method) throws NotFoundException {
        CtField targetField = this.cp.getCtClass(hook.getObfuscatedClass()).getField(hook.getObfuscatedField());
        String returnBody = "HOOK_TYPE.HOOK_ACCESSOR = (TARGET_HOOK_TYPE)value";
        if (hook.setterMultipler != null && hook.setterMultipler.longValue() != -1L) {
            returnBody = "HOOK_TYPE.HOOK_ACCESSOR = (PRIMITIVE_TYPE)value * HOOK_MULTIPLER";
        }

        returnBody = returnBody.replace("TARGET_HOOK_TYPE", targetField.getType().getName());
        returnBody = returnBody.replace("PRIMITIVE_TYPE", "" + this.getPrimitiveTypeName(hook.setterMultipler));
        returnBody = returnBody.replace("HOOK_TYPE", hook.getObfuscatedClass());
        returnBody = returnBody.replace("HOOK_ACCESSOR", hook.getObfuscatedField());
        returnBody = returnBody.replace("HOOK_MULTIPLER", StringUtils.numberToString(hook.setterMultipler));
        String bodyStr = "public TYPE NAME(Object instance, Object value) { RETURN_BODY;}";
        bodyStr = bodyStr.replace("RETURN_BODY", returnBody);
        bodyStr = bodyStr.replace("TYPE", method.getReturnType().getName());
        bodyStr = bodyStr.replace("NAME", method.getName());
        return bodyStr;
    }

    private String createInstanceSetter(Hook hook, CtMethod method) throws NotFoundException {
        CtField targetField = this.cp.getCtClass(hook.getObfuscatedClass()).getField(hook.getObfuscatedField());
        String returnBody = "((HOOK_TYPE)instance).HOOK_ACCESSOR = (TARGET_HOOK_TYPE)value";
        if (hook.setterMultipler != null && hook.setterMultipler.longValue() != -1L) {
            returnBody = "((HOOK_TYPE)instance).HOOK_ACCESSOR = (PRIMITIVE_TYPE)value * HOOK_MULTIPLER";
        }

        returnBody = returnBody.replace("TARGET_HOOK_TYPE", targetField.getType().getName());
        returnBody = returnBody.replace("HOOK_TYPE", hook.getObfuscatedClass());
        returnBody = returnBody.replace("HOOK_ACCESSOR", hook.getObfuscatedField());
        returnBody = returnBody.replace("HOOK_MULTIPLER", StringUtils.numberToString(hook.setterMultipler));
        returnBody = returnBody.replace("PRIMITIVE_TYPE", "" + this.getPrimitiveTypeName(hook.setterMultipler));
        String bodyStr = "public void NAME(Object instance, Object value) { RETURN_BODY;}";
        bodyStr = bodyStr.replace("RETURN_BODY", returnBody);
        bodyStr = bodyStr.replace("TYPE", method.getReturnType().getName());
        bodyStr = bodyStr.replace("NAME", method.getName());
        return bodyStr;
    }

    private String getPrimitiveTypeName(Number setterMultipler) {
        if (setterMultipler.getClass().equals(Integer.class)) {
            return "int";
        } else {
            return setterMultipler.getClass().equals(Long.class) ? "long" : null;
        }
    }

    private String createStaticGetter(Hook hook, CtMethod method) throws NotFoundException {
        String returnBody;
        if (this.isWrapper(method)) {
            returnBody = "HOOK_TYPE.HOOK_ACCESSOR != null ? new TYPE(HOOK_TYPE.HOOK_ACCESSOR) : null";
        } else {
            returnBody = "HOOK_TYPE.HOOK_ACCESSOR";
            if (hook.getterMultipler != null && hook.getterMultipler.longValue() != -1L) {
                returnBody = returnBody + " * HOOK_MULTIPLER";
            }
        }

        returnBody = returnBody.replace("HOOK_TYPE", hook.getObfuscatedClass());
        returnBody = returnBody.replace("HOOK_ACCESSOR", hook.getObfuscatedField());
        returnBody = returnBody.replace("HOOK_MULTIPLER", StringUtils.numberToString(hook.getterMultipler));
        String bodyStr = "public TYPE NAME(Object instance) { return RETURN_BODY;}";
        bodyStr = bodyStr.replace("RETURN_BODY", returnBody);
        bodyStr = bodyStr.replace("TYPE", method.getReturnType().getName());
        bodyStr = bodyStr.replace("NAME", method.getName());
        return bodyStr;
    }

    public String createArray(Hook hook, CtMethod method, boolean isStatic) throws NotFoundException {
        if (!getComponentType(method.getReturnType()).isPrimitive() && !getComponentType(method.getReturnType()).getName().contains("java.lang")) {
            int dimensions = method.getReturnType().getName().split("\\[").length - 1;
            StringBuilder sb = new StringBuilder();
            sb.append("if(@realArray == null) return null;");
            sb.append("@constructorClassName" + getBrackets(dimensions, true) + " newArray = new @constructorClassName[@realArray.length]" + getBrackets(dimensions - 1, true) + ";");
            int i = 1;

            while (true) {
                sb.append("\n");
                sb.append("for(int x" + i + " = 0; x" + i + " < @realArray" + getBrackets(i - 1, false) + ".length && x" + i + " < newArray" + getBrackets(i - 1, false) + ".length; x" + i + "++) {\n");
                if (i + 1 == dimensions + 1) {
                    sb.append("if(@realArray" + getBrackets(i, false) + "!= null)");
                    sb.append("     newArray" + getBrackets(i, false) + " = new @constructorClassName(@realArray" + getBrackets(i, false) + ");");

                    for (i = 0; i < dimensions; ++i) {
                        sb.append("\n}");
                    }

                    sb.append("return newArray;");
                    String instanceClassName = "((OBFUSCATED_CLASS_NAME)instance).OBFUSCATED_FIELD";
                    if (isStatic) {
                        instanceClassName = "OBFUSCATED_CLASS_NAME.OBFUSCATED_FIELD";
                    }

                    instanceClassName = instanceClassName.replace("OBFUSCATED_CLASS_NAME", hook.getObfuscatedClass());
                    instanceClassName = instanceClassName.replace("OBFUSCATED_FIELD", hook.getObfuscatedField());
                    String body = sb.toString().replaceAll("@constructorClassName", method.getReturnType().getName().replace("[]", "")).replaceAll("@realArray", instanceClassName);
                    String fullMethod = "public INTERFACE_TYPE METHOD_NAME(Object instance) { BODY }".replace("BODY", body).replace("INTERFACE_TYPE", method.getReturnType().getName()).replace("METHOD_NAME", method.getName());
                    return fullMethod;
                }

                if (i == 1 && dimensions > 2) {
                    sb.append("if(@realArray" + getBrackets(i, false) + "== null) continue;");
                    sb.append("newArray" + getBrackets(i, false) + " = new @constructorClassName[@realArray" + getBrackets(i, false) + ".length][];");
                } else {
                    sb.append("if(@realArray" + getBrackets(i, false) + "== null) continue;");
                    sb.append("newArray" + getBrackets(i, false) + " = new @constructorClassName[@realArray" + getBrackets(i, false) + ".length];");
                }

                ++i;
            }
        } else {
            return isStatic ? this.createStaticGetter(hook, method) : this.createInstanceGetter(hook, method);
        }
    }

    private boolean isWrapper(CtMethod method) throws NotFoundException {
        return method.getReturnType().getName().startsWith("com.farm.ibot.api.accessors");
    }

    public Hook getHookForMethod(CtMethod m) throws Exception {
        UnmodifiableIterator var2 = ClassPath.from(this.cp.getClassLoader()).getAllClasses().iterator();

        while (true) {
            ClassInfo classInfo;
            do {
                do {
                    if (!var2.hasNext()) {
                        return null;
                    }

                    classInfo = (ClassInfo) var2.next();
                } while (!classInfo.getName().startsWith("com.farm.ibot.api.accessors"));
            } while (!m.getDeclaringClass().getSimpleName().equalsIgnoreCase("Default" + classInfo.getSimpleName()));

            CtMethod[] var4 = this.cp.getCtClass(classInfo.getName()).getMethods();
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                CtMethod method = var4[var6];
                if (method.getName().equalsIgnoreCase(m.getName())) {
                    HookName name = (HookName) method.getAnnotation(HookName.class);
                    if (name != null) {
                        return Hooks.forName(name.value());
                    }
                }
            }
        }
    }
}
