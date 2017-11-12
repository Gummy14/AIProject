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
	public static void NaturalSelection(List<ArtGallery> thePopulation) throws IOException
	{
		int[] parentOne = new int[thePopulation.get(0).getCameras()];
		int[] parentTwo = new int[thePopulation.get(0).getCameras()];
		
		int smallest = thePopulation.get(0).getCameras()+1;//Population member with the smallest number of cameras, initially set to an impossibly large value
		int smallestFoundAt = 0;
		int secondSmallest = thePopulation.get(0).getCameras()+1;//Population member with the second smallest number of cameras, initially set to an impossibly large value
		int secondSmallestFoundAt = 0;
		
		//find smallest count, this will be made into parent 1
		for(int i = 0;i<thePopulation.size();i++)
		{
			if(thePopulation.get(i).getCount()< smallest)
			{
				smallest = thePopulation.get(i).getCount();
				smallestFoundAt = i;
			}
		}
		//Make Parent 1
		for(int j = 0; j<26; j++)
		{
			parentOne[j] = thePopulation.get(smallestFoundAt).getCameraPlacement(j);
		}
		
		//find second smallest count, this will be made into parent 2
		for(int i = 0;i<thePopulation.size();i++)
		{
			if(thePopulation.get(i).getCount()< secondSmallest && i != smallestFoundAt)
			{
				secondSmallest = thePopulation.get(i).getCount();
				secondSmallestFoundAt = i;
			}
		}
		//Make Parent 2
		for(int j = 0; j<26; j++)
		{
			parentTwo[j] = thePopulation.get(secondSmallestFoundAt).getCameraPlacement(j);
		}
		
		//CROSSOVER FUNCTION HERE
	}
	public static void main(String[] args) throws IOException
	{
		List<ArtGallery> thePopulation = makeInitialPopulation();
		List<JFrame> windows = new ArrayList<JFrame>();
		NaturalSelection(thePopulation);
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
