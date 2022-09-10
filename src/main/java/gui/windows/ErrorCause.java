package gui.windows;

public enum ErrorCause
{
	IO("IO"),
	INERNAL("Internal"),
	INPUT("Input");
	
	public final String text;
	
	ErrorCause(String text)
	{
		this.text = text + " Error";
	}
}
