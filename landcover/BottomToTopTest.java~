import java.awt.Rectangle;

public class BottomToTopTest
{
    public BottomToTopTest()
    {
    }
    public static void main(String[] args)
    {
	int width = 40;
	int height = 34;
	int cellSize = 3600;
	int xOffset = 0;
	int yOffset = 0;
	int pixelWidth = cellSize * width;
	int pixelHeight = cellSize * height;
	for (int i = 1 ; i <= 34; i++)
  	    {

		for (int j = 1; j <= 40; j++)
		    {
			yOffset = pixelWidth - cellSize * j ;
			Rectangle r = new Rectangle(xOffset,
						    yOffset,
						    cellSize,
						    cellSize);
			System.out.println("The value of xOffset is " + xOffset);
			System.out.println("The value of yOffset is " + yOffset);
			//crop(r);
		    }
		xOffset = pixelHeight - cellSize * i;
	    }
    }
}
