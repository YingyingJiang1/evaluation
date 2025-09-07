    @Override
    public void addParams(CommandArguments args) {
      args.add(FILTER).add(field)
          .add(formatNum(min, exclusiveMin))
          .add(formatNum(max, exclusiveMax));
    }
