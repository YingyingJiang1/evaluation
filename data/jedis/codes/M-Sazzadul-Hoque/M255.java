  @Override
  public void addParams(CommandArguments args) {

    this.configs.forEach(kv -> args.add(Keyword.CONFIG).add(kv.getKey()).add(kv.getValue()));

    if (!this.args.isEmpty()) {
      args.add(Keyword.ARGS).addObjects(this.args);
    }
  }
