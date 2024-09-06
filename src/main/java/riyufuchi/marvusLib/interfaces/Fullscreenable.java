package riyufuchi.marvusLib.interfaces;

import javax.swing.JFrame;

public interface Fullscreenable<E extends JFrame>
{
	void toggleFullscreen(E oldFrame);
}
