package landcover;

import toposwapper.GridDataManager;
import toposwapper.LookupManager;
import toposwapper.TopoSwapper;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;



public class LandCoverSwapper extends TopoSwapper
{
        public static void main(String[] args)
    {
	try
	    {
		System.out.println("Starting Program Execution");
		Properties prop = new Properties();
		InputStream inputStream = LandCoverSwapper.class.getClassLoader().getResourceAsStream("landcover/configGLC.properties");
		prop.load(inputStream);
		GridDataManager gridManager = new GridDataManager(prop);
		gridManager.createGridDataSetBoundaries();
		gridManager.populateGrid();
		List wpsIndex = gridManager.generateSpatialIndexesForWPS();
		LookupManager.getInstance().add("GLCFileReaderHelper",new GLCFileReaderHelper(prop));
		GLCDirectoryLoader gdl = new GLCDirectoryLoader(prop);
		//gdl.extractGLCFilesFromZipFiles();
		gdl.collectGLCGeoTiffFiles();
		gdl.sortGeoTiffFiles();
		GLCDataImporter glcdi = new GLCDataImporter(prop);
		glcdi.importGLCTiles(gdl.getGLCTiles());
	    }
	catch(Exception e)
	    {
		e.printStackTrace();
	    }
    }
}
