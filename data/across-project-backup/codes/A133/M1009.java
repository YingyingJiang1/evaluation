    @Override
    public String apply(String input) {
        StringBuilder output = new StringBuilder();
        String[] lines = input.split("\n");
        int continueCount = 0;
        int lastStartPos = 0;
        int lastContinueLineNum = -1;
        int matchCount = 0;
        for (int lineNum = 0; lineNum < lines.length;) {
            String line = null;
            if (this.trimEnd) {
                line = StringUtils.stripEnd(lines[lineNum], null);
            } else {
                line = lines[lineNum];
            }
            lineNum++;

            final boolean match;
            if (pattern == null) {
                match = (ignoreCase ? line.toLowerCase() : line).contains(keyword);
            } else {
                match = pattern.matcher(line).find();
            }
            if (invertMatch != match) {
                matchCount++;
                if (beforeLines > continueCount) {
                    int n = lastContinueLineNum == -1 ? (beforeLines >= lineNum ? 1 : lineNum - beforeLines)
                                    : lineNum - beforeLines - continueCount;
                    if (n >= lastContinueLineNum || lastContinueLineNum == -1) {
                        StringBuilder beforeSb = new StringBuilder();
                        for (int i = n; i < lineNum; i++) {
                            appendLine(beforeSb, i, lines[i - 1]);
                        }
                        output.insert(lastStartPos, beforeSb);
                    }
                } // end handle before lines

                lastStartPos = output.length();
                appendLine(output, lineNum, line);

                if (afterLines > continueCount) {
                    int last = lineNum + afterLines - continueCount;
                    if (last > lines.length) {
                        last = lines.length;
                    }
                    for (int i = lineNum; i < last; i++) {
                        appendLine(output, i + 1, lines[i]);
                        lineNum++;
                        continueCount++;
                        lastStartPos = output.length();
                    }
                } // end handle afterLines

                continueCount++;
                if (maxCount > 0 && matchCount >= maxCount) {
                    break;
                }
            } else { // not match
                if (continueCount > 0) {
                    lastContinueLineNum = lineNum - 1;
                    continueCount = 0;
                }
            }
        }
        final String str = output.toString();
        return str;
    }
