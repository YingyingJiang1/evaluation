    private boolean getNextFrameBytes(byte[] inBytes) {
        // Firstbyte should be 0x00 (for this to be a DATA frame)
        int firstByteValue = inBytes[mReadSoFar] | DATA_BYTE;
        if (firstByteValue != 0) {
            logger.debug("done with DATA bytes");
            return false;
        }

        // Next 4 bytes = length of the bytes array starting after the 4 bytes.
        int offset = mReadSoFar + 1;
        int len = ByteBuffer.wrap(inBytes, offset, 4).getInt();

        // Empty message is special case.
        // TODO: Can this is special handling be removed?
        if (len == 0) {
            mFrames.add(new byte[0]);
            return false;
        }

        // Make sure we have enough bytes in the inputstream
        int expectedNumBytes = len + 5 + mReadSoFar;
        if (inBytes.length < expectedNumBytes) {
            logger.warn(String.format("input doesn't have enough bytes. expected: %d, found %d", expectedNumBytes,
                    inBytes.length));
            return false;
        }

        // Read "len" bytes into message
        mLength += len;
        offset += 4;
        byte[] inputBytes = Arrays.copyOfRange(inBytes, offset, len + offset);
        mFrames.add(inputBytes);
        mReadSoFar += (len + 5);
        // we have more frames to process, if there are bytes unprocessed
        return inBytes.length > mReadSoFar;
    }
