    public static ClassDetailVO createClassInfo(Class clazz, boolean withFields, Integer expand) {
        CodeSource cs = clazz.getProtectionDomain().getCodeSource();
        ClassDetailVO classInfo = new ClassDetailVO();
        classInfo.setName(StringUtils.classname(clazz));
        classInfo.setClassInfo(StringUtils.classname(clazz));
        classInfo.setCodeSource(ClassUtils.getCodeSource(cs));
        classInfo.setInterface(clazz.isInterface());
        classInfo.setAnnotation(clazz.isAnnotation());
        classInfo.setEnum(clazz.isEnum());
        classInfo.setAnonymousClass(clazz.isAnonymousClass());
        classInfo.setArray(clazz.isArray());
        classInfo.setLocalClass(clazz.isLocalClass());
        classInfo.setMemberClass(clazz.isMemberClass());
        classInfo.setPrimitive(clazz.isPrimitive());
        classInfo.setSynthetic(clazz.isSynthetic());
        classInfo.setSimpleName(clazz.getSimpleName());
        classInfo.setModifier(StringUtils.modifier(clazz.getModifiers(), ','));
        classInfo.setAnnotations(TypeRenderUtils.getAnnotations(clazz));
        classInfo.setInterfaces(TypeRenderUtils.getInterfaces(clazz));
        classInfo.setSuperClass(TypeRenderUtils.getSuperClass(clazz));
        classInfo.setClassloader(TypeRenderUtils.getClassloader(clazz));
        classInfo.setClassLoaderHash(StringUtils.classLoaderHash(clazz));
        if (withFields) {
            classInfo.setFields(TypeRenderUtils.getFields(clazz, expand));
        }
        return classInfo;
    }
