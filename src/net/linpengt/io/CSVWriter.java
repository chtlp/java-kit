package net.linpengt.io;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVWriter {
	String fileName;
	String sep;
	boolean header, header_written;
	String[] col_names;
	PrintWriter pw;

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

	public String[] getColNames() {
		return col_names;
	}

	public void setColNames(String[] col_names) {
		this.col_names = Arrays.copyOf(col_names, col_names.length);
	}

	public static CSVWriter open(String fileName) {
		try {
			CSVWriter writer = new CSVWriter(fileName);
			writer.openFile();
			return writer;
		} catch (IOException e) {
			throw new RuntimeException(e.toString());
		}
	}

	private void openFile() throws IOException {
		pw = new PrintWriter(new FileWriter(fileName));
	}

	private CSVWriter(String fileName) {
		this.fileName = fileName;
		header = true;
		sep = ",";
		col_names = null;
	}
	
	private void writeHeader() {
		if (!header_written && header) {
			for(int i=0; i<col_names.length; ++i) {
				pw.format("%s%s", col_names[i], i+1 < col_names.length ? sep : "\n");
			}
			header_written = true;
		}		
	}
	
	public void writeRow(List<String> row) {
		writeHeader();
		int num_cols = row.size();
		for(int j=0; j<num_cols; ++j) {
			pw.format("%s%s", row.get(j), j+1 < num_cols ? sep : "\n");
		}
	}
	
	public void writeRows(ArrayList<ArrayList<? extends Object>> data) {
		writeHeader();

		int num_cols = data.size();
		int num_rows = data.get(0).size();
		for (ArrayList<? extends Object> c : data) {
			assert c.size() == num_rows;
		}
		
		for (int i=0; i<num_rows; ++i) {
			for(int j=0; j<num_cols; ++j) {
				pw.format("%s%s", data.get(j).get(i), j+1 <  num_cols ? sep : "\n");
			}
			
		}
	}
	
	public void close() {
		pw.close();
	}

	
}
