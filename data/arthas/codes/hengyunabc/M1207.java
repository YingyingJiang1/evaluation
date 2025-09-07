    public String toString(final Map<String, String> map) {

        final StringBuilder featureSB = new StringBuilder().append(kvSegmentSeparator);

        if (null == map
                || map.isEmpty()) {
            return featureSB.toString();
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {

            featureSB
                    .append(escapeEncode(entry.getKey()))
                    .append(kvSeparator)
                    .append(escapeEncode(entry.getValue()))
                    .append(kvSegmentSeparator)
            ;

        }

        return featureSB.toString();
    }
