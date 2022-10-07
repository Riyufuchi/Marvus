package general.persistance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created On: 20.04.2022
 * Last Edit: 07.10.2022
 * 
 * @author Riyufuchi
 * @version 1.2
 * @since 1.0 
 */
public final class Persistance
{
	/**
	 * Saves data to csv file (data must be preformated into lines)
	 * 
	 * @param path with file name without file extension
	 * @param data cvs lines ready to be dumped into file
	 * @throws IOException
	 * @throws NullPointerException
	 */
	public static void saveToCSV(String path, String[] data) throws IOException, NullPointerException
	{
		Objects.requireNonNull(data);
		BufferedWriter bw = new BufferedWriter(new FileWriter(Objects.requireNonNull(path) + ".csv"));
		for(String line : data)
			bw.append(line + "\n");
		bw.close();
	}
	
	/**
	 * Saves data to csv file
	 * 
	 * @param path with file name without file extension
	 * @param data [x][y]: x columns; y - data to be formated to csv and saved as line
	 * @throws IOException
	 * @throws NullPointerException
	 */
	public static void saveToCSV(String path, String[][] data) throws IOException, NullPointerException
	{
		Objects.requireNonNull(data);
		BufferedWriter bw = new BufferedWriter(new FileWriter(Objects.requireNonNull(path)));
		for(String[] line : data)
			bw.append(makeLine(line) + "\n");
		bw.close();
	}
	
	private static String makeLine(String[] data)
	{
		String line = "";
		final int lenght = data.length - 1;
		for(int i = 0; i < lenght; i++)
			line += data[i] + ";";
		line += data[lenght];
		return line;
	}
	
	/**
	 * Load data from csv file
	 * 
	 * @param path path with file name and .csv as file format
	 * @return list of loaded lines
	 * @throws IOException
	 */
	public static List<String> loadFromCSV(String path) throws IOException, NullPointerException
	{
		Objects.requireNonNull(path);
		List<String> l = new LinkedList<>();
		String s;
		BufferedReader br = new BufferedReader(new FileReader(path));
		while ((s = br.readLine()) != null) 
			l.add(s);
		br.close();
		return l;
	}
	
	/**
	 * Saves objects using serialization
	 * 
	 * @param <E>
	 * @param list
	 * @param path
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws NullPointerException
	 */
	public static <E extends Serializable> void serialize(List<E> list, String path) throws FileNotFoundException, IOException, NullPointerException
	{
		FileOutputStream file = new FileOutputStream(Objects.requireNonNull(path) + ".ser");
		ObjectOutputStream output = new ObjectOutputStream(file);
		output.writeInt(list.size());
		Iterator<E> it = list.iterator();
		while (it.hasNext())
			output.writeObject(it.next());
		output.close();
		file.close();
	}

	@SuppressWarnings("unchecked")
	public static <E> List<E> deserialize(String path) throws FileNotFoundException, IOException, ClassNotFoundException, NullPointerException
	{
		FileInputStream file = new FileInputStream(Objects.requireNonNull(path) + ".ser");
		ObjectInputStream input = new ObjectInputStream(file);
		List<E> list = new LinkedList<>();
		int pocet = input.readInt();
		for (int i = 0; i < pocet; i++)
			list.add((E) input.readObject());
		input.close();
		file.close();
		return list;
	}
}
