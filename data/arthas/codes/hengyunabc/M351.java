                public Object run() throws Exception {
                    try {
                        return MethodHandles.class.getMethod("privateLookupIn", Class.class,
                                        MethodHandles.Lookup.class);
                    } catch (NoSuchMethodException ex) {
                        return null;
                    }
                }
