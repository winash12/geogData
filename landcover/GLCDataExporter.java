package landcover;

import toposwapper.Coordinate;
import toposwapper.LookupManager;
import toposwapper.TopoSwapperException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Properties;

public class GLCDataExporter
{
    Coordinate coord;
    Properties params;
    File destFile;
    short[][] noHaloData = new short[3600][3600];

    private final static Logger LOGGER = Logger.getLogger(GLCDataExporter.class.getName());

    public GLCDataExporter(Properties params)
    {
	this.params = params;
    }
    

    public void noHaloWriteGLCDataToDisk(GLCTile glcTile) 
	throws TopoSwapperException
    {
	FileOutputStream fos = null;
	try
	    {
		String fileName = glcTile.getCoordinate().getSpatialIndex();
		fileName = params.getProperty("glc_output_dir") + "/"+ fileName;
		File fileOutput = new File(fileName);
		fos = new FileOutputStream(fileOutput,false);
		byte[] origData = glcTile.getData();
		if (ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN))
		    {
			int maxRowIndex = origData.length-1;
			for (int i = 0;i<3600;i++)
			    {
				for (int j = 0; j <3600;j++)
				    {
					noHaloData[j][i] = 
					    origData[maxRowIndex*3600+j];
					fos.write((noHaloData[j][i] >> 8) & 0xFF);
					fos.write(noHaloData[j][i] & 0xFF);
					
				    }
				maxRowIndex--;
			    }
		    }
		else
		    {
			int maxRowIndex = noHaloData.length-1;
			for (int i = 0;i<3600;i++)
			    {
				for (int j = 0; j <3600;j++)
				    {
					int oneDimIndex = 
					    maxRowIndex*3600+j;
					noHaloData[j][i] = 
					    origData[oneDimIndex];
					fos.write(noHaloData[j][i] & 0xFF);
					fos.write((noHaloData[j][i] >> 8) & 0xFF);
					
				    }
				maxRowIndex--;
			    }
		    }
	    }
	catch (Exception e)
	    {
		e.printStackTrace();
		new TopoSwapperException(e.getMessage());
	    }
	finally
	    {
		try
		    {
			fos.close();
		    }
		catch (Exception e)
		    {
			e.printStackTrace();
		    }
	    }
    }
}
