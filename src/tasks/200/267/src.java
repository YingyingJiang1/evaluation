    private void init() throws IOException {
	File f = new File(file);
	RandomAccessFileReader in = new RandomAccessFileReader(f);
	SimpleDateFormat dateformat = new SimpleDateFormat(DATE_FORMAT);
	Pattern idp = Pattern.compile("\\[myid:(\\d+)\\]");

	long lastFp = in.getPosition();
	String line = in.readLine();
	Matcher m = null;

	// if we have read data from the file, and it matches the timep pattern
	if ((line != null) && (m = timep.matcher(line)).lookingAt()) {
	    starttime = timestampFromText(dateformat, m.group(1));
	} else {
	    throw new IOException("Invalid log format. First line doesn't start with time");
	}

	/*
	  Count number of log entries. Any line starting with a timestamp counts as an entry
	*/
	String lastentry = line;
	try {
	    while (line != null) {
		m = timep.matcher(line);
		if (m.lookingAt()) {
		    if (size % skipN == 0) {
			long time = timestampFromText(dateformat, m.group(1));
			skiplist.addMark(time, lastFp, size);
		    }
		    size++;
		    lastentry = line;
		} 
		if (serverid == 0 && (m = idp.matcher(line)).find()) {
		    serverid = Integer.valueOf(m.group(1));
		}

		lastFp = in.getPosition();
		line = in.readLine();
	    }
	} catch (EOFException eof) {
	    // ignore, simply end of file, though really (line!=null) should have caught this
	} finally {
	    in.close();
	}

	m = timep.matcher(lastentry);
	if (m.lookingAt()) {
	    endtime = timestampFromText(dateformat, m.group(1));
	} else {
	    throw new IOException("Invalid log format. Last line doesn't start with time");
	}
    }
