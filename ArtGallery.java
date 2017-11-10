import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Polygon;
import java.util.Random;

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
		private int[] xpoly      = new int[361];
		private int[] ypoly      = new int[361];		
		//This list contains all the museum building vertices
		ArrayList<MuseumVertex> BuildingVertices = new ArrayList<MuseumVertex>();
		//This arrayList will contain all the polygons that are formed by the camera's viewpoints
		ArrayList<Polygon> PolygonList = new ArrayList<Polygon>();
		private Polygon MuseumOutline;
		private int[] setX = new int[size];
		private int[] setY = new int[size];

		private boolean intersect = false;

//******************
//Class Constructors
//******************
	public ArtGallery()
	{
		size = 26;
		cameras = new int[1];
		cameras[0] = 2;
		//cameras[1] = 5;
		//cameras[2] = 11;
		setX = new int[size];
		setY = new int[size];
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
			
				//System.out.println("Vertex #"+(linecount+1)+" ("+x+", "+y+")");
				this.BuildingVertices.add(new MuseumVertex((Integer.parseInt(x)), (Integer.parseInt(y))));
				setX[lineCount] = Integer.parseInt(x);
				setY[lineCount] = Integer.parseInt(y);
				//System.out.println("Vertex #" + (linecount) + " (" + setx[linecount] + ", " + sety[linecount] + ")");
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
//Draws the museum polygon
//***********************************************
	public void paintComponent(Graphics g) 
	{	

		int ScalingConstant = 2;
		//count  = 0;
		super.paintComponent(g);
		for(int i = 0; i< BuildingVertices.size();i++)
		{
			//Scale the building to meet the screen needs
			BuildingVertices.get(i).setX(BuildingVertices.get(i).getX()*ScalingConstant);	
			BuildingVertices.get(i).setY(BuildingVertices.get(i).getY()*ScalingConstant);	
			setY[i] = setY[i]*ScalingConstant;
			setX[i] = setX[i]*ScalingConstant;
			g.setColor(Color.BLACK);
			
			g.fillOval(((int)BuildingVertices.get(i).getX())-5,((int)BuildingVertices.get(i).getY()-5), 10, 10);
			//g.fillOval(((int)setX[i]-5)),((int)setY[i]-5), 10, 10);
			}
		MuseumOutline = new Polygon(setX, setY, size);
		g.drawPolygon(MuseumOutline);
		
		
		createPolygons(g, MuseumOutline, cameras, setX, setY);
		g.setColor(Color.GREEN);

		for(int i = 0; i < PolygonList.size(); i++)
			g.drawPolygon(PolygonList.get(i));
		
	}
	public void createPolygons(Graphics g, Polygon polygon, int[] cameras, int[] setX, int[] SetY) {
		
		for(int currentCamera = 0; currentCamera < cameras.length; currentCamera++)
		{
			int ang = 0;
			while(ang <=360)
			{
				double length=1;
				double 	endX,endY = 0;
				
				endX = setX[currentCamera] + length*Math.cos(Math.toRadians(ang)); // endx, endy is final point on line
				endY = setY[currentCamera] + length*Math.sin(Math.toRadians(ang)); // while setx and y are the vertex it starts
				while(polygon.contains(endX,endY) == true) // while the end point is still inside the polygon
				{
					length++;
					endX = setX[currentCamera] + length*Math.cos(Math.toRadians(ang)); // add distance to the line
					endY = setY[currentCamera] + length*Math.sin(Math.toRadians(ang));
				}
				xpoly[ang] = (int) endX;
				ypoly[ang] = (int) endY;
				g.drawLine(setX[currentCamera], setY[currentCamera], xpoly[ang], ypoly[ang]);
				//Math.toDegrees(ang);
				ang++;
			 }
				
				Polygon cameraVision;
				cameraVision = new Polygon(xpoly, ypoly, 361);
				PolygonList.add(cameraVision);
		}
	}
	public static void main(String[] args) throws IOException
	{
		ArtGallery artGallery = new ArtGallery();
		artGallery.readFile();
		JFrame mainWindow = new JFrame();
		mainWindow.add(artGallery);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setSize(1000, 1000);
		mainWindow.setTitle("Art Gallery");
		mainWindow.setBackground(Color.WHITE);
		mainWindow.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub	
	}
	
//***********************************************
//This function will parse each polygon generated by each camera's field of vision
//	- Function returns "true" if all cameras are contained within the field of vision of at least one polygon
//	- Other wise returns "false"
//***********************************************
	public boolean IsSecure()
	{
		//This boolean keeps track of weather or not all of the museum corners have been spotted or not
		boolean cameraVisible = false;
		boolean allCamerasVisible    = true;

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
	
	private class MuseumVertex{
		private boolean isVisible;
		int xValue;
		int yValue;
		public MuseumVertex(int x, int y)
		{
			xValue = x;
			yValue = y;
			
		}
		public void    setVisibility(boolean visibility) {isVisible = visibility;}
		public boolean getVisibility() {return isVisible;}
		public int getX() {return xValue;}
		public int getY() {return yValue;}
		public void setX(int x) {xValue = x;}
		public void setY(int y) {yValue = y;}

	}
	
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




	
