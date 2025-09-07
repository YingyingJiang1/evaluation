    private Format createFormat(Short dataFormat, String dataFormatString) {
        String formatStr = dataFormatString;

        Format format = checkSpecialConverter(formatStr);
        if (format != null) {
            return format;
        }

        // Remove colour formatting if present
        Matcher colourM = colorPattern.matcher(formatStr);
        while (colourM.find()) {
            String colour = colourM.group();

            // Paranoid replacement...
            int at = formatStr.indexOf(colour);
            if (at == -1) {break;}
            String nFormatStr = formatStr.substring(0, at) + formatStr.substring(at + colour.length());
            if (nFormatStr.equals(formatStr)) {break;}

            // Try again in case there's multiple
            formatStr = nFormatStr;
            colourM = colorPattern.matcher(formatStr);
        }

        // Strip off the locale information, we use an instance-wide locale for everything
        Matcher m = localePatternGroup.matcher(formatStr);
        while (m.find()) {
            String match = m.group();
            String symbol = match.substring(match.indexOf('$') + 1, match.indexOf('-'));
            if (symbol.indexOf('$') > -1) {
                symbol = symbol.substring(0, symbol.indexOf('$')) + '\\' + symbol.substring(symbol.indexOf('$'));
            }
            formatStr = m.replaceAll(symbol);
            m = localePatternGroup.matcher(formatStr);
        }

        // Check for special cases
        if (formatStr == null || formatStr.trim().length() == 0) {
            return getDefaultFormat();
        }

        if ("General".equalsIgnoreCase(formatStr) || "@".equals(formatStr)) {
            return getDefaultFormat();
        }

        if (DateUtils.isADateFormat(dataFormat, formatStr)) {
            return createDateFormat(formatStr);
        }
        // Excel supports fractions in format strings, which Java doesn't
        if (formatStr.contains("#/") || formatStr.contains("?/")) {
            String[] chunks = formatStr.split(";");
            for (String chunk1 : chunks) {
                String chunk = chunk1.replaceAll("\\?", "#");
                Matcher matcher = fractionStripper.matcher(chunk);
                chunk = matcher.replaceAll(" ");
                chunk = chunk.replaceAll(" +", " ");
                Matcher fractionMatcher = fractionPattern.matcher(chunk);
                // take the first match
                if (fractionMatcher.find()) {
                    String wholePart = (fractionMatcher.group(1) == null) ? "" : defaultFractionWholePartFormat;
                    return new FractionFormat(wholePart, fractionMatcher.group(3));
                }
            }

            // Strip custom text in quotes and escaped characters for now as it can cause performance problems in
            // fractions.
            // String strippedFormatStr = formatStr.replaceAll("\\\\ ", " ").replaceAll("\\\\.",
            // "").replaceAll("\"[^\"]*\"", " ").replaceAll("\\?", "#");
            return new FractionFormat(defaultFractionWholePartFormat, defaultFractionFractionPartFormat);
        }

        if (numPattern.matcher(formatStr).find()) {
            return createNumberFormat(formatStr);
        }
        return getDefaultFormat();
    }
