public class NewTest
{
    public static void main(String[] args)
    {
	int maxRowIndex = 1201;
	for (int i = 0;i<1201;i++)
	    {
		for (int j = 0; j <1201;j++)
		    {
			//System.out.println("The value of maxrow Index is " + maxRowIndex);
			int ind = maxRowIndex*1201 + j;
			System.out.println(ind);
		    }
		maxRowIndex--;
	    }

    }
}
