    private static List<Element> getDirectChildsByTag(Element element, String sTagName) {
        final NodeList children = element.getElementsByTagName(sTagName);
        final List<Element> res = new ArrayList<>();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getParentNode().equals(element)) {
                res.add((Element) children.item(i));
            }
        }
        return res;
    }
