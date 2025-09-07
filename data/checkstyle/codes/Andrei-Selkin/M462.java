    private static boolean shouldHaveLineBreakBefore(RightCurlyOption bracePolicy,
                                                     Details details) {
        return bracePolicy == RightCurlyOption.SAME
                && !hasLineBreakBefore(details.rcurly)
                && !TokenUtil.areOnSameLine(details.lcurly, details.rcurly);
    }
