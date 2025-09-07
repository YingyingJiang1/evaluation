    @Override
    public int size() {
        if (content != null) {
            //粗略计算行数作为item size
            return content.length()/100 + 1;
        }
        return 0;
    }
