import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Polygon;
import java.util.Random;
import java.util.Arrays;

public class WoC extends JPanel implements ActionListener{
	private static int crowdSize = 10;
	private static int numberOfGenerations = 50;
	private static int size = 21;
	static ArrayList<GA> Crowd = new ArrayList<GA>();
	static Polygon MuseumOutline;
	public static ArrayList<Color> colors = new ArrayList<Color>();
	private static ArrayList<MuseumVertex> BuildingVertices = new ArrayList<MuseumVertex>();
	private static ArrayList<Polygon> PolygonList = new ArrayList<Polygon>();
	private static int[] setofX = new int[size];
	private static int[] setofY = new int[size];
	private static int[] thepolyX      = new int[361];
	private static int[] thepolyY      = new int[361];
	int[] WoCSolution = new int[size];
	private static int finalCount = 0;
	

	public static void createCrowd() throws IOException
	{
		for(int i = 0; i<crowdSize;i++)
		{
			Crowd.add(new GA(numberOfGenerations));
		}
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
		
	}
	public static int[] compareSolutions()
	{
		int[] combinedSolution = new int[size];
		int oneCount = 0;
		int valueInMemberSoultion = 0;
		for(int i = 0; i<size;i++)
		{
			oneCount = 0;
			for(int j = 0; j<Crowd.size();j++)
			{
				valueInMemberSoultion = Crowd.get(j).getSolution(i);
				if(valueInMemberSoultion == 1)
				{
					oneCount++;
				}
				
			}
			if(oneCount>((crowdSize)*.75))//If at least 3/4 of solutions have a camera here, add one too
			{
				combinedSolution[i] = 1;
			}
		}
		
		return combinedSolution;
	}
	public static void makeSightLines(int currentCam)
	{
		int ang = 0;
		while(ang <=360)
		{
			double length=5;
			double 	endX,endY = 0;
			
			endX = setofX[currentCam] + length*Math.cos(Math.toRadians(ang)); // endx, endy is final point on line
			endY = setofY[currentCam] + length*Math.sin(Math.toRadians(ang)); // while setx and y are the vertex it starts
			while(MuseumOutline.contains(endX,endY) == true) // while the end point is still inside the polygon
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
	public void paintComponent(Graphics g) 
	{	
		//count  = 0;
		super.paintComponent(g);
		g.drawPolygon(MuseumOutline);
		int colorCount = 0;
		for(int j = 0;j<WoCSolution.length;j++)
		{
			if(WoCSolution[j] == 1)
			{
				if (colorCount >= 11) colorCount = 0;
				colorCount++;
				g.setColor(colors.get(colorCount));
				g.fillOval(((int)setofX[j]-5),((int)setofY[j]-5), 10, 10);
			}
		}
		colorCount = 0;
		for(int i = 0; i < PolygonList.size(); i++) {
			if (colorCount >= 11) colorCount = 0;
			colorCount++;
			g.setColor(colors.get(colorCount));
			g.drawPolygon(PolygonList.get(i));
		}
		
	}
	public WoC() throws IOException
	{
		createCrowd();
		readFile();
		int ScalingConstant = 5;
		for(int i = 0; i< BuildingVertices.size();i++)
		{
			//Scale the building to meet the screen needs
			BuildingVertices.get(i).setX(BuildingVertices.get(i).getX()*ScalingConstant);	
			BuildingVertices.get(i).setY(BuildingVertices.get(i).getY()*ScalingConstant);	
			setofY[i] = setofY[i]*ScalingConstant;
			setofX[i] = setofX[i]*ScalingConstant;
			//g.fillOval(((int)setX[i]-5)),((int)setY[i]-5), 10, 10);
		}
		MuseumOutline = new Polygon(setofX, setofY, size);
		WoCSolution = compareSolutions();
		//This code segment causes more problems than it fixes
		//clean up solution
		/*for(int i = 0; i<size-3;i=i+3)
		{
			if(WoCSolution[i] == 1 && WoCSolution[i+3] == 1)
			{
				WoCSolution[i+1] = 0;
				WoCSolution[i+2] = 0;
			}
		}*/
		PolygonList.clear();
		System.out.print("\nWoC Solution	       ");
		for(int i = 0;i<WoCSolution.length;i++)
		{
			System.out.print(WoCSolution[i]);
			if(WoCSolution[i] == 1)
			{
				makeSightLines(i);
				finalCount++;
			}
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		long startTime = System.currentTimeMillis();
		WoC woc = new WoC();
		colors.add(Color.BLUE);
		colors.add(Color.RED);
		colors.add(Color.GREEN);
		colors.add(Color.MAGENTA);
		colors.add(Color.PINK);
		colors.add(Color.ORANGE);
		colors.add(Color.DARK_GRAY);
		colors.add(Color.LIGHT_GRAY);
		colors.add(Color.YELLOW);
		colors.add(Color.CYAN);
		colors.add(Color.getColor("#663399"));
		colors.add(Color.getColor("#b29aaa"));
		System.out.print("\nNumber of Camera used: "+finalCount);
		JFrame mainWindow = new JFrame();
		mainWindow.add(woc);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setTitle("Art Gallery");
		//mainWindow.setBackground(Color.WHITE);
		mainWindow.setSize(1000, 1000);
		mainWindow.setVisible(true);
		long endTime = System.currentTimeMillis();
		double elapsedSeconds = (endTime - startTime)/ 1000.0;
		System.out.print("\nTime taken in ms: "+elapsedSeconds+"s");
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
