    public static MethodHandles.Lookup implLookup() {
        if (IMPL_LOOKUP == null) {
            Class<MethodHandles.Lookup> lookupClass = MethodHandles.Lookup.class;

            try {
                Field implLookupField = lookupClass.getDeclaredField("IMPL_LOOKUP");
                long offset = UNSAFE.staticFieldOffset(implLookupField);
                IMPL_LOOKUP = (MethodHandles.Lookup) UNSAFE.getObject(UNSAFE.staticFieldBase(implLookupField), offset);
            } catch (Throwable e) {
                // ignored
            }
        }
        return IMPL_LOOKUP;
    }
