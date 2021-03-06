import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Polygon;
import java.util.Random;
import java.util.Arrays;

public class ArtGallery extends JPanel implements ActionListener{

//******************
//Class Properties
//******************
		//Chosen camera 
		private int testCamera   = 4;
		private double distance  = 0;
		//Size variable determines the number of vertices in the building
		private Random rand      = new Random();
		//private int size       =  rand.nextInt(50) + 1;
		private int size;
		private int[] cameras;
		private int[] polyX      = new int[361];
		private int[] polyY      = new int[361];
		//This list contains all the museum building vertices
		private ArrayList<MuseumVertex> BuildingVertices = new ArrayList<MuseumVertex>();
		//This arrayList will contain all the polygons that are formed by the camera's viewpoints
		private ArrayList<Polygon> PolygonList = new ArrayList<Polygon>();
		private Polygon MuseumOutline;
		private int[] setX = new int[size];
		private int[] setY = new int[size];
		private int[] cameraPlacementTrackingArray;
		private boolean Sec = true;
		
		private int count = 0;

//******************
//Class Constructors
//******************
	public ArtGallery(int x) throws IOException
	{
		//Reads file to generate the vertices
		size = 21;
		setX = new int[size];
		setY = new int[size];
		cameraPlacementTrackingArray = new int[size];
		this.readFile();

		cameras = new int[size];
		for(int i = 0; i < cameras.length; i++)
			cameras[i] = i+1;
		
		int ScalingConstant = 5;
		for(int i = 0; i< BuildingVertices.size();i++)
		{
			//Scale the building to meet the screen needs
			BuildingVertices.get(i).setX(BuildingVertices.get(i).getX()*ScalingConstant);	
			BuildingVertices.get(i).setY(BuildingVertices.get(i).getY()*ScalingConstant);	
			setY[i] = setY[i]*ScalingConstant;
			setX[i] = setX[i]*ScalingConstant;
			//g.fillOval(((int)setX[i]-5)),((int)setY[i]-5), 10, 10);
		}
		MuseumOutline = new Polygon(setX, setY, size);
		findSecureSolution(cameras);
		//createPolygons( MuseumOutline, cameras, setX, setY);
	}
	public ArtGallery(int[] x) throws IOException
	{
		//Reads file to generate the vertices
		size = 21;
		setX = new int[size];
		setY = new int[size];
		
		this.readFile();

		cameras = new int[size];
		for(int i = 0; i < cameras.length; i++)
			cameras[i] = i+1;
		
		int ScalingConstant = 5;
		for(int i = 0; i< BuildingVertices.size();i++)
		{
			//Scale the building to meet the screen needs
			BuildingVertices.get(i).setX(BuildingVertices.get(i).getX()*ScalingConstant);	
			BuildingVertices.get(i).setY(BuildingVertices.get(i).getY()*ScalingConstant);	
			setY[i] = setY[i]*ScalingConstant;
			setX[i] = setX[i]*ScalingConstant;
			//g.fillOval(((int)setX[i]-5)),((int)setY[i]-5), 10, 10);
		}
		MuseumOutline = new Polygon(setX, setY, size);
		cameraPlacementTrackingArray = x.clone();
		for(int i = 0; i<size;i++)
		{
			if(cameraPlacementTrackingArray[i] == 1)
			{
				createPolygon(MuseumOutline, cameraPlacementTrackingArray[i], setX, setY);
			}
		}
		if(isSecure())
		{
			Sec = true;
		}
		else
		{
			Sec = false;
		}
		//createPolygons( MuseumOutline, cameras, setX, setY);
	}
	
	public ArtGallery() throws IOException
	{
		//Reads file to generate the vertices
		//Random rand = new 
		size = 21;
		setX = new int[size];
		setY = new int[size];
		cameraPlacementTrackingArray = new int[size];
		this.readFile();

		cameras = new int[size];
		for(int i = 0; i < cameras.length; i++)
			cameras[i] = i+1;
		
		int ScalingConstant = 5;
		for(int i = 0; i < BuildingVertices.size(); i++)
		{
			//Scale the building to meet the screen needs
			BuildingVertices.get(i).setX(BuildingVertices.get(i).getX()*ScalingConstant);	
			BuildingVertices.get(i).setY(BuildingVertices.get(i).getY()*ScalingConstant);	
			setY[i] = setY[i]*ScalingConstant;
			setX[i] = setX[i]*ScalingConstant;
			//g.fillOval(((int)setX[i]-5)),((int)setY[i]-5), 10, 10);
		}
		MuseumOutline = new Polygon(setX, setY, size);
		findSecureSolution(cameras);
		//createPolygons( MuseumOutline, cameras, setX, setY);
	}
	
//******************
//Setters And Getters
//******************	
public boolean getSec()
{
	return Sec;
}
public Polygon getMuseumOutline()
{
	return MuseumOutline;
}
public int getCameras()
{
	return cameras.length;
}
public int getCameraPlacement(int element)
{
	return cameraPlacementTrackingArray[element];
}
public int[] getCameraPlacement()
{
	return cameraPlacementTrackingArray;
}
public void setCameraPlacement(int[] value)
{
	for(int i = 0;i<size;i++)
	{
		cameraPlacementTrackingArray[i] = value[i];
	}
}
public int getCount()
{
	return count;
}
public void setCount(int newCount)
{
	count = newCount;
}
public int[] getSetX()
{
	return setX;
}
public int[] getSetY()
{
	return setY;
}
public ArrayList<MuseumVertex> getBuildingVertices()
{
	return BuildingVertices;
}
//******************
//Class Methods
//******************
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
			setX[lineCount] = Integer.parseInt(x);
			setY[lineCount] = Integer.parseInt(y);
			lineCount++;
		}
		BReader.close();
		opnfile.close();	
	}		// end readFile

	public void GenerateMuseumLayout(int numberVertices)
	{
		Random rand = new Random();
		int randX, randY;
		for (int i = 0; i < numberVertices; i++)
		{
			randX =  rand.nextInt(100) + 1;
			randY =  rand.nextInt(100) + 1;

			BuildingVertices.add(new MuseumVertex(randX, randY));
			setX[i] = randX;
			setY[i] = randY;
		} 
	}
