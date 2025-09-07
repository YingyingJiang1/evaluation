    public static String[] splitInvokeInfo(String invokeInfo) {
        int index1 = invokeInfo.indexOf('|');
        int index2 = invokeInfo.indexOf('|', index1 + 1);
        int index3 = invokeInfo.indexOf('|', index2 + 1);
        return new String[] { invokeInfo.substring(0, index1), invokeInfo.substring(index1 + 1, index2),
                invokeInfo.substring(index2 + 1, index3), invokeInfo.substring(index3 + 1) };
    }
