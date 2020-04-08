package srtm1;

import toposwapper.GridDataManager;
import toposwapper.LookupManager;
import toposwapper.TopoSwapper;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;



public class SrtmTopoSwapper extends TopoSwapper
{
        public static void main(String[] args)
    {
	try
	    {
		System.out.println("Starting Program Execution");
		Properties prop = new Properties();
		InputStream inputStream = SrtmTopoSwapper.class.getClassLoader().getResourceAsStream("srtm1/configSRTM1.properties");
		prop.load(inputStream);
		GridDataManager gridManager = new GridDataManager(prop);
		gridManager.createGridDataSetBoundaries();
		gridManager.populateGrid();
		List wpsIndex = gridManager.generateSpatialIndexesForWPS();
		LookupManager.getInstance().add("SrtmTopoFileReaderHelper",new SrtmTopoFileReaderHelper(prop));
		LookupManager.getInstance().add("SrtmDownloadManager",SrtmOneDownloadManager.getInstance());
		SrtmOneDownloadManager.getInstance().setParameters(prop);
		SrtmOneDownloadManager.getInstance().loadIndexFile();
		SrtmTopoDirectoryLoader tdl = new SrtmTopoDirectoryLoader(prop);
		tdl.downloadSRTMZipFiles(wpsIndex);
		//tdl.processSRTMFiles(wpsIndex,prop);
	    }
	catch(Exception e)
	    {
		e.printStackTrace();
	    }
    }
}
