	public static byte[] readLine(InputStream in) throws IOException {
		byte[] buf = new byte[256];
		int room = buf.length;
		int offset = 0;
		int chr;
		loop: while (true) {
			chr = in.read();
			switch (chr) {
				case -1:
					break loop;
				case CR:
					chr = in.read();
					if (chr == LF)
						break loop;
					if (chr == -1)
						break loop;
					((InputStream) in).reset();
					break loop;
				case LF:
					break loop;
				default:
					if (room == 0) {
						room = buf.length;
						byte[] new_buf = new byte[room + 256];
						System.arraycopy(buf, 0, new_buf, 0, buf.length);
						buf = new_buf;
					}
					buf[offset++] = (byte) chr;
					room--;
			}
		}
		if (offset == 0 && chr == -1) {
			return null;
		}
		byte[] new_buf = new byte[offset];
		System.arraycopy(buf, 0, new_buf, 0, offset);
		return new_buf;
	}