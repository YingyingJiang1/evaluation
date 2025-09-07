    private String getDescriptionText() {
        final int descriptionEndIdx;
        if (propertySectionStartIdx > -1) {
            descriptionEndIdx = propertySectionStartIdx;
        }
        else if (exampleSectionStartIdx > -1) {
            descriptionEndIdx = exampleSectionStartIdx;
        }
        else {
            descriptionEndIdx = parentSectionStartIdx;
        }
        return constructSubTreeText(rootNode, 0, descriptionEndIdx - 1);
    }
