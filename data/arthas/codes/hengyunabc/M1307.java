    public static void reg(AdviceListener listener) {

        // 触发监听器创建
        listener.create();

        // 注册监听器
        advices.put(listener.id(), listener);
    }
