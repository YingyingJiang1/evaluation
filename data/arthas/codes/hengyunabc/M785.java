    private static void initTransformer() {
        if (transformer != null) {
            return;
        } else {
            synchronized (RetransformCommand.class) {
                if (transformer == null) {
                    transformer = new RetransformClassFileTransformer();
                    TransformerManager transformerManager = ArthasBootstrap.getInstance().getTransformerManager();
                    transformerManager.addRetransformer(transformer);
                }
            }
        }
    }
