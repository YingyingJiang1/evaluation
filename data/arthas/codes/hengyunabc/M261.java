    public static byte[] readFileToByteArray(final File file) throws IOException {
    	InputStream in = null;
    	try {
    		in = new FileInputStream(file);
    		return IOUtils.getBytes(in);
		} finally {
			IOUtils.close(in);
		}
    }
