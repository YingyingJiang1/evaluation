  private HostAndPort getNodeFromHash(Long hash) {
    SortedMap<Long, HostAndPort> tail = nodes.tailMap(hash);
    if (tail.isEmpty()) {
      return nodes.get(nodes.firstKey());
    }
    return tail.get(tail.firstKey());
  }
