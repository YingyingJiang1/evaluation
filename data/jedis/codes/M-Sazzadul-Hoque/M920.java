  public CommandArguments key(Object key) {
    if (keyPreProc != null) {
      key = keyPreProc.actualKey(key);
    }

    if (key instanceof Rawable) {
      Rawable raw = (Rawable) key;
      processKey(raw.getRaw());
      args.add(raw);
    } else if (key instanceof byte[]) {
      byte[] raw = (byte[]) key;
      processKey(raw);
      args.add(RawableFactory.from(raw));
    } else if (key instanceof String) {
      String raw = (String) key;
      processKey(raw);
      args.add(RawableFactory.from(raw));
    } else {
      throw new IllegalArgumentException("\"" + key.toString() + "\" is not a valid argument.");
    }

    addKeyInKeys(key);

    return this;
  }
