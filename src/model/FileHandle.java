package model;


public class FileHandle {
	private static String prefix = "";
	private static String carFileName = "";
	private static String vanFileName = "";
	public FileHandle() {
		
	}
	public static void writeCarToFile(Car car) {
		
		car.toString();
		String temp = "";
		writeToFile(prefix+"/"+carFileName, temp);
	}
public static void writeVanToFile(Van van) {
		
		van.toString();
		String temp = "";
		writeToFile(prefix+"/"+vanFileName, temp);
	}
	
	private static void writeToFile(String filename, String content) {
		return;
	}
}
