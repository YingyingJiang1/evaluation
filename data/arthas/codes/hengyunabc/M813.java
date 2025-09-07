        @Override
        public int compare(String o1, String o2) {
            if (null == unsortedStats) {
                return -1;
            }
            if (!unsortedStats.containsKey(o1)) {
                return 1;
            }
            if (!unsortedStats.containsKey(o2)) {
                return -1;
            }
            return unsortedStats.get(o2).getLoadedCount() - unsortedStats.get(o1).getLoadedCount();
        }
