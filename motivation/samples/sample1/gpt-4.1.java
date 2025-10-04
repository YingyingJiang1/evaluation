	public static byte[] readLine(InputStream in) throws IOException {
		if (in == null)
			throw new IllegalArgumentException("InputStream is null");
		int c;
		// skip leading LF characters
		do {
			in.mark(1);
			c = in.read();
		} while (c == LF);
		if (c == -1)
			return null;
		int capacity = 256;
		byte[] buffer = new byte[capacity];
		int count = 0;
		while (c != -1 && c != CR && c != LF) {
			if (count == capacity) {
				capacity += 256;
				buffer = Arrays.copyOf(buffer, capacity);
			}
			buffer[count++] = (byte) c;
			c = in.read();
		}
		// If CR and next is LF, skip it if possible
		if (c == CR) {
			in.mark(1);
			int next = in.read();
			if (next != LF && next != -1) {
				in.reset();
			}
		}
		if (count == 0 && c == -1)
			return null;
		return Arrays.copyOf(buffer, count);
	}