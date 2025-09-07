    public static void unReg(AdviceListener listener) {
        if (null != listener) {
            // 注销监听器
            advices.remove(listener.id());

            // 触发监听器销毁
            listener.destroy();
        }
    }
