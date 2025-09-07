    private static void complete(Completion completion, String prefix, List<String> candidates) {
        if (candidates.size() == 1) {
            completion.complete(candidates.get(0).substring(prefix.length()), true);
        } else {
            String commonPrefix = CompletionUtils.findLongestCommonPrefix(candidates);
            if (commonPrefix.length() > 0) {
                if (commonPrefix.length() == prefix.length()) {
                    completion.complete(candidates);
                } else {
                    completion.complete(commonPrefix.substring(prefix.length()), false);
                }

            } else {
                completion.complete(candidates);
            }
        }
    }
