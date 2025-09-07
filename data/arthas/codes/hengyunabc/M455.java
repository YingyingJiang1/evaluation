    private void appendStringBuilder(StringBuilder buf, String data) throws ObjectTooLargeException {
        if (buf.length() + data.length() > maxObjectLength) {
            throw new ObjectTooLargeException("Object size exceeds size limit: " + maxObjectLength);
        }
        buf.append(data);
    }
