    public static MethodVO createMethodInfo(Method method, Class clazz, boolean detail) {
        MethodVO methodVO = new MethodVO();
        methodVO.setDeclaringClass(clazz.getName());
        methodVO.setMethodName(method.getName());
        methodVO.setDescriptor(Type.getMethodDescriptor(method));
        methodVO.setConstructor(false);
        if (detail) {
            methodVO.setModifier(StringUtils.modifier(method.getModifiers(), ','));
            methodVO.setAnnotations(TypeRenderUtils.getAnnotations(method.getDeclaredAnnotations()));
            methodVO.setParameters(getClassNameList(method.getParameterTypes()));
            methodVO.setReturnType(StringUtils.classname(method.getReturnType()));
            methodVO.setExceptions(getClassNameList(method.getExceptionTypes()));
            methodVO.setClassLoaderHash(StringUtils.classLoaderHash(clazz));
        }
        return methodVO;
    }
