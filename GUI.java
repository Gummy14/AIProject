import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI extends JPanel implements ActionListener
{
	public static void main(String[] args) throws IOException
	{
		ArtGallery artGallery = new ArtGallery(26);
		artGallery.IsSecure();
		JFrame mainWindow = new JFrame();
		mainWindow.add(artGallery);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setTitle("Art Gallery");
		//mainWindow.setBackground(Color.WHITE);
		mainWindow.setSize(1000, 1000);
		mainWindow.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
