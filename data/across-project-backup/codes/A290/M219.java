    private Format createNumberFormat(String formatStr) {
        String format = cleanFormatForNumber(formatStr);
        DecimalFormatSymbols symbols = decimalSymbols;

        // Do we need to change the grouping character?
        // eg for a format like #'##0 which wants 12'345 not 12,345
        Matcher agm = alternateGrouping.matcher(format);
        if (agm.find()) {
            char grouping = agm.group(2).charAt(0);
            // Only replace the grouping character if it is not the default
            // grouping character for the US locale (',') in order to enable
            // correct grouping for non-US locales.
            if (grouping != ',') {
                symbols = DecimalFormatSymbols.getInstance(locale);

                symbols.setGroupingSeparator(grouping);
                String oldPart = agm.group(1);
                String newPart = oldPart.replace(grouping, ',');
                format = format.replace(oldPart, newPart);
            }
        }

        try {
            return new InternalDecimalFormatWithScale(format, symbols);
        } catch (IllegalArgumentException iae) {
            LOGGER.error("Formatting failed for format {}, falling back", formatStr, iae);
            // the pattern could not be parsed correctly,
            // so fall back to the default number format
            return getDefaultFormat();
        }
    }
