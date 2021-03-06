package srtm1;

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

public class SrtmDataExporter
{
    Coordinate coord;
    Properties params;
    File destFile;
    short[][] haloData = new short[3601][3601];
    short[][] noHaloData = new short[3600][3600];

    private final static Logger LOGGER = Logger.getLogger(SrtmDataExporter.class.getName());

    public SrtmDataExporter(Coordinate coord,Properties params)
    {
	this.coord = coord;
	this.params = params;
    }
    
    public void haloWriteSrtmDataToDisk(File fileOutput,SrtmOneTile tile) throws TopoSwapperException
    {
	FileOutputStream fos = null;
	try
	    {
		fos = new FileOutputStream(destFile,false);
		short[] origData = tile.getInputData();
		int maxRowIndex = origData.length;
		for (int i = 0;i<3601;i++)
		    {
			for (int j = 0; j <3601;j++)
			    {
				haloData[i][j] = 
				    origData[maxRowIndex*3601+j];
				fos.write(haloData[-i][j]  & 0xFFFF);
				fos.write((haloData[i][j] >> 8) & 0xFFFF);
				
			    }
			maxRowIndex--;
		    }
		renameFile(fileOutput);
	    }
	catch (IOException e)
	    {
		e.printStackTrace();
		throw new TopoSwapperException(e.getMessage());
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

    public void noHaloWriteSrtmDataToDisk() throws TopoSwapperException
    {
	FileOutputStream fos = null;
	SrtmOneFileHolder srtmFileHolder = null;
	SrtmOneTile tile = (SrtmOneTile)LookupManager.getObject();
	//System.out.println("The value of tile is " + tile);
	srtmFileHolder = (SrtmOneFileHolder)LookupManager.getInstance().lookup("SrtmOneFileHolder");
	File fileOutput = srtmFileHolder.getSrtmFile();
	try
	    {
		renameFile(fileOutput);
		fos = new FileOutputStream(destFile,false);
		short[] origData = tile.getInputData();
		if (ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN))
		    {
			int maxRowIndex = origData.length-1;
			for (int i = 0;i<3600;i++)
			    {
				for (int j = 0; j <3600;j++)
				    {
					noHaloData[j][i] = 
					    origData[maxRowIndex*3601+j];
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
					    maxRowIndex*3601+j;
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
   

    private void renameFile(File fileOutput) throws IOException
    {
	System.out.println("Renaming file");
	String spatialIndex = coord.getSpatialIndex();
	String destFileName = params.getProperty("srtm_output_dir")+"/"+spatialIndex;
	LOGGER.info("The value of destFileName is " + destFileName);
	String srcFileName = params.getProperty("srtm_output_dir")+"/"+fileOutput.getName();
	LOGGER.info("The value of srcFileName is " + srcFileName);
	File srcFile = new File(srcFileName);
	File destFile = new File(destFileName);
	if (destFile.exists())
	    {
		destFile.exists();
	    }
	Path source = srcFile.toPath();
        Files.move(source, source.resolveSibling(destFileName));
	setDestFile(destFileName);
    }


    private void setDestFile(File destFile)
    {
	this.destFile = destFile;
    }
}
