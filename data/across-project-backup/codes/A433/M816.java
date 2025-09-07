    private Tfhd parseTfhd(final int trackId) throws IOException {
        final Tfhd obj = new Tfhd();

        obj.bFlags = stream.readInt();
        obj.trackId = stream.readInt();

        if (trackId != -1 && obj.trackId != trackId) {
            return null;
        }

        if (hasFlag(obj.bFlags, 0x01)) {
            stream.skipBytes(8);
        }
        if (hasFlag(obj.bFlags, 0x02)) {
            stream.skipBytes(4);
        }
        if (hasFlag(obj.bFlags, 0x08)) {
            obj.defaultSampleDuration = stream.readInt();
        }
        if (hasFlag(obj.bFlags, 0x10)) {
            obj.defaultSampleSize = stream.readInt();
        }
        if (hasFlag(obj.bFlags, 0x20)) {
            obj.defaultSampleFlags = stream.readInt();
        }

        return obj;
    }