//***********************************************
//Draws the museum polygon and all the inner polygons representing each camera's vision
//***********************************************
	public void paintComponent(Graphics g) 
	{	
		//count  = 0;
		super.paintComponent(g);
		for(int i = 0; i< BuildingVertices.size();i++)
		{
			//Draws the building vertices
			g.setColor(Color.BLACK);
			g.fillOval(((int)BuildingVertices.get(i).getX())-5,((int)BuildingVertices.get(i).getY()-5), 10, 10);
		}
		g.setColor(Color.BLACK);
		g.drawPolygon(MuseumOutline);
		g.setColor(Color.MAGENTA);
		for(int i = 0; i < PolygonList.size(); i++)
			g.drawPolygon(PolygonList.get(i));
		
	}
	public void createPolygons(Polygon polygon, int[] cameras, int[] setX, int[] SetY) {
		
		for(int currentCamera = 0; currentCamera < cameras.length; currentCamera++)
		{
			createPolygon(polygon, currentCamera, setX, setY);
		}
	}
	public void createPolygon(Polygon polygon, int currentCamera, int[] setX, int[] SetY) {
		int ang = 0;
		
		while(ang <=360)
		{
			double length=5;
			double 	endX,endY = 0;
			
			endX = setX[currentCamera] + length*Math.cos(Math.toRadians(ang)); // endx, endy is final point on line
			endY = setY[currentCamera] + length*Math.sin(Math.toRadians(ang)); // while setx and y are the vertex it starts
			while(polygon.contains(endX,endY) == true) // while the end point is still inside the polygon
			{
				length=length+5;
				endX = setX[currentCamera] + length*Math.cos(Math.toRadians(ang)); // add distance to the line
				endY = setY[currentCamera] + length*Math.sin(Math.toRadians(ang));
			}
			polyX[ang] =  (int)endX;
			polyY[ang] =  (int)endY;
			ang++;
		}	
			Polygon cameraVision;
			cameraVision = new Polygon(polyX, polyY, 361);
			PolygonList.add(cameraVision);
	}

	/*public static void main(String[] args) throws IOException
	{
		ArtGallery artGallery = new ArtGallery(6);
		artGallery.isSecure();
		JFrame mainWindow = new JFrame();
		mainWindow.add(artGallery);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setTitle("Art Gallery");
		//mainWindow.setBackground(Color.WHITE);
		mainWindow.setSize(1000, 1000);
		mainWindow.setVisible(true);

	}*/
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub	
	}
	
//***********************************************
//This function will parse each polygon generated by each camera's field of vision
//	- Function returns "true" if all cameras are contained within the field of vision of at least one polygon
//	- Other wise returns "false"
//***********************************************
	public boolean isSecure()
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

	public void findSecureSolution(int[] cameras) {
		List<Integer> cams = new ArrayList<Integer>();//creates an array list for the cameras
		for (int i = 0; i <cameras.length; i++) cams.add(cameras[i]);//adds the specified number cameras to the arraylist
		count = 0;//number of added cameras starts at zero
		while (true) {
			Random rand = new Random();
			if (cams.size() == 0) break;//if we have no cameras, just break
			int index = rand.nextInt(cams.size());//picks a random spot for a camera
			//i'm sorry for this dirty fix
			while(cameraPlacementTrackingArray[index] == 1)//if that spot is already taken, randomly pick another until we find one that isn't taken
			{
				index = rand.nextInt(cams.size());//picks a random spot 
			}
			createPolygon(MuseumOutline, cams.get(index)-1, setX, setY);//draws the polygon representing that cameras line of sight
			//cams.remove(index); THIS LINE CAUSED MAJOR PROBLEMS WHEN TRACKING WHICH CAMERAS WHERE PLACED
			boolean secure = this.isSecure();//checks to see if the entire museum is covered
			count++;//adds one to the camera count
			cameraPlacementTrackingArray[index] = 1;
			if (secure) break;//if the museum is covered, break, otherwise loop again
		}
		//System.out.println(count);
	}
	
	/*class MuseumVertex {
		

	}*/
	
//Never Used functions and ETC********************************************
	
	public double getDistance(double x1, double y1, double x2, double y2)
	{
		distance = Math.sqrt(((x2 - x1)*(x2 - x1))+((y2 - y1)*(y2 - y1)));
		return distance;
	}

	public void getMid(int x1, int y1, int x2, int y2)
	{
		//midx = (x1+x2)/2;
		//midy = (y1+y2)/2;
	}
}
