    @Override
    public void addTypeArgs(CommandArguments args) {
      if (separator != null) {
        args.add("SEPARATOR");
        args.add(separator);
      }
      if (caseSensitive) {
        args.add("CASESENSITIVE");
      }
    }
