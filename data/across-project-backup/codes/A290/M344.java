    public static boolean isADateFormatUncached(Short formatIndex, String formatString) {
        // First up, is this an internal date format?
        if (isInternalDateFormat(formatIndex)) {
            return true;
        }
        if (StringUtils.isEmpty(formatString)) {
            return false;
        }
        String fs = formatString;
        final int length = fs.length();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char c = fs.charAt(i);
            if (i < length - 1) {
                char nc = fs.charAt(i + 1);
                if (c == '\\') {
                    switch (nc) {
                        case '-':
                        case ',':
                        case '.':
                        case ' ':
                        case '\\':
                            // skip current '\' and continue to the next char
                            continue;
                    }
                } else if (c == ';' && nc == '@') {
                    i++;
                    // skip ";@" duplets
                    continue;
                }
            }
            sb.append(c);
        }
        fs = sb.toString();

        // short-circuit if it indicates elapsed time: [h], [m] or [s]
        if (date_ptrn4.matcher(fs).matches()) {
            return true;
        }
        // If it starts with [DBNum1] or [DBNum2] or [DBNum3]
        // then it could be a Chinese date
        fs = date_ptrn5.matcher(fs).replaceAll("");
        // If it starts with [$-...], then could be a date, but
        // who knows what that starting bit is all about
        fs = date_ptrn1.matcher(fs).replaceAll("");
        // If it starts with something like [Black] or [Yellow],
        // then it could be a date
        fs = date_ptrn2.matcher(fs).replaceAll("");
        // You're allowed something like dd/mm/yy;[red]dd/mm/yy
        // which would place dates before 1900/1904 in red
        // For now, only consider the first one
        final int separatorIndex = fs.indexOf(';');
        if (0 < separatorIndex && separatorIndex < fs.length() - 1) {
            fs = fs.substring(0, separatorIndex);
        }

        // Ensure it has some date letters in it
        // (Avoids false positives on the rest of pattern 3)
        if (!date_ptrn3a.matcher(fs).find()) {
            return false;
        }

        // If we get here, check it's only made up, in any case, of:
        // y m d h s - \ / , . : [ ] T
        // optionally followed by AM/PM
        boolean result = date_ptrn3b.matcher(fs).matches();
        if (result) {
            return true;
        }
        result = date_ptrn6.matcher(fs).find();
        return result;
    }
