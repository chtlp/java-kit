package net.linpengt.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class CSVReader implements Iterable<List<String>>, Iterator<List<String>> {
	String fileName;
	int skipLines;
	String sep;
	boolean header;
	private BufferedReader br;
	String[] col_names;
	ArrayList<String> row;

	int lines_read;
	String buffered_line;

	public int getSkipLines() {
		return skipLines;
	}

	public void setSkipLines(int skipLines) {
		this.skipLines = skipLines;
	}

	public String getSep() {
		return sep;
	}

	public void setSep(String sep) {
		this.sep = sep;
	}

	public boolean isHeader() {
		return header;
	}

	public void setHeader(boolean header) {
		this.header = header;
	}

	public static CSVReader open(String fileName) {
		try {
			CSVReader reader = new CSVReader(fileName);
			reader.openFile();
			return reader;
		} catch (IOException e) {
			throw new RuntimeException(e.toString());
		}
	}

	private void openFile() throws IOException {
		br = new BufferedReader(new FileReader(fileName));
	}

	private CSVReader(String fileName) {
		this.fileName = fileName;
		header = true;
		sep = ",";
		skipLines = 0;

		lines_read = -1;
		buffered_line = null;
	}

	/**
	 * read one line, throws RuntimeException on errors
	 * @return the line read
	 */
	private String readLine() {
		try {
			return br.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e.toString());
		}
	}

	/**
	 * skip lines and read headers
	 * 
	 * @throws IOException
	 */
	private void init() {
		for (int i = 0; i < skipLines; ++i) {
			readLine();
		}
		if (header)
			col_names = readLine().split(sep);
	}
	
	@Override
	public boolean hasNext() {
		if (lines_read == -1) {
			init();
			lines_read = 0;
		}
		buffered_line = readLine();
		if (buffered_line != null)
			++lines_read;
		else {
			try {
				br.close();
			} catch (IOException e) {
				throw new RuntimeException(e.toString());
			}
		}
		return buffered_line != null;
	}


	@Override
	public List<String> next() {
		if (buffered_line == null)
			return null;
		
		String[] ele = buffered_line.split(sep);
		if (row == null) {
			row = new ArrayList<String>();
			for (int i=0; i<ele.length; ++i)
				row.add(null);
		}
		for (int i=0; i<ele.length; ++i)
			row.set(i, ele[i]);
		return row;
	}

	@Override
	public void remove() {
		throw new NotImplementedException();
	}

	@Override
	public Iterator<List<String>> iterator() {
		return this;
	}
}
