package RW_Problem;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
	/**
	 * using singleton pattern, return only one FileControl instance; 
	 */
	private Monitor(){
		
	}
	private static Monitor fcInstance = new Monitor();
	public static Monitor getInstance(){
		return fcInstance;
	}
	
	Lock lock = new ReentrantLock();
	private Condition readers = lock.newCondition();
	private Condition writers = lock.newCondition();
	private int isReading_Count = 0;
	private int waitingWriters_Count = 0;
	private boolean isWriting = false;
	
	public void ReaderEntry(int ReaderID) throws Exception{
		lock.lock();
		if(isWriting || waitingWriters_Count > 0){
			System.out.println("Reader thread "+ReaderID+": waiting to read");
			readers.await();
		}
		System.out.println("Reader thread "+ReaderID+": ready to read");
		isReading_Count++;
		lock.unlock();
	}
	
	public void ReaderExit(int ReaderID){
		lock.lock();
		System.out.println("Reader thread "+ReaderID+": finished reading");
		isReading_Count--;
		if(isReading_Count == 0){
			if(waitingWriters_Count > 0){
				writers.signal();
			}else{
				readers.signal();
			}
		}
		lock.unlock();
	}
	
	public void WriterEntry(int WriterID) throws Exception{
		lock.lock();
		if(isWriting || isReading_Count > 0){
			System.out.println("Writer thread "+WriterID+": waiting to write");
			waitingWriters_Count++;
			writers.await();
			waitingWriters_Count--;
		}
		System.out.println("Writer thread "+WriterID+": ready to write");
		isWriting = true;
		lock.unlock();
	}
	
	public void WriterExit(int WriterID){
		lock.lock();
		System.out.println("Writer thread "+WriterID+": finished writing");
		if(waitingWriters_Count > 0){
			writers.signal();
		}else{
			isWriting = false;
			readers.signal();
		}
		lock.unlock();
	}
}
