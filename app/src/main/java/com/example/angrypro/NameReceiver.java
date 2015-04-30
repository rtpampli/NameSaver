package com.example.angrypro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NameReceiver extends BroadcastReceiver {

	public final static String EXTRA_QUERY = "EXTRA_QUERY";
	public static final String NEW_LINEQUERY = "com.example.angrypro.action.NEW_LINEQUERY";
	String FILE = "names.txt";
	File file;

	public void onReceive(Context context, Intent intent) {
		int line = intent.getIntExtra(EXTRA_QUERY, 0); //Reads the line number integer from the broadcast intent
		String result = null;
		file = new File(context.getFilesDir(), FILE); //accesses the names.txt file created in MainActivity
	
		try {
			 result = readLineFromBroadcast(line); //finds the name at line number
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.v("Findthisline", "found name " + result + " at line " + line);
	}
	/**
	 * 
	 * @param linenum (takes in the line number you want to read)
	 * @return (returns the name at the param linenum as a string
	 * @throws IOException
	 */
	public String readLineFromBroadcast(int linenum) throws IOException {
		int inp = linenum;
		String line = null;
		BufferedReader bf = new BufferedReader(new FileReader(file));
		if(inp > countLineNumber()){
			Log.v("Findthisline", "there is not a name at line " + inp);
		}
		else if ((line = bf.readLine()) != null) {
			for (int i = 1; i < inp; i++) {
				line = bf.readLine();
			}
		}
		
		bf.close();
		return line;
	}
	/**
	 * Reads file and returns number of lines
	 */
	public int countLineNumber() {
		int lines = 0;
		try {
			
			LineNumberReader lineNumberReader = new LineNumberReader(
					new FileReader(file));
			lineNumberReader.skip(Long.MAX_VALUE);
			lines = lineNumberReader.getLineNumber();
			lineNumberReader.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException Occured" 
					+ e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException Occured" + e.getMessage());
		}

		return lines + 1;

	}
}
