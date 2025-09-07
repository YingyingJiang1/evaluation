    @Override
    public String apply(String data) {
        data = super.apply(data);
        if (out != null) {
            out.write(data);
            out.flush();
        } else {
            LogUtil.getResultLogger().info(data);
        }
        return data;
    }
