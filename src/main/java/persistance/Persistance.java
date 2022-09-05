package persistance;

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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created On: 20.04.2022
 * Last Edit: 06.09.2022
 * @author Riyufuchi
 * @version 1.1
 * @since 1.0 
 */
public final class Persistance
{
	/**
	 * 
	 * @param path path with file name and .csv as file format
	 * @param data data[x][y] - x columns, y - data to be formated to csv and saved as line
	 * @throws IOException
	 * @throws NullPointerException
	 */
	public static void saveToCSV(String path, String[][] data) throws IOException, NullPointerException
	{
		Objects.requireNonNull(path);
		Objects.requireNonNull(data);
		BufferedWriter bw = new BufferedWriter(new FileWriter(path));
		for (int i = 0; i < data.length; i++) 
			//for(int j = 0; j < data[0].length; j++)
				bw.append(String.join(";", data[0] + "\n"));
		bw.close();
	}
	 
	/**
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
	
	public static <E> void uloz(List<E> seznam, String nazev) throws FileNotFoundException, IOException
	{
		FileOutputStream soubor = new FileOutputStream(Objects.requireNonNull(nazev) + ".ser");
		ObjectOutputStream vystup = new ObjectOutputStream(soubor);
		vystup.writeInt(seznam.size());
		Iterator<?> it = seznam.iterator();
		while (it.hasNext())
			vystup.writeObject(it.next());
		vystup.close();
		soubor.close();
	}

	@SuppressWarnings("unchecked")
	public static <E> void nacti(List<E> list, String nazev) throws FileNotFoundException, IOException, ClassNotFoundException
	{
		FileInputStream soubor = new FileInputStream(Objects.requireNonNull(nazev) + ".ser");
		ObjectInputStream vstup = new ObjectInputStream(soubor);
		int pocet = vstup.readInt();
		for (int i = 0; i < pocet; i++)
			list.add((E) vstup.readObject());
		vstup.close();
		soubor.close();
	}
}
