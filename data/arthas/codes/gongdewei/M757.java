    private ThreadVO createThreadVO(String name) {
        ThreadVO threadVO = new ThreadVO();
        threadVO.setId(-1);
        threadVO.setName(name);
        threadVO.setPriority(-1);
        threadVO.setDaemon(true);
        threadVO.setInterrupted(false);
        return threadVO;
    }
