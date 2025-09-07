    public static synchronized void addRetransformEntry(List<RetransformEntry> retransformEntryList) {
        List<RetransformEntry> tmp = new ArrayList<RetransformEntry>();
        tmp.addAll(retransformEntries);
        tmp.addAll(retransformEntryList);
        Collections.sort(tmp, new Comparator<RetransformEntry>() {
            @Override
            public int compare(RetransformEntry entry1, RetransformEntry entry2) {
                return Integer.compare(entry1.getId(), entry2.getId());
            }
        });
        retransformEntries = tmp;
    }
