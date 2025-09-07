    private OptionVO convertToOptionVO(Field optionField) throws IllegalAccessException {
        Option optionAnnotation = optionField.getAnnotation(Option.class);
        OptionVO optionVO = new OptionVO();
        optionVO.setLevel(optionAnnotation.level());
        optionVO.setName(optionAnnotation.name());
        optionVO.setSummary(optionAnnotation.summary());
        optionVO.setDescription(optionAnnotation.description());
        optionVO.setType(optionField.getType().getSimpleName());
        optionVO.setValue(""+optionField.get(null));
        return optionVO;
    }
