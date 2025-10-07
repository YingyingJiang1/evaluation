	/**
	 * Reads a line of bytes from the given InputStream.
	 * 
	 * This method reads bytes from the input stream until it encounters a
	 * carriage return (CR, '\r') or line feed (LF, '\n') character, or
	 * the end of the stream. The line terminator itself is not included
	 * in the returned byte array.
	 * 
	 * <p>
	 * Behavior specifics:
	 * <ul>
	 *   <li>Leading LF characters are skipped at the start of reading.</li>
	 *   <li>If the buffer fills up, it is automatically expanded in chunks of 256 bytes.</li>
	 *   <li>If the line ends with CR and the next byte is LF (and the stream supports mark/reset),
	 *       the LF is skipped.</li>
	 *   <li>If the end of the stream is reached before any bytes are read, the method returns null.</li>
	 * </ul>
	 * </p>
	 * 
	 * @param in the InputStream to read from
	 * @return a byte array containing the line read (excluding CR/LF), or null if end-of-stream is reached before any data
	 * @throws IOException if an I/O error occurs while reading from the stream
	 */
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