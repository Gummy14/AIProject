import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GA extends JPanel implements ActionListener{
	
	static int populationSize = 10; //Number of members in the population
	
	public static List<ArtGallery> makeInitialPopulation() throws IOException
	{
		List<ArtGallery> population = new ArrayList<ArtGallery>(); //Creates the ArrayList that holds the entire population
		for(int i = 0; i<populationSize;i++)
		{
			population.add(new ArtGallery()); //Adds an ArtGallery Object to the ArrayList
		}
		return population;
		
	}
	public void NaturalSelection() throws IOException
	{
		List<ArtGallery> thePopulation = makeInitialPopulation();
		int[] parentOne = new int[thePopulation.get(0).getCameras()];
		int[] parentTwo = new int[thePopulation.get(0).getCameras()];
	}
	public static void main(String[] args) throws IOException
	{
		List<ArtGallery> thePopulation = makeInitialPopulation();
		List<JFrame> windows = new ArrayList<JFrame>();

		for(int i = 0; i<populationSize; i++)
		{
			System.out.print("Population Member #"+i+":");
			for(int j = 0; j<26; j++)
			{
				System.out.print(thePopulation.get(i).getCameraPlacement(j));
			}
			System.out.print("\n");
			windows.add(new JFrame());
			windows.get(i).add(thePopulation.get(i));
			windows.get(i).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			windows.get(i).setTitle("Art Gallery");
			windows.get(i).setSize(1000, 1000);
			windows.get(i).setVisible(true);
		}

	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
