    @Override
    public void process(CommandProcess process) {
        MemoryModel result = new MemoryModel();
        result.setMemoryInfo(memoryInfo());
        process.appendResult(result);
        process.end();
    }
