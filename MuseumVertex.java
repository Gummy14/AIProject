
public class MuseumVertex {
	private boolean isVisible;
	int xValue;
	int yValue;
	public MuseumVertex(int x, int y)
	{
		xValue = x;
		yValue = y;
		
	}
	public void setVisibility(boolean visibility) {isVisible = visibility;}
	public boolean getVisibility() {return isVisible;}
	public int getX() {return xValue;}
	public int getY() {return yValue;}
	public void setX(int x) {xValue = x;}
	public void setY(int y) {yValue = y;}
}
