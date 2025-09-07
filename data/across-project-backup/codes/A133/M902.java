    private ExitStatus processChangeNameValue(CommandProcess process) throws IllegalAccessException {
        Collection<Field> fields = findOptionFields(new EqualsMatcher<String>(optionName));

        // name not exists
        if (fields.isEmpty()) {
            return ExitStatus.failure(-1, format("options[%s] not found.", optionName));
        }

        Field field = fields.iterator().next();
        Option optionAnnotation = field.getAnnotation(Option.class);
        Class<?> type = field.getType();
        Object beforeValue = FieldUtils.readStaticField(field);
        Object afterValue;

        try {
            // try to case string to type
            if (isIn(type, int.class, Integer.class)) {
                FieldUtils.writeStaticField(field, afterValue = Integer.valueOf(optionValue));
            } else if (isIn(type, long.class, Long.class)) {
                FieldUtils.writeStaticField(field, afterValue = Long.valueOf(optionValue));
            } else if (isIn(type, boolean.class, Boolean.class)) {
                FieldUtils.writeStaticField(field, afterValue = Boolean.valueOf(optionValue));
            } else if (isIn(type, double.class, Double.class)) {
                FieldUtils.writeStaticField(field, afterValue = Double.valueOf(optionValue));
            } else if (isIn(type, float.class, Float.class)) {
                FieldUtils.writeStaticField(field, afterValue = Float.valueOf(optionValue));
            } else if (isIn(type, byte.class, Byte.class)) {
                FieldUtils.writeStaticField(field, afterValue = Byte.valueOf(optionValue));
            } else if (isIn(type, short.class, Short.class)) {
                FieldUtils.writeStaticField(field, afterValue = Short.valueOf(optionValue));
            } else if (isIn(type, short.class, String.class)) {
                FieldUtils.writeStaticField(field, afterValue = optionValue);
            } else {
                return ExitStatus.failure(-1, format("Options[%s] type[%s] was unsupported.", optionName, type.getSimpleName()));
            }

            // FIXME hack for ongl strict
            if (field.getName().equals("strict")) {
                GlobalOptions.updateOnglStrict(Boolean.valueOf(optionValue));
                logger.info("update ongl strict to: {}", optionValue);
            }
        } catch (Throwable t) {
            return ExitStatus.failure(-1, format("Cannot cast option value[%s] to type[%s].", optionValue, type.getSimpleName()));
        }

        ChangeResultVO changeResultVO = new ChangeResultVO(optionAnnotation.name(), beforeValue, afterValue);
        process.appendResult(new OptionsModel(changeResultVO));
        return ExitStatus.success();
    }
