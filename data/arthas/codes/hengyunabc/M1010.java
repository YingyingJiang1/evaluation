    protected void appendLine(StringBuilder output, int lineNum, String line) {
        if (showLineNumber) {
            output.append(lineNum).append(':');
        }
        output.append(line).append('\n');
    }
