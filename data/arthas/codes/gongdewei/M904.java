    private List<OptionVO> convertToOptionVOs(Collection<Field> fields) throws IllegalAccessException {
        List<OptionVO> list = new ArrayList<OptionVO>();
        for (Field field : fields) {
            list.add(convertToOptionVO(field));
        }
        return list;
    }
