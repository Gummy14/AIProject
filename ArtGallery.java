import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Polygon;

public class ArtGallery extends JPanel implements ActionListener{
	String x;
	String y;
	int test = 18;
	
	double distance = 0;
	double midx = 0;
	double midy = 0;
	int size = 26;
	int count = 0;
	int setx[] = new int[size];
	int sety[] = new int[size];
	int sightx[] = new int[size];
	int sighty[] = new int[size];
	Polygon p;
	boolean intersect = false;
	public void readFile() throws IOException
	{
		FileReader opnfile = new FileReader("Gallery.txt");
		BufferedReader BReader = new BufferedReader(opnfile);
		String crd;
		int linecount = 0;
		while((crd = BReader.readLine()) != null)
		{
			
			x = crd.substring(0,3);
			y = crd.substring(6);
			
				//System.out.println("Vertex #"+(linecount+1)+" ("+x+", "+y+")");
				setx[linecount] = Integer.parseInt(x);
				sety[linecount] = Integer.parseInt(y);
				System.out.println("Vertex #"+(linecount)+" ("+setx[linecount]+", "+sety[linecount]+")");
				linecount++;
		}
		BReader.close();
		opnfile.close();	
	}
	public double getDistance(double x1, double y1, double x2, double y2)
	{
		distance = Math.sqrt(((x2 - x1)*(x2- x1))+((y2 - y1)*(y2 - y1)));
		return distance;
	}
	public void getMid(int x1, int y1, int x2, int y2)
	{
		midx = (x1+x2)/2;
		midy = (y1+y2)/2;
	}
	public void paintComponent(Graphics g) 
	{	
		//count  = 0;
		double xtest = setx[test]*5;
		double ytest = sety[test]*5;
		//Every vertex can see the other two it's connected to
		//get the next one
		/*if(test != 25)
		{
			sightx[0] = setx[test+1];
			sighty[0] = sety[test+1];
		}
		else if(test == 25)
		{
			sightx[0] = setx[0];
			sighty[0] = sety[0];
		}
		//get the previous one
		if(test != 0)
		{
			sightx[1] = setx[test-1];
			sighty[1] = sety[test-1];
			
		}
		else if(test == 0)
		{
			sightx[1] = setx[25];
			sighty[1] = sety[25];
		}
		count=2;*/
		super.paintComponent(g);
		for(int i = 0; i<setx.length;i++)
		{
			setx[i] = setx[i]*5;
			sety[i] = sety[i]*5;
			g.setColor(Color.BLACK);
			g.fillOval(((int)setx[i])-5,((int)sety[i])-5, 10, 10);
		}
		p = new Polygon(setx, sety, size);
		g.drawPolygon(p);
		//g.fillPolygon(p);
		g.setColor(Color.RED);
		
		//compare every other vertex with the one we're looking at to see what it can see
		for(int i = 0; i<setx.length;i++)
		{
			
			//check every edge to see if there's any intersecting
			// if so, don't include that edge
			for(int h = 0; h<setx.length-1;h++)
			{
				if(h != i && h+1 != i)
				{
					//for right angles
					if(p.contains(xtest+1, ytest-1) == false && p.contains(xtest-1, ytest+1) == true)
					{
						intersect = Line2D.linesIntersect(xtest-1,ytest+1,setx[i],sety[i],setx[h],sety[h],setx[h+1],sety[h+1]);
					}
					else if(p.contains(xtest+1, ytest+1) == false && p.contains(xtest-1, ytest-1) == true)
					{
						intersect = Line2D.linesIntersect(xtest-1,ytest-1,setx[i],sety[i],setx[h],sety[h],setx[h+1],sety[h+1]);
					}
					else if(p.contains(xtest-1, ytest+1) == false && p.contains(xtest+1, ytest-1) == true)
					{
						intersect = Line2D.linesIntersect(xtest+1,ytest-1,setx[i],sety[i],setx[h],sety[h],setx[h+1],sety[h+1]);
					}
					else if(p.contains(xtest-1, ytest-1) == false && p.contains(xtest+1, ytest+1) == true)
					{
						intersect = Line2D.linesIntersect(xtest+1,ytest+1,setx[i],sety[i],setx[h],sety[h],setx[h+1],sety[h+1]);
					}
					else
					{
						//used for non-right angles
						getMid(setx[test-1],sety[test-1], setx[test+1],sety[test+1]);
						//g.drawLine((((int)setx[test])), (((int)sety[test])), (((int)midx)),(((int)midy)));
						
						//for exterior acute angles
						//jutting out of the museum
						if(p.contains(midx,midy))
						{
							//double slope = (midy-ytest)/(midx-xtest);
							if(midx>xtest && midy>ytest)
							{
								while(Math.abs(midx-xtest)>10 || Math.abs(midy-ytest)>10)
								{
								while(true)
								{
									if(p.contains(midx-1, midy))
									{
										midx=midx-1;
									}
									else
									{
										break;
									}
								}
								while(true)
								{
									if(p.contains(midx, midy-1))
									{
										midy=midy-1;
									}
									else
									{
										break;
									}
								}
								}
							}
							else if(midx>xtest && midy<ytest)
							{
								while(Math.abs(midx-xtest)>10 || Math.abs(midy-ytest)>10)
								{
								while(true)
								{
									if(p.contains(midx-1, midy))
									{
										midx=midx-1;
									}
									else
									{
										break;
									}
								}
								while(true)
								{
									if(p.contains(midx, midy+1))
									{
										midy=midy+1;
									}
									else
									{
										break;
									}
								}
								}
							}
							else if(midx<xtest && midy<ytest)
							{	
								while(Math.abs(midx-xtest)>10 || Math.abs(midy-ytest)>10)
								{
								while(true)
								{
									if(p.contains(midx+1, midy))
									{
										midx=midx+1;
									}
									else
									{
										break;
									}
								}
								while(true)
								{
									if(p.contains(midx, midy+1))
									{
										midy=midy+1;
									}
									else
									{
										break;
									}
								}
								}
								//g.drawLine((((int)setx[test])), (((int)sety[test])), (((int)midx)),(((int)midy)));
							}
							else if(midx<xtest && midy>ytest)
							{
								while(Math.abs(midx-xtest)>10 || Math.abs(midy-ytest)>10)
								{
								while(true)
								{
									if(p.contains(midx+1, midy))
									{
										midx=midx+1;
									}
									else
									{
										break;
									}
								}
								while(true)
								{
									if(p.contains(midx, midy-1))
									{
										midy=midy-1;
									}
									else
									{
										break;
									}
								}
								}
							}
							
							//xtest = midx;
							//ytest = midy;
							intersect = Line2D.linesIntersect(midx,midy,setx[i],sety[i],setx[h],sety[h],setx[h+1],sety[h+1]);
							
						}
						else
						{
							//for interior acute angles
							//not done yet
							if(i != test-1 || i != test+1)
							{
								if(midx>xtest && midy>ytest)
								{
									while(Math.abs(midx-xtest)>2 || Math.abs(midy-ytest)>2)
									{
									while(true)
									{
										if(p.contains(midx, midy-1) == false)
										{
											midy=midy-1;
										}
										else
										{
											break;
										}

									}
										if(p.contains(midx-1, midy)  == false)
										{
											midx=midx-1;
										}
										else
										{
											break;
										}

									}
									//midx=midx-3;
									midy=midy-3;
								}
								else if(midx>xtest && midy<ytest)
								{
									while(Math.abs(midx-xtest)>2 || Math.abs(midy-ytest)>2)
									{
									while(true)
									{
										if(p.contains(midx-1, midy) == false)
										{
											midx=midx-1;
										}
										else
										{
											break;
										}

									}
										if(p.contains(midx, midy+1)  == false)
										{
											midy=midy+1;
										}
										else
										{
											break;
										}

									}
									midx=midx-9;
									midy=midy+5;
									
								}
								else if(midx<xtest && midy<ytest)
								{									
									while(Math.abs(midx-xtest)>2 || Math.abs(midy-ytest)>2)
									{
									while(true)
									{
										if(p.contains(midx, midy+1) == false)
										{
											midy=midy+1;
										}
										else
										{
											break;
										}

									}
										if(p.contains(midx+1, midy)  == false)
										{
											midx=midx+1;
										}
										else
										{
											break;
										}

									}
									
									midx=midx+2;
									midy=midy+5;
									
								}
								else if(midx<xtest && midy>ytest)
								{
									while(Math.abs(midx-xtest)>2 || Math.abs(midy-ytest)>2)
									{
									while(true)
									{
										if(p.contains(midx+1, midy) == false)
										{
											midx=midx+1;
										}
										else
										{
											break;
										}

									}
										if(p.contains(midx, midy-1)  == false)
										{
											midy=midy-1;
										}
										else
										{
											break;
										}

									}
									
									midx=midx+3;
									midy=midy-3;
									
								}
								intersect = Line2D.linesIntersect(midx,midy,setx[i],sety[i],setx[h],sety[h],setx[h+1],sety[h+1]);
							}
						}
					}
				}
				//if intersecting, don't add line
				if(intersect == true)
				{
					break;
				}
			}
			//if not intersecting, do add line
			if(intersect == false)
			{
				sightx[count] = setx[i]/5;
				sighty[count] = sety[i]/5;
				count++;
			}
			intersect = false;
			
		}
		for(int i  = 0; i<count;i++)
		{
			System.out.print("("+sightx[i]+","+sighty[i]+")");
			g.drawLine((((int)setx[test])), (((int)sety[test])), (((int)sightx[i]*5)),(((int)sighty[i]*5)));
		}
		//g.setColor(Color.GREEN);
		//g.fillPolygon(sightx, sighty, count);
		count = 0;
	}
	public static void main(String[] args) throws IOException
	{
		ArtGallery start = new ArtGallery();
		start.readFile();
		JFrame k = new JFrame();
		k.add(start);
		k.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		k.setSize(1000, 1000);
		k.setTitle("Art Gallery");
		k.setBackground(Color.WHITE);
		k.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
