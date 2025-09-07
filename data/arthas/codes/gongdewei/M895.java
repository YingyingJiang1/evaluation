    private ArgumentVO createArgumentVO(com.taobao.middleware.cli.Argument argument) {
        ArgumentVO argumentVO = new ArgumentVO();
        argumentVO.setArgName(argument.getArgName());
        argumentVO.setMultiValued(argument.isMultiValued());
        argumentVO.setRequired(argument.isRequired());
        return argumentVO;
    }
