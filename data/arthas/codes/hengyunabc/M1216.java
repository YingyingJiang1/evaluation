    public static void updateOnglStrict(boolean strict) {
        try {
            Field field = OgnlRuntime.class.getDeclaredField("_useStricterInvocation");
            field.setAccessible(true);
            // 获取字段的内存偏移量和基址
            Object staticFieldBase = UnsafeUtils.UNSAFE.staticFieldBase(field);
            long staticFieldOffset = UnsafeUtils.UNSAFE.staticFieldOffset(field);

            // 修改字段的值
            UnsafeUtils.UNSAFE.putBoolean(staticFieldBase, staticFieldOffset, strict);
        } catch (NoSuchFieldException | SecurityException e) {
            // ignore
        }
    }
