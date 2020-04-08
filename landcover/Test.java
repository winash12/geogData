import java.awt.Rectangle;
public class Test
{
    public Test()
    {
    }
    public static void main(String[] args)
    {
	final int CELL_SIZE = 3600;

	final int WIDTH = 40;
	final int HEIGHT = 34;
	
	final int PIXEL_WIDTH = CELL_SIZE * WIDTH;
	final int PIXEL_HEIGHT = CELL_SIZE * HEIGHT;
	
	//int xOffset = 0;
	//int yOffset = 0;
	for (int  xOffset = PIXEL_WIDTH - CELL_SIZE; xOffset >= 0; xOffset -= CELL_SIZE)
	    {
		for (int yOffset = PIXEL_HEIGHT - CELL_SIZE; yOffset >= 0; yOffset -= CELL_SIZE)
		    {

			System.out.println("The value of xOffset is " + xOffset);
			System.out.println("The value of yOffset is " + yOffset);
			Rectangle r = new Rectangle(xOffset,
						    yOffset,
						    CELL_SIZE,
						    CELL_SIZE);

		    }
	    }
    }
}
