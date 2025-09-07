    private List<TimeFragmentVO> createTimeTunnelVOList(Map<Integer, TimeFragment> timeFragmentMap) {
        List<TimeFragmentVO> timeFragmentList = new ArrayList<TimeFragmentVO>(timeFragmentMap.size());
        for (Map.Entry<Integer, TimeFragment> entry : timeFragmentMap.entrySet()) {
            timeFragmentList.add(createTimeFragmentVO(entry.getKey(), entry.getValue(), expand));
        }
        return timeFragmentList;
    }
