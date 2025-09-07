    @Override
    public synchronized List<CacheKey> evictMany(int n) {
        List<CacheKey> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            result.add(this.evictNext());
        }
        return result;
    }
