    @Override
    protected void addTypeArgs(CommandArguments args) {
      if (weight != 1.0) {
        args.add("WEIGHT");
        args.add(Double.toString(weight));
      }
      if (nostem) {
        args.add("NOSTEM");
      }
      if (phonetic != null) {
        args.add("PHONETIC");
        args.add(this.phonetic);
      }
    }
