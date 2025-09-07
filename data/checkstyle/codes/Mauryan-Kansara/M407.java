    private int expandedTabs(String line, int columnNumber) {
        expectedColumnNumberWithoutExpandedTabs = columnNumber - 1;
        return CommonUtil.lengthExpandedTabs(
                    line, columnNumber, getTabWidth());
    }
