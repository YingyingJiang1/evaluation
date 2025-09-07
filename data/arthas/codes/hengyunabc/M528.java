    @Override
    public void draw(CommandProcess process, Base64Model result) {
        String content = result.getContent();
        if (content != null) {
            process.write(content);
        }
        process.write("\n");
    }
