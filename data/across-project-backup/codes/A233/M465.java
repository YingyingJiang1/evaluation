    private Map<String, Object> processChild(PDDocument document, PDOutlineItem item)
            throws Exception {
        Map<String, Object> bookmark = new HashMap<>();

        // Get bookmark title
        String title = item.getTitle();
        bookmark.put("title", title);

        // Get page number (1-based for UI purposes)
        PDPage page = item.findDestinationPage(document);
        if (page != null) {
            int pageIndex = document.getPages().indexOf(page);
            bookmark.put("pageNumber", pageIndex + 1);
        } else {
            bookmark.put("pageNumber", 1);
        }

        // Process children if any
        PDOutlineItem child = item.getFirstChild();
        if (child != null) {
            List<Map<String, Object>> children = new ArrayList<>();

            while (child != null) {
                // Recursively process child items
                Map<String, Object> childBookmark = processChild(document, child);
                children.add(childBookmark);
                child = child.getNextSibling();
            }

            bookmark.put("children", children);
        } else {
            bookmark.put("children", new ArrayList<>());
        }

        return bookmark;
    }
