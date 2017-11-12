import java.util.ArrayList;
import java.util.Random;

public class Algorithms {
	public static ArrayList<ArtGallery> GeneratePopulation(ArrayList<ArtGallery> populationPrototype, int sizePopulation)
	{
		int chromosomeLength = populationPrototype.size();
		ArrayList<ArtGallery> newPopulationMember = new ArrayList<ArtGallery>();

		for(int i = 0; i < sizePopulation; i++)
		{	
			newPopulationMember = new ArrayList<ArtGallery>();
			//Create new population member
			for(int count = 0; count < sizePopulation; count++)
				newPopulationMember.add(populationPrototype.get(count));
			
			Random rand = new Random();
			int hasCamera;
			for(int j = 0; j < chromosomeLength; j++)
			{
				hasCamera = rand.nextInt(1);
				//if(hasCamera == 1)
					//newPopulationMember.get(j).set
			//	else {}
			}//inner loop
		}//outer loop
		
		return newPopulationMember;
	}
	
	public static ArtGallery Mutate(ArtGallery populationMember, double MutationProbability)
	{
				
        //Input for mutation probability (Pm) will be allowed to be between 1 and .001 
        int maxMutationInt = 1;
        if (MutationProbability < 1 && MutationProbability >= 0.1)
            maxMutationInt = 10;
        else if (MutationProbability < 1 && MutationProbability >= 0.01)
            maxMutationInt = 100;
        else if (MutationProbability < 1 && MutationProbability >= 0.001)
            maxMutationInt = 1000;
        else { System.out.println("Mutation Probability is corrupted."); }

        Random rnd = new Random();
        	for(int count = 0; count < populationMember.BuildingVertices.size(); count++) {
            
	        	if (rnd.nextInt(maxMutationInt)+1 <= 1)
	          {
	                //Generates a random gene index to swap with for the mutation
	        		if(populationMember.getVertices().get(count).getCamera() == true)
	        			populationMember.getVertices().get(count).setCamera(false);
	        		else {populationMember.getVertices().get(count).setCamera(true);}  
	          }
        	}
		return populationMember;	
	}
	

}
