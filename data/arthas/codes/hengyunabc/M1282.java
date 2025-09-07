        @AtInvoke(name = "", inline = true, whenComplete = true, excludes = {"java.arthas.SpyAPI", "java.lang.Byte"
                , "java.lang.Boolean"
                , "java.lang.Short"
                , "java.lang.Character"
                , "java.lang.Integer"
                , "java.lang.Float"
                , "java.lang.Long"
                , "java.lang.Double"})
        public static void onInvokeAfter(@Binding.This Object target, @Binding.Class Class<?> clazz,
                @Binding.InvokeInfo String invokeInfo) {
            SpyAPI.atAfterInvoke(clazz, invokeInfo, target);
        }
