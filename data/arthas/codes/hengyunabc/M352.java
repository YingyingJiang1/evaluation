                public Object run() throws Exception {
                    try {
                        return MethodHandles.Lookup.class.getMethod("defineClass", byte[].class);
                    } catch (NoSuchMethodException ex) {
                        return null;
                    }
                }
