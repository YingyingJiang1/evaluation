    @Override
    public StringBuffer format(Object number, StringBuffer toAppendTo, FieldPosition pos) {
        final double value;
        if (number instanceof Number) {
            value = ((Number) number).doubleValue();
            if (Double.isInfinite(value) || Double.isNaN(value)) {
                return integerFormat.format(number, toAppendTo, pos);
            }
        } else {
            // testBug54786 gets here with a date, so retain previous behaviour
            return integerFormat.format(number, toAppendTo, pos);
        }

        final double abs = Math.abs(value);
        if (abs >= 1E11 || (abs <= 1E-10 && abs > 0)) {
            return scientificFormat.format(number, toAppendTo, pos);
        } else if (Math.floor(value) == value || abs >= 1E10) {
            // integer, or integer portion uses all 11 allowed digits
            return integerFormat.format(number, toAppendTo, pos);
        }
        // Non-integers of non-scientific magnitude are formatted as "up to 11
        // numeric characters, with the decimal point counting as a numeric
        // character". We know there is a decimal point, so limit to 10 digits.
        // https://support.microsoft.com/en-us/kb/65903
        final double rounded = new BigDecimal(value).round(TO_10_SF).doubleValue();
        return decimalFormat.format(rounded, toAppendTo, pos);
    }
