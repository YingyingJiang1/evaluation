        @Override
        public void register(AdviceListener adviceListener, ClassFileTransformer transformer) {
            if (adviceListener instanceof ProcessAware) {
                ProcessAware processAware = (ProcessAware) adviceListener;
                // listener 有可能是其它 command 创建的
                if(processAware.getProcess() == null) {
                    processAware.setProcess(this.process);
                }
            }
            this.listener = adviceListener;
            AdviceWeaver.reg(listener);
            
            this.transformer = transformer;
        }
