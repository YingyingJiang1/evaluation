    public List<JvmItemVO> group(String group) {
        synchronized (this) {
            List<JvmItemVO> list = jvmInfo.get(group);
            if (list == null) {
                list = new ArrayList<JvmItemVO>();
                jvmInfo.put(group, list);
            }
            return list;
        }
    }
