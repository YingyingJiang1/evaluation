    @Override
    public final void addParams(CommandArguments args) {
      this.fieldName.addParams(args);
      args.add(type.name());
      addTypeArgs(args);
      if (sortable) {
        args.add("SORTABLE");
      }
      if (noIndex) {
        args.add("NOINDEX");
      }
    }
