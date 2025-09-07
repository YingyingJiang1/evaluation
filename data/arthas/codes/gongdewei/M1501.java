    public static MethodVO createMethodInfo(Constructor constructor, Class clazz, boolean detail) {
        MethodVO methodVO = new MethodVO();
        methodVO.setDeclaringClass(clazz.getName());
        methodVO.setDescriptor(Type.getConstructorDescriptor(constructor));
        methodVO.setMethodName("<init>");
        methodVO.setConstructor(true);
        if (detail) {
            methodVO.setModifier(StringUtils.modifier(constructor.getModifiers(), ','));
            methodVO.setAnnotations(TypeRenderUtils.getAnnotations(constructor.getDeclaredAnnotations()));
            methodVO.setParameters(getClassNameList(constructor.getParameterTypes()));
            methodVO.setExceptions(getClassNameList(constructor.getExceptionTypes()));
            methodVO.setClassLoaderHash(StringUtils.classLoaderHash(clazz));
        }
        return methodVO;
    }
