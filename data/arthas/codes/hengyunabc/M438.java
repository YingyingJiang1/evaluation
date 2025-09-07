    private void flushAttributes() {
        if (attributeOptions.isEmpty()) {
            return;
        }
        if (attributeOptions.size() == 1 && attributeOptions.get(0) == 0) {
            builder.append(FIRST_ESC_CHAR);
            builder.append(SECOND_ESC_CHAR);
            builder.append('m');
        } else {
            _appendEscapeSequence('m', attributeOptions.toArray());
        }
        attributeOptions.clear();
    }
