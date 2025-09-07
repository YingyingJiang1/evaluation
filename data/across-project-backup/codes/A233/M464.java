    private List<Map<String, Object>> extractBookmarkItems(
            PDDocument document, PDDocumentOutline outline) throws Exception {
        List<Map<String, Object>> bookmarks = new ArrayList<>();
        PDOutlineItem current = outline.getFirstChild();

        while (current != null) {
            Map<String, Object> bookmark = new HashMap<>();

            // Get bookmark title
            String title = current.getTitle();
            bookmark.put("title", title);

            // Get page number (1-based for UI purposes)
            PDPage page = current.findDestinationPage(document);
            if (page != null) {
                int pageIndex = document.getPages().indexOf(page);
                bookmark.put("pageNumber", pageIndex + 1);
            } else {
                bookmark.put("pageNumber", 1);
            }

            // Process children if any
            PDOutlineItem child = current.getFirstChild();
            if (child != null) {
                List<Map<String, Object>> children = new ArrayList<>();
                PDOutlineNode parent = current;

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

            bookmarks.add(bookmark);
            current = current.getNextSibling();
        }

        return bookmarks;
    }
