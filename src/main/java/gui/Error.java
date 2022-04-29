package gui;

public enum Error {
	IO("IO"),
	INERNAL("Internal"),
	INPUT("Input");
	
	public final String text;
	
	Error(String text)
	{
		this.text = text + " Error";
	}
}
