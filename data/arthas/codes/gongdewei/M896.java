    private CommandOptionVO createOptionVO(Option option) {
        CommandOptionVO optionVO = new CommandOptionVO();
        if (!isEmptyName(option.getLongName())) {
            optionVO.setLongName(option.getLongName());
        }
        if (!isEmptyName(option.getShortName())) {
            optionVO.setShortName(option.getShortName());
        }
        optionVO.setDescription(option.getDescription());
        optionVO.setAcceptValue(option.acceptValue());
        return optionVO;
    }
