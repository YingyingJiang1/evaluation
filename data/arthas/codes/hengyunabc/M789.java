    public static synchronized RetransformEntry deleteRetransformEntry(int id) {
        RetransformEntry result = null;
        List<RetransformEntry> tmp = new ArrayList<RetransformEntry>();
        for (RetransformEntry entry : retransformEntries) {
            if (entry.getId() != id) {
                tmp.add(entry);
            } else {
                result = entry;
            }
        }
        retransformEntries = tmp;
        return result;
    }
