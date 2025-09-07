    @Override
    public void addParams(CommandArguments args) {
      args.add(SearchKeyword.FILTER.getRaw());
      args.add(SafeEncoder.encode(property));
      args.add(formatNum(min, exclusiveMin));
      args.add(formatNum(max, exclusiveMax));
    }
