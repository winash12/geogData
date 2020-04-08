package srtm1;


import toposwapper.Coordinate;
import toposwapper.CoordinateComparator;
import toposwapper.LookupManager;
import toposwapper.TopoDirectoryLoader;
import toposwapper.TopoSwapperException;

import java.util.ArrayList;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Iterator;
import java.util.Properties;

public class SrtmTopoDirectoryLoader extends TopoDirectoryLoader
{
    private Properties params;

    public SrtmTopoDirectoryLoader(Properties params) throws TopoSwapperException
    {
	super();
	this.params = params;
	createRequiredDirectories();
    }

    public void createRequiredDirectories() throws TopoSwapperException
    {
	boolean status = new File(params.getProperty("srtm_output_dir")).mkdirs();
	String path =params.getProperty("srtm_dir")+"/"+params.getProperty("OCEANIC_REGION"); 
	boolean status2 =new File(path).mkdirs();
	if (status && status2)
	    {
		return;
	    }
	else
	    {
		new TopoSwapperException("Unable to create directories");
	    }
    }


    protected boolean isZipFilePresentInImportDirectory() throws TopoSwapperException
    {
	boolean isZipFilePresent = false;
	try
	    {
		File f = new File(params.getProperty("srtm_dir"));
		FilenameFilter textFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
			    String lowercaseName = name.toLowerCase();
			    if (lowercaseName.endsWith(".zip")) {
				return true;
			    } else {
				return false;
			    }
			}
		    };
	
		File[] files = f.listFiles(textFilter);
		for (File file : files) {
		    if (file.isFile()) 
			//System.out.print("     file:");
			//	System.out.println(file.getCanonicalPath());
			isZipFilePresent = true;
		    break;
		}
	    }
	catch (Exception e)
	    {
		e.printStackTrace();
		throw new TopoSwapperException("I/O error");
	    }
	return isZipFilePresent;
    }

    public void downloadSRTMZipFiles(List coordinates) throws 
	TopoSwapperException
    {
	boolean isZipFilePresent = isZipFilePresentInImportDirectory();
	if (isZipFilePresent)
	    {
		Iterator it = coordinates.iterator();
		while (it.hasNext())
		    {
			Coordinate coord = (Coordinate)it.next();
			SrtmOneTile tile = new SrtmOneTile(coord);
			LookupManager.add(tile);
			SrtmDataImporter importer = new SrtmDataImporter();
			importer.download();
			LookupManager.remove(tile);
		    }
	    }
    }

    public void processSRTMFiles(List coordinates) throws TopoSwapperException
    {
	Iterator it = coordinates.iterator();
	while (it.hasNext())
	    {
		Coordinate coord = (Coordinate)it.next();
		SrtmOneTile tile = new SrtmOneTile(coord);
		LookupManager.add(tile);
		SrtmDataImporter reader = new SrtmDataImporter();
		reader.load();
		System.out.println("Finished importing file");
		SrtmDataExporter writer = new SrtmDataExporter(coord,params);
		writer.noHaloWriteSrtmDataToDisk();
		LookupManager.remove(tile);
		//FileWriter code goes here
	    }
    }
	
    /*public List<SrtmTile> generateSrtmList(final File folder) 
    {
	List<SrtmTile> listOfTiles = new ArrayList<SrtmTile>();
	for (final Coordinate coord : folder.listFiles()) 
	    {
		SrtmTile srtmTile = new SrtmTile(coord);
		listOfTiles.add(srtmTile);
	    }
	return listOfTiles;
	}*/
    

    /*public void printWesternMostPoint(List<SrtmOneTile> list)
    {
	CoordinateComparator cc = new CoordinateComparator();
	Coordinate coord = cc.getWesternMostCoordinate(list);
	}*/

    /*public void printSouthernMostPoint(List<SrtmOneTile> list)
    {
	CoordinateComparator cc = new CoordinateComparator();
	Coordinate coord = cc.getSouthernMostCoordinate(list);
	}*/

    /*public static void main(String[] args)
    {
	try
	    {
		Coordinate coord = new Coordinate(30,79);
		WPSSpatialIndex wps = new WPSSpatialIndex("00001-01200.00001-01200");
		coord.setWPSSpatialIndex(wps);
		Properties parameters = new Properties();
		InputStream inputStream = SrtmDataImporter.class.getClassLoader().getResourceAsStream("config.properties");
		parameters.load(inputStream);
		SrtmDataImporter reader = new SrtmDataImporter(coord,parameters);
		reader.load();
		SrtmDataExporter writer = new SrtmDataExporter(coord,parameters);
		writer.noHaloWriteSrtmDataToDisk(reader.getSrtmFile(),reader.getSrtmTile());
	    }
	catch (Exception e)
	    {
		e.printStackTrace();
	    }
	    }*/
}
