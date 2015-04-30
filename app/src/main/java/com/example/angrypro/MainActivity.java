package com.example.angrypro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private NameReceiver receiver = new NameReceiver();
	EditText et;
	TextView tv;
	String text, data, complete;
	AssetManager assetManager;
	InputStream input;
	FileOutputStream output;
	String FILE = "names.txt";
	File file = new File(FILE);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		et = (EditText) findViewById(R.id.editText1);
		tv = (TextView) findViewById(R.id.textView1);

		assetManager = getAssets();
		file = getBaseContext().getFileStreamPath(FILE);
		if (file.isFile()) { //If the file exists in internal storage already it ignores the read from assets
		} else {
			try {
				input = assetManager.open("names.txt");//Reads original file from assets and
				int size = input.available();		   //saves it to a string
				byte[] buffer = new byte[size];
				input.read(buffer);
				input.close();

				// byte buffer into a string
				text = new String(buffer);
				complete = text;
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				FileOutputStream fOut = openFileOutput(FILE,//Creates a new file in internal storage for reading
						MODE_WORLD_WRITEABLE);				//and stores the string created from assets into it
				fOut.write(complete.getBytes());
				fOut.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
/**
 * Takes text from editText and saves it to the file. Checks for blank input and confirms save
 * @param v
 * @throws IOException
 */
	public void save(View v) throws IOException {
		if (et.getText().toString().isEmpty()) {
			Toast.makeText(getBaseContext(), "Please enter a name",
					Toast.LENGTH_SHORT).show();
		} else if (find(v) == false) {
			data = "\n" + et.getText().toString();

			try {
				FileOutputStream fOut = openFileOutput(FILE, MODE_APPEND); //writes to file
				fOut.write(data.getBytes());
				fOut.close();
				Toast.makeText(getBaseContext(), "file saved",
						Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
/**
 * Reads all text from text file, displays in TextView and confirms read.
 * @param v
 */
	public void read(View v) {
		try {
			FileInputStream fin = openFileInput(FILE);
			int c;
			String temp = "";
			while ((c = fin.read()) != -1) {  //reads from file
				temp = temp + Character.toString((char) c);
			}
			tv.setText(temp);
			Toast.makeText(getBaseContext(), "file read", Toast.LENGTH_SHORT)
					.show();

		} catch (Exception e) {

		}
	}

/**
 * Determines if name already exists by comparing entered string to each line in text file
 * @param v
 * @return
 * @throws IOException
 */
	public boolean find(View v) throws IOException {
		String inp = et.getText().toString();
		String line;
		Boolean b = false;
		BufferedReader bf = new BufferedReader(new FileReader(file));
		while ((line = bf.readLine()) != null) {
			if (line.equalsIgnoreCase(inp)) {
				Toast.makeText(getBaseContext(), "the name already exists!",
						Toast.LENGTH_SHORT).show();
				b = true;
			}
		}
		bf.close();
		return b;
	}
/**
 * Finds name in text file at the given line number. Gives error if input is blank or not a number, or if a name does not exist at the given line
 * @param v
 * @throws IOException
 */
	public void readLine(View v) throws IOException {
		String testRegex = "^\\d+"; // Used to compare input for a number
		int inp;
		String line;
		BufferedReader bf = new BufferedReader(new FileReader(file));
		if (!(et.getText().toString().matches(testRegex))) {
			Toast.makeText(getBaseContext(), "Please insert a line number",
					Toast.LENGTH_SHORT).show();
			tv.setText("");
		} else {
			inp = Integer.parseInt(et.getText().toString());
			if (inp > countLineNumber()) {
				tv.setText("");
				Toast.makeText(getBaseContext(),
						"A name does not exist at this line",
						Toast.LENGTH_SHORT).show();
			} else if ((line = bf.readLine()) != null) {
				for (int i = 1; i < inp; i++) {
					line = bf.readLine();
				}
				tv.setText(line);
			}
		}

		bf.close();
	}
/**
 * Counts the number of lines and returns as an integer for comparison in readLine()
 * @return
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
			System.out
					.println("FileNotFoundException Occured" + e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException Occured" + e.getMessage());
		}

		return lines + 1;

	}
}
