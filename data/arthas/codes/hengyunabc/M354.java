    public static ProtectionDomain getProtectionDomain(final Class source) {
        if (source == null) {
            return null;
        }
        return (ProtectionDomain) AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                return source.getProtectionDomain();
            }
        });
    }
