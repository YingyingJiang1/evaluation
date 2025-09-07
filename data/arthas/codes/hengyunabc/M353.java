                public Object run() throws Exception {
                    Method[] methods = Object.class.getDeclaredMethods();
                    for (Method method : methods) {
                        if ("finalize".equals(method.getName())
                                        || (method.getModifiers() & (Modifier.FINAL | Modifier.STATIC)) > 0) {
                            continue;
                        }
                        OBJECT_METHODS.add(method);
                    }
                    return null;
                }
