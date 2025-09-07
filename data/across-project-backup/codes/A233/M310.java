    public static PDRectangle textToPageSize(String size) {
        switch (size.toUpperCase()) {
            case "A0" -> {
                return PDRectangle.A0;
            }
            case "A1" -> {
                return PDRectangle.A1;
            }
            case "A2" -> {
                return PDRectangle.A2;
            }
            case "A3" -> {
                return PDRectangle.A3;
            }
            case "A4" -> {
                return PDRectangle.A4;
            }
            case "A5" -> {
                return PDRectangle.A5;
            }
            case "A6" -> {
                return PDRectangle.A6;
            }
            case "LETTER" -> {
                return PDRectangle.LETTER;
            }
            case "LEGAL" -> {
                return PDRectangle.LEGAL;
            }
            default -> throw ExceptionUtils.createInvalidPageSizeException(size);
        }
    }
