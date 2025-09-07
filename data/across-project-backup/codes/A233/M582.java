    private static List<Bookmark> extractOutlineItems(
            PDDocument sourceDocument,
            PDOutlineItem current,
            List<Bookmark> bookmarks,
            PDOutlineItem nextParent,
            int level,
            int maxLevel)
            throws Exception {

        while (current != null) {

            String currentTitle = current.getTitle().replace("/", "");
            int firstPage =
                    sourceDocument.getPages().indexOf(current.findDestinationPage(sourceDocument));
            PDOutlineItem child = current.getFirstChild();
            PDOutlineItem nextSibling = current.getNextSibling();
            int endPage;
            if (child != null && level < maxLevel) {
                endPage =
                        sourceDocument
                                .getPages()
                                .indexOf(child.findDestinationPage(sourceDocument));
            } else if (nextSibling != null) {
                endPage =
                        sourceDocument
                                .getPages()
                                .indexOf(nextSibling.findDestinationPage(sourceDocument));
            } else if (nextParent != null) {

                endPage =
                        sourceDocument
                                .getPages()
                                .indexOf(nextParent.findDestinationPage(sourceDocument));
            } else {
                endPage = -2;
                /*
                happens when we have something like this:
                Outline Item 2
                    Outline Item 2.1
                        Outline Item 2.1.1
                    Outline Item 2.2
                        Outline 2.2.1
                        Outline 2.2.2 <--- this item neither has an immediate next parent nor an immediate next sibling
                Outline Item 3
                 */
            }
            if (!bookmarks.isEmpty()
                    && bookmarks.get(bookmarks.size() - 1).getEndPage() == -2
                    && firstPage
                            >= bookmarks
                                    .get(bookmarks.size() - 1)
                                    .getStartPage()) { // for handling the above-mentioned case
                Bookmark previousBookmark = bookmarks.get(bookmarks.size() - 1);
                previousBookmark.setEndPage(firstPage);
            }
            bookmarks.add(new Bookmark(currentTitle, firstPage, endPage));

            // Recursively process children
            if (child != null && level < maxLevel) {
                extractOutlineItems(
                        sourceDocument, child, bookmarks, nextSibling, level + 1, maxLevel);
            }

            current = nextSibling;
        }
        return bookmarks;
    }
