package riyufuchi.marvus.marvusLib.legacy.utils;

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

import riyufuchi.marvus.marvusData.MoneySum;
import riyufuchi.sufuLib.gui.DialogHelper;

/**
 * Created On: 02.07.2021
 * Last Edit: 17.04.2023
 * 
 * @author Riyufuchi
 * @version 1.4
 * @since 1.3.1 
 */
public class FilesIO 
{
	public static void saveToCSV(String path, List<MoneySum> data)
	{
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) 
		{
			for (MoneySum m : data)
			{
				//String[] values = {String.valueOf(m.getMoneySum()), m.getDate()};
				bw.append(String.join(";", m.getDataArray()) + "\n");
			}
			bw.flush();
		}
		catch(IOException e)
		{
			DialogHelper.errorDialog(null, e.getMessage(), e.getClass().getSimpleName());
		}
		DialogHelper.informationDialog(null, "File saved to: " + path, "Export to .CSV completed");
	}
	
	public static LinkedList<MoneySum> loadFromCSV(String path)
	{
		LinkedList<MoneySum> l = new LinkedList<MoneySum>();
		String s;
		String[] split = new String[2];
		try (BufferedReader br = new BufferedReader(new FileReader(path)))
		{
			while ((s = br.readLine()) != null)
			{
				split = s.split(";");
				l.add(new MoneySum(split[0], split[1]));
			}
		}
		catch(IOException | IndexOutOfBoundsException e)
		{
			DialogHelper.errorDialog(null, e.getMessage(), e.getClass().getSimpleName());
		}
		return l;
	}
	
	public static void writeBinary(String path, List<MoneySum> data)
	{
		try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(path)))
		{
			for (MoneySum m: data)
			{
				dos.writeUTF(m.getMoneySum().toPlainString());
				dos.writeUTF(m.getDate());
				dos.flush();
			}
		}
		catch (IOException e)
		{
			DialogHelper.errorDialog(null, e.getMessage(), e.getClass().getSimpleName());
		}
		DialogHelper.informationDialog(null, "File saved to: " + path, "Export to binary completed");
	}
	
	public static LinkedList<MoneySum> loadBinary(String path)
	{
		LinkedList<MoneySum> l = new LinkedList<>();
		String money = "";
		String date = "";
		try (DataInputStream dis = new DataInputStream(new FileInputStream(path))) 
		{
			while (dis.available() > 0)
			{
				money = dis.readUTF();
				date = dis.readUTF();
				l.add(new MoneySum(money, date));
			}
		}
		catch (IOException e)
		{
			DialogHelper.errorDialog(null, e.getMessage(), e.getClass().getSimpleName());
		}
		return l;
	}
}