	public static byte[] readLine(InputStream in) throws IOException {
		byte[] buf = new byte[256];
		int i = 0;
		int b;
		
		// Skip leading LF characters
		while ((b = in.read()) == LF);
		
		// Return null if we hit EOF before reading any data
		if (b < 0)
			return null;
		
		// Process the first byte (which we already read)
		if (b == CR) {
			// If CR and stream supports mark/reset, check for following LF
			if (in.markSupported()) {
				in.mark(1);
				int b2 = in.read();
				if (b2 != LF)
					in.reset();
			}
			return new byte[0];
		}
		
		// Store the first byte
		buf[i++] = (byte) b;
		
		// Read until CR, LF, or EOF
		while ((b = in.read()) >= 0) {
			if (b == CR || b == LF)
				break;
			
			// Expand buffer if needed
			if (i >= buf.length) {
				byte[] newBuf = new byte[buf.length + 256];
				System.arraycopy(buf, 0, newBuf, 0, buf.length);
				buf = newBuf;
			}
			
			buf[i++] = (byte) b;
		}
		
		// If CR and stream supports mark/reset, check for following LF
		if (b == CR && in.markSupported()) {
			in.mark(1);
			int b2 = in.read();
			if (b2 != LF)
				in.reset();
		}
		
		// Create result array of exact length
		byte[] result = new byte[i];
		System.arraycopy(buf, 0, result, 0, i);
		return result;
	}