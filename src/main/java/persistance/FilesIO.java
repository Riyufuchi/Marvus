package persistance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import gui.ErrorWindow;
import workData.Money;

/**
 * Copyright Header
 * 
 * Project: ODB Manager
 * Created On: 02.07.2021
 * Last Edit: 02.07.2021
 * @author Riyufuchi
 * @version 1.0
 * @since 1.3.1 
 */
public class FilesIO 
{
	public static void saveToCVS(String path, List<Money> data)
	{
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) 
		{
			for (Money m : data) 
			{
				//String[] values = {String.valueOf(m.getMoneySum()), m.getDate()};
				bw.append(String.join(";", m.getDataArray()) + "\n");
			}
			bw.flush();
		}
		catch(IOException e)
		{
			new ErrorWindow("IO Error", e.getMessage());
		}
		new ErrorWindow("Export completed", "File saved to path: " + path);
	}
	 
	public static LinkedList<Money> loadFromCVS(String path)
	{
		LinkedList<Money> l = new LinkedList<Money>();
		int i = 1;
		String s;
		try (BufferedReader br = new BufferedReader(new FileReader(path))) 
		{
			while ((s = br.readLine()) != null) 
			{
				String[] split = s.split(";");
				l.add(new Money(i, split[0], split[1]));
				i++;
			}
		}
		catch(IOException e)
		{
			new ErrorWindow("IO Error", e.getMessage());
		}
		return l;
	}
	
	public static void writeBinary(String path, List<Money> data)
	{
		try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(path))) 
		{
			for (Money m: data) 
			{
				dos.writeUTF(m.getMoneySum().toPlainString());
				dos.writeUTF(m.getDate());
				dos.flush();
			}
		}
		catch (IOException e) 
		{
			new ErrorWindow("IO error", e.getMessage());
		}
		new ErrorWindow("Export completed", "File saved to path: " + path);
	}
	
	public static LinkedList<Money> loadBinary(String path)
	{
		LinkedList<Money> l = new LinkedList<>();
		int i = 0;
		String money = "";
		String date = "";
		try (DataInputStream dis = new DataInputStream(new FileInputStream(path))) 
		{
			while (dis.available() > 0) 
			{
				money = dis.readUTF();
				date = dis.readUTF();
				l.add(new Money(i, money, date));
				i++;
			}
		}
		catch (IOException e) 
		{
			new ErrorWindow("IO error", e.getMessage());
		}
		return l;
	}
}