    @Override
    public void addParams(CommandArguments args) {
        if (existance != null) {
            args.add(existance);
        }

        super.addParams(args);
    }
