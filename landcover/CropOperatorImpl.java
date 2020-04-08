package landcover;

import toposwapper.Coordinate;
import toposwapper.SpatialIndex;
import toposwapper.TopoSwapperException;
import toposwapper.WPSSpatialIndex;

import org.esa.beam.framework.datamodel.Product;

import java.awt.Rectangle;
import java.text.DecimalFormat;
import java.util.Properties;

public class CropOperatorImpl implements CropOperator
{
    private BeamCropOperator bco;
    private GLCDataExporter glcDataExporter;
    private Properties params;
    private String wpsFileFormatPattern = "00000";
    DecimalFormat wpsFormatter = new DecimalFormat(wpsFileFormatPattern);

    public CropOperatorImpl(Properties params)
    {
	this.params = params;
	this.glcDataExporter = new GLCDataExporter(params);
	this.bco = new BeamCropOperator();
    }

    public void cropTile(Product product) throws TopoSwapperException
    {
	final int CELL_SIZE = new Integer(params.getProperty("cellSize")).intValue();
	int minLon = new Integer(params.getProperty("minLongitude")).intValue();
	int maxLon = new Integer(params.getProperty("maxLongitude")).intValue();
	final int WIDTH = maxLon - minLon;

	int minLat = new Integer(params.getProperty("minLatitude")).intValue();
	int maxLat = new Integer(params.getProperty("maxLatitude")).intValue();
	final int HEIGHT = maxLat - minLat;

	final int PIXEL_WIDTH = CELL_SIZE * WIDTH;
	final int PIXEL_HEIGHT = CELL_SIZE * HEIGHT;

	int xOffset = 0;
	int yOffset = 0;
	    
	int ystart = 1;
	int yend = CELL_SIZE;
	
	for (int i = 1 ; i <= HEIGHT; i++)
  	    {
		int xstart =1;
		int xend = CELL_SIZE;
		yOffset = PIXEL_HEIGHT - CELL_SIZE * i;
		for (int j = 1; j <= WIDTH; j++)
		    {

			Rectangle r = new Rectangle(xOffset,
						    yOffset,
						    CELL_SIZE,
						    CELL_SIZE);
			System.out.println("The value of xOffset is " + xOffset);
			System.out.println("The value of yOffset is " + yOffset);
			Product croppedImage = bco.cropTile(product,r);
			Coordinate southWesternPoint = 
			    geoReferenceThisProduct(croppedImage);
			System.out.println("The latitude of tile is " + southWesternPoint.getLatitude());
			System.out.println("The longitude of tile is " + southWesternPoint.getLongitude());
			SpatialIndex wpsIndex = new WPSSpatialIndex(wpsFormatter,
								    xstart,
								    xend,
								    ystart,
								    yend);
			southWesternPoint.setSpatialIndex(wpsIndex);
			System.out.println("WPS Spatial Index set");
			GLCTile tile = 
			    createGLCTile(croppedImage,
					  southWesternPoint);
			//Call GLC WRF Data Exporter
			glcDataExporter.noHaloWriteGLCDataToDisk(tile);
			System.exit(0);
			//
			xOffset = PIXEL_WIDTH - CELL_SIZE * j ;
			xstart += CELL_SIZE;
			xend += CELL_SIZE;
		    }

		//
		ystart += CELL_SIZE;
		yend += CELL_SIZE;
	    }
    }
    
   private Coordinate geoReferenceThisProduct(Product product)
    {
	BeamProductReader bpr = new BeamProductReader();
	Coordinate coordinate = bpr.getCoordinateOfSouthWesternCorner(product);
	return coordinate;
    }
    private GLCTile createGLCTile(Product product,Coordinate coordinate)
	throws TopoSwapperException
    {
	GLCTile glcTile = extractDataFromProduct(product,coordinate);
	return glcTile;
    }
    private GLCTile extractDataFromProduct(Product product,
					   Coordinate coordinate)
	throws TopoSwapperException
    {
	BeamProductReader bpr = new BeamProductReader();
	GLCTile glcTile = new GLCTile(coordinate);
	System.out.println("Extracting data");
	glcTile = bpr.extractDataFromProduct(product,glcTile);
	return glcTile;
    }
}
