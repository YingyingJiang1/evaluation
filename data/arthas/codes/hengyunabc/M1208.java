    public Map<String, String> toMap(final String featureString) {

        final Map<String, String> map = new HashMap<String, String>();

        if (isBlank(featureString)) {
            return map;
        }

        for (String kv : escapeSplit(featureString, kvSegmentSeparator)) {

            if (isBlank(kv)) {
                // 过滤掉为空的字符串片段
                continue;
            }

            final String[] ar = escapeSplit(kv, kvSeparator);
            if (ar.length != 2) {
                // 过滤掉不符合K:V单目的情况
                continue;
            }

            final String k = ar[0];
            final String v = ar[1];
            if (!isBlank(k)
                    && !isBlank(v)) {
                map.put(escapeDecode(k), escapeDecode(v));
            }

        }

        return map;
    }
