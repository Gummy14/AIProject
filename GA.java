import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class GA extends JPanel implements ActionListener{
	
	static int populationSize = 10; //Number of members in the population
	private static int size = 21;
	private static ArrayList<MuseumVertex> BuildingVertices = new ArrayList<MuseumVertex>();
	private static ArrayList<Polygon> PolygonList = new ArrayList<Polygon>();
	static List<ArtGallery> thePopulation = new ArrayList<ArtGallery>();
	private static int[] setofX = new int[size];
	private static int[] setofY = new int[size];
	private static int[] sol = new int[size];
	private static int[] thepolyX      = new int[361];
	private static int[] thepolyY      = new int[361];
	Polygon MuseumOutline;
	
	public Polygon getMuseumOutline()
	{
		return MuseumOutline;
	}
	public void readFile() throws IOException
	{
		FileReader opnfile = new FileReader("Gallery.txt");
		BufferedReader BReader = new BufferedReader(opnfile);
		String crd;
		int lineCount = 0;
		while((crd = BReader.readLine()) != null)
		{
			String x = crd.substring(0,3);
			String y = crd.substring(6);
			this.BuildingVertices.add(new MuseumVertex((Integer.parseInt(x)), (Integer.parseInt(y))));
			setofX[lineCount] = Integer.parseInt(x);
			setofY[lineCount] = Integer.parseInt(y);
			lineCount++;
		}
		BReader.close();
		opnfile.close();	
		MuseumOutline = new Polygon(setofX, setofY, size);
	}
	public static void makeInitialPopulation() throws IOException
	{
		//List<ArtGallery> population = new ArrayList<ArtGallery>(); //Creates the ArrayList that holds the entire population
		for(int i = 0; i<populationSize;i++)
		{
			thePopulation.add(new ArtGallery()); //Adds an ArtGallery Object to the ArrayList
		}
		
	}
	public static void NaturalSelection() throws IOException
	{
		Random rand = new Random();
		int[] parentOne = new int[thePopulation.get(0).getCameras()];
		int[] parentTwo = new int[thePopulation.get(0).getCameras()];
		int[] childOne = new int[thePopulation.get(0).getCameras()];
		int[] childTwo = new int[thePopulation.get(0).getCameras()];
		boolean isChildOneValid = false, isChildTwoValid = false;
		
		int childOneCount = 0;
		int childTwoCount = 0;
		
		int smallest = thePopulation.get(0).getCameras()+1;//Population member with the smallest number of cameras, initially set to an impossibly large value
		int smallestFoundAt = 0;
		int secondSmallest = thePopulation.get(0).getCameras()+1;//Population member with the second smallest number of cameras, initially set to an impossibly large value
		int secondSmallestFoundAt = 0;
		
		Polygon checker = null;//polygon to use for checking child solutions
		
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
		for(int j = 0; j<size; j++)
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
		for(int j = 0; j<size; j++)
		{
			parentTwo[j] = thePopulation.get(secondSmallestFoundAt).getCameraPlacement(j);
		}
		
		//CROSSOVER FUNCTION HERE
		
		int randomFirstValue = rand.nextInt(thePopulation.get(0).getCameras()/2);
		int randomSecondValue = rand.nextInt((thePopulation.get(0).getCameras()/2)+thePopulation.get(0).getCameras()/2);
		
		//generates child one
		for(int i = 0;i<randomFirstValue;i++)
		{
			childOne[i]=parentTwo[i];
		}
		for(int i = randomFirstValue;i<randomSecondValue;i++)
		{
			childOne[i]=parentOne[i];
		}
		for(int i = randomSecondValue;i<thePopulation.get(0).getCameras();i++)
		{
			childOne[i]=parentTwo[i];
		}

		//System.out.print("\n");		
		//1/10 chance of mutation
		int mutationProbablility = rand.nextInt(10);
		if(mutationProbablility == 1)
		{
		
			int mutationLocation = rand.nextInt(size);
			while(childOne[mutationLocation] != 1)
			{
				mutationLocation = rand.nextInt(size);
			}
			childOne[mutationLocation] = 0;
		}
		//creates polygons for all of child ones camera positions and checks if that solution is secure

		for(int i = 0;i<thePopulation.get(0).getCameras();i++)
		{
			if(childOne[i] == 1)//if a camera is placed at this spot
			{
				makeSightLines(i);
				childOneCount++;
			}
		}
		
			int consecutiveZeroCount1 = 0;
			boolean threeOrMoreZeros = false;
			for(int i = 0;i<thePopulation.get(0).getCameras();i++)
			{
				if(childOne[i] == 0)
				{
					consecutiveZeroCount1++;
				}
				else if(childOne[i] == 1)
				{
					consecutiveZeroCount1 = 0;
				}
				if(consecutiveZeroCount1 == 3)
				{
					threeOrMoreZeros = true;
					break;
				}
			}
			if(threeOrMoreZeros = false)
			{
				isChildOneValid = true;
				/*System.out.print("childOne");
				for(int i = 0;i<thePopulation.get(0).getCameras();i++)
				{
					System.out.print(childOne[i]);
				}
				System.out.print(" Is Secure\n");*/
			}
			else
			{
				if(isSecure())
				{
					isChildOneValid = true;
					/*System.out.print("childOne");
					for(int i = 0;i<thePopulation.get(0).getCameras();i++)
					{
						System.out.print(childOne[i]);
					}
					System.out.print(" Is Secure\n");*/
				}
				else
				{
					//System.out.print("ChildOne Is Not Secure\n");
				}
			}
			
		PolygonList.clear();
		
		//generates child two
		for(int i = 0;i<randomFirstValue;i++)
		{
			childTwo[i]=parentOne[i];
		}
		for(int i = randomFirstValue;i<randomSecondValue;i++)
		{
			childTwo[i]=parentTwo[i];
		}
		for(int i = randomSecondValue;i<thePopulation.get(0).getCameras();i++)
		{
			childTwo[i]=parentOne[i];
		}
/////////////

/////////////
		
		//1/10 chance of mutation
		int mutationProbablility2 = rand.nextInt(10);
		if(mutationProbablility2 == 1)
		{
			int mutationLocation2 = rand.nextInt(size);
			while(childTwo[mutationLocation2] != 1)
			{
				mutationLocation2 = rand.nextInt(size);
			}
			childTwo[mutationLocation2] = 0;
		}
		//creates polygons for all of child twos camera positions and checks if that solution is secure
		for(int i = 0;i<thePopulation.get(0).getCameras();i++)
		{
			if(childTwo[i] == 1)//if a camera is placed at this spot
			{
				makeSightLines(i);
				childTwoCount++;
			}
		}
		
		int consecutiveZeroCount = 0;
		boolean threeOrMoreZeros2 = false;
		for(int i = 0;i<thePopulation.get(0).getCameras();i++)
		{
			if(childTwo[i] == 0)
			{
				consecutiveZeroCount++;
			}
			else if(childTwo[i] == 1)
			{
				consecutiveZeroCount = 0;
			}
			if(consecutiveZeroCount == 3)
			{
				threeOrMoreZeros2 = true;
				break;
			}
		}
		if(threeOrMoreZeros2 = false)
		{
			isChildTwoValid = true;
			/*System.out.print("childTwo");
			for(int i = 0;i<thePopulation.get(0).getCameras();i++)
			{
				System.out.print(childTwo[i]);
			}
			System.out.print(" Is Secure\n");*/
		}
		else
		{
			if(isSecure())
			{
				/*isChildTwoValid = true;
				System.out.print("childTwo");
				for(int i = 0;i<thePopulation.get(0).getCameras();i++)
				{
					System.out.print(childTwo[i]);
				}
				System.out.print(" Is Secure\n");*/
			}
			else
			{
				//System.out.print("ChildTwo Is Not Secure\n");
			}
		}
		PolygonList.clear();
		
		//Now that we have two children that may or may not be valid solutions we have to replace the worst members of the populations with them
		int largest = 0;//number of cameras for the Population member with the largest amount of cameras
		int largestFoundAt = 0;
		int secondLargest = 0;//number of cameras for the Population member with the 2nd largest amount of cameras
		int secondLargestFoundAt = 0;
		
		//finds largest
		for(int i = 0;i<thePopulation.size();i++)
		{
			if(thePopulation.get(i).getCount()> largest)
			{
				largest = thePopulation.get(i).getCount();
				largestFoundAt = i;
			}
		}
		//finds 2nd largest
		for(int i = 0;i<thePopulation.size();i++)
		{
			if(thePopulation.get(i).getCount()> secondLargest && i != largest)
			{
				secondLargest = thePopulation.get(i).getCount();
				secondLargestFoundAt = i;
			}
		}
		//child one replaces the largest
		if(isChildOneValid = true && childOneCount<largest)
		{
			thePopulation.remove(largestFoundAt);
			thePopulation.add(new ArtGallery(childOne));
		}
		//child two replaces the second largest
		if(isChildTwoValid = true && childTwoCount<secondLargest)
		{
			thePopulation.remove(secondLargestFoundAt);
			thePopulation.add(new ArtGallery(childTwo));
		}
/////////////
		/*for(int i = 0; i<populationSize; i++)
		{
			System.out.print("Population Member #"+i+":");
			for(int j = 0; j<size; j++)
			{
				System.out.print(thePopulation.get(i).getCameraPlacement(j));
			}
			System.out.print("\n");
		}
		System.out.println("\n");*/
	}
/////////////
	public static void makeSightLines(int currentCam)
	{
		int ang = 0;
		while(ang <=360)
		{
			double length=5;
			double 	endX,endY = 0;
			
			endX = setofX[currentCam] + length*Math.cos(Math.toRadians(ang)); // endx, endy is final point on line
			endY = setofY[currentCam] + length*Math.sin(Math.toRadians(ang)); // while setx and y are the vertex it starts
			while(thePopulation.get(0).getMuseumOutline().contains(endX,endY) == true) // while the end point is still inside the polygon
			{
				length=length+5;
				endX = setofX[currentCam] + length*Math.cos(Math.toRadians(ang)); // add distance to the line
				endY = setofY[currentCam] + length*Math.sin(Math.toRadians(ang));
			}
			thepolyX[ang] =  (int)endX;
			thepolyY[ang] =  (int)endY;
			ang++;
		}	
			Polygon cameraVision;
			cameraVision = new Polygon(thepolyX, thepolyY, 361);
			PolygonList.add(cameraVision);
	}
	public static boolean isSecure()
	{
		//This boolean keeps track of weather or not all of the museum corners have been spotted or not
		boolean cameraVisible = false;
		boolean allCamerasVisible = true;

			for(int i = 0; i < BuildingVertices.size(); i++)
			{
				cameraVisible = false;
				for(int j = 0; j < PolygonList.size(); j++)
				{
					//If polygon contains camera then set visibility to true
					if(PolygonList.get(j).contains(BuildingVertices.get(i).getX(), BuildingVertices.get(i).getY()))
					{
						BuildingVertices.get(i).setVisibility(true);
						cameraVisible = true;
						break;
					}		
				}//inner
				if(!cameraVisible) {
					allCamerasVisible = false;
					break;
				}
			}//outer loop
		return allCamerasVisible;
	}//end function
	public GA(int gensize) throws IOException
	{
		readFile();
		int ScalingConstant = 5;
		for(int i = 0; i < BuildingVertices.size(); i++)
		{
			//Scale the building to meet the screen needs
			BuildingVertices.get(i).setX(BuildingVertices.get(i).getX()*ScalingConstant);	
			BuildingVertices.get(i).setY(BuildingVertices.get(i).getY()*ScalingConstant);	
			setofY[i] = setofY[i]*ScalingConstant;
			setofX[i] = setofX[i]*ScalingConstant;
			//g.fillOval(((int)setX[i]-5)),((int)setY[i]-5), 10, 10);
		}
		makeInitialPopulation();
		List<JFrame> windows = new ArrayList<JFrame>();
		
		for(int i = 0; i<gensize;i++)
		{
			NaturalSelection();
		}
		int camCount=0;
		int smallest = 1000;
		int smallestFoundAt = 0;
		for(int i = 0; i<populationSize; i++)
		{
			
			for(int j = 0; j<size;j++)
			{
				if(thePopulation.get(i).getCameraPlacement(j) == 1)
				{
					camCount++;
				}
			}
			//System.out.println(camCount);
		}
		for(int i = 0;i<thePopulation.size();i++)
		{
			if(thePopulation.get(i).getCount()< smallest)
			{
				smallest = thePopulation.get(i).getCount();
				smallestFoundAt = i;
			}
		}
		System.out.print("\nCrowd Member Solution: ");
		//System.out.print(camCount+"\n");
		for(int i = 0;i<size;i++)
		{
			sol[i] = thePopulation.get(smallestFoundAt).getCameraPlacement(i);
			System.out.print(thePopulation.get(smallestFoundAt).getCameraPlacement(i));
		}
		//System.out.print("\n");
		BuildingVertices.clear();
		thePopulation.clear();
	}
	public int getSolution(int element)
	{
		return sol[element];
	}
	/*public static void main(String[] args) throws IOException
	{
		
		/*for(int i = 0; i<populationSize; i++)
		{
			System.out.print("\n");
			windows.add(new JFrame());
			windows.get(i).add(thePopulation.get(i));
			windows.get(i).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			windows.get(i).setTitle("Art Gallery");
			windows.get(i).setSize(1000, 1000);
			windows.get(i).setVisible(true);
		}
	}*/
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}