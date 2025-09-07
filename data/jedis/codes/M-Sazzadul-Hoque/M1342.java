    @Override
    public void addParams(CommandArguments args) {
      args.add(GEOFILTER).add(field)
          .add(lon).add(lat)
          .add(radius).add(unit);
    }
