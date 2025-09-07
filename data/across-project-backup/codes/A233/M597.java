    private List<Integer> duplicate(int totalPages, String pageOrder) {
        List<Integer> newPageOrder = new ArrayList<>();
        int duplicateCount;

        try {
            // Parse the duplicate count from pageOrder
            duplicateCount =
                    pageOrder != null && !pageOrder.isEmpty()
                            ? Integer.parseInt(pageOrder.trim())
                            : 2; // Default to 2 if not specified
        } catch (NumberFormatException e) {
            log.error("Invalid duplicate count specified", e);
            duplicateCount = 2; // Default to 2 if invalid input
        }

        // Validate duplicate count
        if (duplicateCount < 1) {
            duplicateCount = 2; // Default to 2 if invalid input
        }

        // For each page in the document
        for (int pageNum = 0; pageNum < totalPages; pageNum++) {
            // Add the current page index duplicateCount times
            for (int dupCount = 0; dupCount < duplicateCount; dupCount++) {
                newPageOrder.add(pageNum);
            }
        }

        return newPageOrder;
    }
