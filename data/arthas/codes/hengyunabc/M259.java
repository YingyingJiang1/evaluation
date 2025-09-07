	public static void writeByteArrayToFile(final File file, final byte[] data, final int off, final int len,
			final boolean append) throws IOException {
		FileOutputStream out = null;
		try {
			out = openOutputStream(file, append);
			out.write(data, off, len);
		} finally {
			IOUtils.close(out);
		}
	}
