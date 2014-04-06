package metrics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.csvreader.CsvWriter;


public   class ToFile {

	private File Text_file;
	private File CSV_file;
	private FileWriter writer = null;
	private CsvWriter csv_writer =null;
	boolean alreadyExists= false;
	private String  version;
	private Integer number=1;
	
	public ToFile(String file_name, String metrics_name, String prog_version) 
	{
		System.out.println("Enter the version number...");
		Scanner input = new Scanner(System.in);
		String Version=input.next();
		version=Version;//prog_version;
		Text_file= new File("Calculations_files//"+file_name+version+".txt");
		CSV_file=new File("Calculations_files//"+file_name+".csv");
		alreadyExists = CSV_file.exists();
		try {
			csv_writer = new CsvWriter(new FileWriter(CSV_file, true), ',');
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!alreadyExists)
		{
			try {
				csv_writer.write("#");
				csv_writer.write("jedit_Verion");
				csv_writer.write(metrics_name);
				csv_writer.endRecord();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			writer = new FileWriter(Text_file,true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			writer.write("Calculation for "+metrics_name);
			writer.	write(System.getProperty("line.separator"));
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void TextFile_print(String output) throws IOException
	{

		writer.write(output);
		writer.	write(System.getProperty("line.separator"));
		writer.flush();

	}
	public void CSVFile_print(String output)
	{
	try {
		csv_writer.write(number.toString());
		csv_writer.write(version);
		csv_writer.write(output);
		csv_writer.endRecord();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	number++;
	}

	public void close()throws IOException
	{
//		input.close();
		writer.close();
		csv_writer.close();
	}

}
