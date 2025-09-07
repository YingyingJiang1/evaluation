  private List<ConnectionPool> getShuffledNodesPool() {
    List<ConnectionPool> pools = new ArrayList<>(resources.values());
    Collections.shuffle(pools);
    return pools;
  }
