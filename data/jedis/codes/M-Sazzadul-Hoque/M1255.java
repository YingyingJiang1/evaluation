    @Override
    public void addParams(CommandArguments args) {
      args.add(SearchKeyword.GEOFILTER.getRaw());
      args.add(SafeEncoder.encode(property));
      args.add(Protocol.toByteArray(lon));
      args.add(Protocol.toByteArray(lat));
      args.add(Protocol.toByteArray(radius));
      args.add(SafeEncoder.encode(unit));
    }
