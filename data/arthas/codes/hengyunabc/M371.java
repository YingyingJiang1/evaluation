    public static Class defineClass(String className, byte[] b, ClassLoader loader, ProtectionDomain protectionDomain,
                    Class<?> contextClass) throws Exception {

        Class c = null;

        // 在 jdk 17之后，需要hack方式来调用 #2659
        if (c == null && classLoaderDefineClassMethod != null) {
            Lookup implLookup = UnsafeUtils.implLookup();
            MethodHandle unreflect = implLookup.unreflect(classLoaderDefineClassMethod);

            if (protectionDomain == null) {
                protectionDomain = PROTECTION_DOMAIN;
            }
            try {
                c = (Class) unreflect.invoke(loader, className, b, 0, b.length, protectionDomain);
            } catch (InvocationTargetException ex) {
                throw new ReflectException(ex.getTargetException());
            } catch (Throwable ex) {
                // Fall through if setAccessible fails with InaccessibleObjectException on JDK
                // 9+
                // (on the module path and/or with a JVM bootstrapped with
                // --illegal-access=deny)
                if (!ex.getClass().getName().endsWith("InaccessibleObjectException")) {
                    throw new ReflectException(ex);
                }
            }
        }

        // Preferred option: JDK 9+ Lookup.defineClass API if ClassLoader matches
        if (contextClass != null && contextClass.getClassLoader() == loader && privateLookupInMethod != null
                        && lookupDefineClassMethod != null) {
            try {
                MethodHandles.Lookup lookup = (MethodHandles.Lookup) privateLookupInMethod.invoke(null, contextClass,
                                MethodHandles.lookup());
                c = (Class) lookupDefineClassMethod.invoke(lookup, b);
            } catch (InvocationTargetException ex) {
                Throwable target = ex.getTargetException();
                if (target.getClass() != LinkageError.class && target.getClass() != IllegalArgumentException.class) {
                    throw new ReflectException(target);
                }
                // in case of plain LinkageError (class already defined)
                // or IllegalArgumentException (class in different package):
                // fall through to traditional ClassLoader.defineClass below
            } catch (Throwable ex) {
                throw new ReflectException(ex);
            }
        }

        // Classic option: protected ClassLoader.defineClass method
        if (c == null && classLoaderDefineClassMethod != null) {
            if (protectionDomain == null) {
                protectionDomain = PROTECTION_DOMAIN;
            }
            Object[] args = new Object[] { className, b, 0, b.length, protectionDomain };
            try {
                if (!classLoaderDefineClassMethod.isAccessible()) {
                    classLoaderDefineClassMethod.setAccessible(true);
                }
                c = (Class) classLoaderDefineClassMethod.invoke(loader, args);
            } catch (InvocationTargetException ex) {
                throw new ReflectException(ex.getTargetException());
            } catch (Throwable ex) {
                // Fall through if setAccessible fails with InaccessibleObjectException on JDK
                // 9+
                // (on the module path and/or with a JVM bootstrapped with
                // --illegal-access=deny)
                if (!ex.getClass().getName().endsWith("InaccessibleObjectException")) {
                    throw new ReflectException(ex);
                }
            }
        }

        // Fallback option: JDK 9+ Lookup.defineClass API even if ClassLoader does not
        // match
        if (c == null && contextClass != null && contextClass.getClassLoader() != loader
                        && privateLookupInMethod != null && lookupDefineClassMethod != null) {
            try {
                MethodHandles.Lookup lookup = (MethodHandles.Lookup) privateLookupInMethod.invoke(null, contextClass,
                                MethodHandles.lookup());
                c = (Class) lookupDefineClassMethod.invoke(lookup, b);
            } catch (InvocationTargetException ex) {
                throw new ReflectException(ex.getTargetException());
            } catch (Throwable ex) {
                throw new ReflectException(ex);
            }
        }

        // No defineClass variant available at all?
        if (c == null) {
            throw new ReflectException(THROWABLE);
        }

        // Force static initializers to run.
        Class.forName(className, true, loader);
        return c;
    }
