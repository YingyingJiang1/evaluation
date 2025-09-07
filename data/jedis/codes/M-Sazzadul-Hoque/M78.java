    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Long> build(Object data) {
      final List<Object> flatHash = (List<Object>) data;
      final Map<String, Long> hash = new HashMap<>(flatHash.size() / 2, 1f);
      final Iterator<Object> iterator = flatHash.iterator();
      while (iterator.hasNext()) {
        hash.put(SafeEncoder.encode((byte[]) iterator.next()), (Long) iterator.next());
      }
      return hash;
    }
