    @Override
    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className,
                    JavaFileObject.Kind kind, FileObject sibling) throws IOException {

        for (MemoryByteCode byteCode : byteCodes) {
            if (byteCode.getClassName().equals(className)) {
                return byteCode;
            }
        }

        MemoryByteCode innerClass = new MemoryByteCode(className);
        byteCodes.add(innerClass);
        classLoader.registerCompiledSource(innerClass);
        return innerClass;

    }
