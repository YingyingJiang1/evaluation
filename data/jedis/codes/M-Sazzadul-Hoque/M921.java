  private void addKeyInKeys(Object key) {
    if (keys.isEmpty()) {
      keys = Collections.singletonList(key);
    } else if (keys.size() == 1) {
      List oldKeys = keys;
      keys = new ArrayList();
      keys.addAll(oldKeys);
      keys.add(key);
    } else {
      keys.add(key);
    }
  }
