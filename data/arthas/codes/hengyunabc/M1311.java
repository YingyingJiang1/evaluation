    public void removeTransformer(ClassFileTransformer transformer) {
        reTransformers.remove(transformer);
        watchTransformers.remove(transformer);
        traceTransformers.remove(transformer);
    }
