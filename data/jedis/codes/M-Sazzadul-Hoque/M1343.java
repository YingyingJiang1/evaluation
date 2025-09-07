    @Override
    public void addParams(CommandArguments args) {
      args.add(SUMMARIZE);

      if (fields != null) {
        args.add(FIELDS).add(fields.size()).addObjects(fields);
      }
      if (fragsNum != null) {
        args.add(FRAGS).add(fragsNum);
      }
      if (fragSize != null) {
        args.add(LEN).add(fragSize);
      }
      if (separator != null) {
        args.add(SEPARATOR).add(separator);
      }
    }
