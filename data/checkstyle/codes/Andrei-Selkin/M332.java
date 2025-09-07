    private static int compareContainerOrder(String importName1, String importName2,
                                             boolean caseSensitive) {
        final String container1 = getImportContainer(importName1);
        final String container2 = getImportContainer(importName2);
        final int compareContainersOrderResult;
        if (caseSensitive) {
            compareContainersOrderResult = container1.compareTo(container2);
        }
        else {
            compareContainersOrderResult = container1.compareToIgnoreCase(container2);
        }
        final int result;
        if (compareContainersOrderResult == 0) {
            result = compare(importName1, importName2, caseSensitive);
        }
        else {
            result = compareContainersOrderResult;
        }
        return result;
    }
