import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GA {
	
	int populationSize = 10; //Number of members in the population
	
	public void makeInitialPopulation() throws IOException
	{
		List<ArtGallery> population = new ArrayList<ArtGallery>(); //Creates the ArrayList that holds the entire population
		for(int i = 0; i<=populationSize;i++)
		{
			population.add(new ArtGallery()); //Adds an ArtGallery Object to the ArrayList
		}
		//finds a secure solution for each population member
		for (int i=0; i<populationSize; i++){
		    population.get(i).findSecureSolution( population.get(i).getCameras());
		}
		
	}
}
