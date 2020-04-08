import java.awt.Rectangle;
public class BurkeTest
{
    public BurkeTest()
    {
    }
    public static void main(String[] args)
    {
	int cellSize = 3600;
	for (int i = 0; i < 40* cellSize ;i += cellSize)
	    {
		for (int j = 0 ; j < 34*cellSize ;j += cellSize)
		    {
			System.out.println("The values of i " + i + "and j is " + j);
			Rectangle rect = new Rectangle(i,
						       j,
						       cellSize,
						       cellSize);		     

		    }
	    }
    }
}
