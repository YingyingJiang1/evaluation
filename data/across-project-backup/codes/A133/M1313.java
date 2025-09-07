    private void initMethod() {
        if (constructor != null || method != null) {
            return;
        }

        try {
            ClassLoader loader = this.clazz.getClassLoader();
            final Type asmType = Type.getMethodType(methodDesc);

            // to arg types
            final Class<?>[] argsClasses = new Class<?>[asmType.getArgumentTypes().length];
            for (int index = 0; index < argsClasses.length; index++) {
                // asm class descriptor to jvm class
                final Class<?> argumentClass;
                final Type argumentAsmType = asmType.getArgumentTypes()[index];
                switch (argumentAsmType.getSort()) {
                case Type.BOOLEAN: {
                    argumentClass = boolean.class;
                    break;
                }
                case Type.CHAR: {
                    argumentClass = char.class;
                    break;
                }
                case Type.BYTE: {
                    argumentClass = byte.class;
                    break;
                }
                case Type.SHORT: {
                    argumentClass = short.class;
                    break;
                }
                case Type.INT: {
                    argumentClass = int.class;
                    break;
                }
                case Type.FLOAT: {
                    argumentClass = float.class;
                    break;
                }
                case Type.LONG: {
                    argumentClass = long.class;
                    break;
                }
                case Type.DOUBLE: {
                    argumentClass = double.class;
                    break;
                }
                case Type.ARRAY: {
                    argumentClass = toClass(loader, argumentAsmType.getInternalName());
                    break;
                }
                case Type.VOID: {
                    argumentClass = void.class;
                    break;
                }
                case Type.OBJECT:
                case Type.METHOD:
                default: {
                    argumentClass = toClass(loader, argumentAsmType.getClassName());
                    break;
                }
                }

                argsClasses[index] = argumentClass;
            }

            if ("<init>".equals(this.methodName)) {
                this.constructor = clazz.getDeclaredConstructor(argsClasses);
            } else {
                this.method = clazz.getDeclaredMethod(methodName, argsClasses);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

    }
