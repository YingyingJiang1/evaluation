    @Override
    public void process(CommandProcess process) {
        //每个分支调用process.end()结束执行
        if (StringUtils.isEmpty(getName())) {
            listMBean(process);
        } else if (isMetaData()) {
            listMetaData(process);
        } else {
            listAttribute(process);
        }
    }
