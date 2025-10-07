	public static byte[] readLine(InputStream in) throws IOException {
		if (in == null)
			throw new IllegalArgumentException("InputStream cannot be null");
		byte[] buffer = new byte[256];
		int count = 0;
		boolean firstByte = true;
		while (true) {
			int b = in.read();
			if (b == -1) {
				if (count == 0)
					return null;
				break;
			}
			// Skip leading LF characters
			if (firstByte && b == LF)
				continue;
			firstByte = false;
			if (b == CR || b == LF)
				break;
			if (count == buffer.length) {
				byte[] newBuffer = new byte[buffer.length + 256];
				System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
				buffer = newBuffer;
			}
			buffer[count++] = (byte) b;
		}
		// If line ended with CR and next is LF, skip the LF if possible
		if (count > 0 && buffer[count - 1] == CR) {
			in.mark(1);
			int next = in.read();
			if (next != LF && next != -1) {
				in.reset();
			}
		}
		if (count == 0)
			return null;
		return Arrays.copyOf(buffer, count);
	}