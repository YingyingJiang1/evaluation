    @Override
    public void addTypeArgs(CommandArguments args) {
      args.add(algorithm);
      args.add(attributes.size() << 1);
      for (Map.Entry<String, Object> entry : attributes.entrySet()) {
        args.add(entry.getKey());
        args.add(entry.getValue());
      }
    }
