    private static List<Integer> handlePart(String part, int totalPages, int offset) {
        List<Integer> partResult = new ArrayList<>();

        // First check for n-syntax because it should not be processed as a range
        if (part.contains("n")) {
            partResult = evaluateNFunc(part, totalPages);
            // Adjust the results according to the offset
            for (int i = 0; i < partResult.size(); i++) {
                int adjustedValue = partResult.get(i) - 1 + offset;
                partResult.set(i, adjustedValue);
            }
        } else if (part.contains("-")) {
            // Process ranges only if it's not n-syntax
            String[] rangeParts = part.split("-");
            try {
                int start = Integer.parseInt(rangeParts[0]);
                int end =
                        (rangeParts.length > 1 && !rangeParts[1].isEmpty())
                                ? Integer.parseInt(rangeParts[1])
                                : totalPages;
                for (int i = start; i <= end; i++) {
                    if (i >= 1 && i <= totalPages) {
                        partResult.add(i - 1 + offset);
                    }
                }
            } catch (NumberFormatException e) {
                // Range is invalid, ignore this part
            }
        } else {
            // This is a single page number
            try {
                int pageNum = Integer.parseInt(part.trim());
                if (pageNum >= 1 && pageNum <= totalPages) {
                    partResult.add(pageNum - 1 + offset);
                }
            } catch (NumberFormatException ignored) {
                // Ignore invalid numbers
            }
        }
        return partResult;
    }
