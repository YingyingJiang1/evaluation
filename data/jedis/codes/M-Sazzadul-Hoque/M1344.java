    @Override
    public void addParams(CommandArguments args) {
      args.add(HIGHLIGHT);

      if (fields != null) {
        args.add(FIELDS).add(fields.size()).addObjects(fields);
      }
      if (tags != null) {
        args.add(TAGS).add(tags[0]).add(tags[1]);
      }
    }
