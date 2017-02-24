package RW_Problem;

import java.io.File;
import java.io.IOException;

public class Task_Main {
	
	public static void main(String[] args) {
		//define a common file as a parameter transfer to reader and writer thread;
		File file = new File("./_SharedFile.txt");
		if(file.exists()){
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Thread reader_writer[] = new Thread[7];
		
		for(int i=0;i<7;i++){
			reader_writer[i] = new Thread(new Reader_Writer_Thread(i,file));
		}
		
		for(int i=0;i<7;i++){
			reader_writer[i].start();
		}
	}
}


