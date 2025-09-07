    @Override
    public void unregister() {
        if (transformer != null) {
            DemoBootstrap.getRunningInstance().getTransformerManager().removeTransformer(transformer);
        }
        this.process.setProcessStatus(ExecStatus.TERMINATED);
        if (listener instanceof ProcessAware) {
            // listener有可能其它 command 创建的，所以不能unRge
            if (this.process.equals(((ProcessAware) listener).getProcess())) {
                AdviceWeaver.unReg(listener);
            }
        } else {
            AdviceWeaver.unReg(listener);
        }
    }
