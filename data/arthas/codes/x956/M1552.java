    static Object getInputProtobufObj(Method rpcMethod, byte[] in) {
        Class[] inputArgs = rpcMethod.getParameterTypes();
        Class inputArgClass = inputArgs[0];

        // use the inputArg classtype to create a protobuf object
        Method parseFromObj;
        try {
            parseFromObj = inputArgClass.getMethod("parseFrom", byte[].class);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Couldn't find method in 'parseFrom' in " + inputArgClass.getName());
        }

        Object inputObj;
        try {
            inputObj = parseFromObj.invoke(null, in);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }

        if (inputObj == null || !inputArgClass.isInstance(inputObj)) {
            throw new IllegalArgumentException("Input obj is **not** instance of the correct input class type");
        }
        return inputObj;
    }
