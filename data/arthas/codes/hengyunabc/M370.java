    public static Class defineClass(String className, byte[] b, ClassLoader loader, ProtectionDomain protectionDomain)
                    throws Exception {

        return defineClass(className, b, loader, protectionDomain, null);
    }
