    public static Class<?> getInnerGenericClass(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) type;
            Type[] actualTypeArguments = paramType.getActualTypeArguments();
            if (actualTypeArguments.length > 0) {
                Type innerType = actualTypeArguments[0]; // 获取第一个实际类型参数
                if (innerType instanceof ParameterizedType) {
                    return getInnerGenericClass(innerType); // 递归调用获取最内层类型
                } else if (innerType instanceof Class) {
                    return (Class<?>) innerType; // 直接返回 Class 类型
                }
            }
        }
        return null; // 如果没有找到对应的类型
    }
