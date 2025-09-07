    public void fill(Object data, FillConfig fillConfig) {
        if (data == null) {
            data = new HashMap<String, Object>(16);
        }
        if (fillConfig == null) {
            fillConfig = FillConfig.builder().build();
        }
        fillConfig.init();

        Object realData;
        // The data prefix that is populated this time
        String currentDataPrefix;

        if (data instanceof FillWrapper) {
            FillWrapper fillWrapper = (FillWrapper)data;
            currentDataPrefix = fillWrapper.getName();
            realData = fillWrapper.getCollectionData();
        } else {
            realData = data;
            currentDataPrefix = null;
        }
        currentUniqueDataFlag = uniqueDataFlag(writeContext.writeSheetHolder(), currentDataPrefix);

        // processing data
        if (realData instanceof Collection) {
            List<AnalysisCell> analysisCellList = readTemplateData(templateCollectionAnalysisCache);
            Collection<?> collectionData = (Collection<?>)realData;
            if (CollectionUtils.isEmpty(collectionData)) {
                return;
            }
            Iterator<?> iterator = collectionData.iterator();
            if (WriteDirectionEnum.VERTICAL.equals(fillConfig.getDirection()) && fillConfig.getForceNewRow()) {
                shiftRows(collectionData.size(), analysisCellList);
            }
            while (iterator.hasNext()) {
                doFill(analysisCellList, iterator.next(), fillConfig, getRelativeRowIndex());
            }
        } else {
            doFill(readTemplateData(templateAnalysisCache), realData, fillConfig, null);
        }
    }
