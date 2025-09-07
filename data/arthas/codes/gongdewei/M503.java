    @Override
    public void draw(CommandProcess process, MBeanModel result) {
        if (result.getMbeanNames() != null) {
            drawMBeanNames(process, result.getMbeanNames());
        } else if (result.getMbeanMetadata() != null) {
            drawMBeanMetadata(process, result.getMbeanMetadata());
        } else if (result.getMbeanAttribute() != null) {
            drawMBeanAttributes(process, result.getMbeanAttribute());
        }
    }
