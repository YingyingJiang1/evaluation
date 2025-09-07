  public HostAndPort getSlotNode(int slot) {
    r.lock();
    try {
      return slotNodes[slot];
    } finally {
      r.unlock();
    }
  }
