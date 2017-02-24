package RW_Problem;

import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;

public class Reader_Writer_Thread implements Runnable{
	private File file;
	private int threadID;
	private int currentReadIndex = 0;
	private Monitor montor = Monitor.getInstance();
	
	public Reader_Writer_Thread(int n_threadID, File n_file) {
		this.file = n_file;
		this.threadID = n_threadID;
	}
	
	@Override
	public void run() {
		for(int i=0;i<4;i++){
			int random_number = (int) (Math.random()*7);
			if(random_number%2 == 0){
				try {
					montor.WriterEntry(threadID);
					WriteFile(file);
					montor.WriterExit(threadID);;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				try {
					montor.ReaderEntry(threadID);
					ReadFile(file);
					montor.ReaderExit(threadID);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}
	}
	
	public void ReadFile(File file){
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			String test = raf.readLine();
			if(test == null && currentReadIndex == 0){
				System.out.println("File is EMPTY !!");
			}else{
				raf.seek(currentReadIndex);
				String line = raf.readLine();
				if(line != null){
					System.out.println(line);
					currentReadIndex += line.length()+1;
				}
			}
			raf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void WriteFile(File file){
		try {
			FileWriter fw = new FileWriter(file,true);
			fw.write("Thread " + this.threadID + " is writing." + "\n");
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
}
