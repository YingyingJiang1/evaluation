    @Override
    public void draw(CommandProcess process, EnhancerModel result) {
        // ignore enhance result status, judge by the following output
        if (result.getEffect() != null) {
            process.write(ViewRenderUtil.renderEnhancerAffect(result.getEffect()));
        }
    }
