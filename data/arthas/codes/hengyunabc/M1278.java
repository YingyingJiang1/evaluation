        @AtEnter(inline = true)
        public static void atEnter(@Binding.This Object target, @Binding.Class Class<?> clazz,
                @Binding.MethodInfo String methodInfo, @Binding.Args Object[] args) {
            SpyAPI.atEnter(clazz, methodInfo, target, args);
        }
