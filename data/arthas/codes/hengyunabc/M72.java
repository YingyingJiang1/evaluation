    public Map<String, byte[]> getByteCodes() {
        Map<String, byte[]> result = new HashMap<String, byte[]>(byteCodes.size());
        for (Entry<String, MemoryByteCode> entry : byteCodes.entrySet()) {
            result.put(entry.getKey(), entry.getValue().getByteCode());
        }
        return result;
    }
