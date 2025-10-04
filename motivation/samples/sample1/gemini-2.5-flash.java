	public static byte[] readLine(InputStream in) throws IOException {
		byte[] buffer = new byte[256];
		int     offset = 0;
		int     ch;
		boolean gotData = false;

		// Skip leading LFs
		while ((ch = in.read()) != -1 && ch == LF) {
			// Just skip
		}

		if (ch == -1) {
			return null; // End of stream before any data
		}

		// Read the first character (which is not LF)
		if (ch != CR && ch != LF) {
			buffer[offset++] = (byte) ch;
			gotData = true;
		} else if (ch == CR) {
			// If CR, check for LF
			if (in.markSupported()) {
				in.mark(1);
				if (in.read() != LF) {
					in.reset();
				}
			}
			return gotData ? Arrays.copyOf(buffer, offset) : new byte[0]; // Line ends with CR
		} else if (ch == LF) {
			return gotData ? Arrays.copyOf(buffer, offset) : new byte[0]; // Line ends with LF
		}

		while ((ch = in.read()) != -1) {
			if (ch == CR) {
				if (in.markSupported()) {
					in.mark(1);
					if (in.read() != LF) {
						in.reset();
					}
				}
				break; // End of line
			} else if (ch == LF) {
				break; // End of line
			}

			if (offset == buffer.length) {
				buffer = Arrays.copyOf(buffer, buffer.length + 256);
			}
			buffer[offset++] = (byte) ch;
			gotData = true;
		}

		if (!gotData && ch == -1) {
			return null; // End of stream before any data
		}

		return Arrays.copyOf(buffer, offset);
	}
