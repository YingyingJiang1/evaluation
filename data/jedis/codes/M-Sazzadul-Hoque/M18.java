        public CacheConfig build() {
            CacheConfig cacheConfig = new CacheConfig();
            cacheConfig.maxSize = this.maxSize;
            cacheConfig.cacheable = this.cacheable;
            cacheConfig.evictionPolicy = this.evictionPolicy;
            cacheConfig.cacheClass = this.cacheClass;
            return cacheConfig;
        }
