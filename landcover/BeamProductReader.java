package landcover;

import toposwapper.Coordinate;
import toposwapper.TopoSwapperException;

import org.esa.beam.framework.datamodel.Band;
import org.esa.beam.framework.datamodel.GeoCoding;
import org.esa.beam.framework.datamodel.GeoPos;
import org.esa.beam.framework.datamodel.PixelPos;
import org.esa.beam.framework.dataio.ProductIO;
import org.esa.beam.framework.datamodel.Product;

public class BeamProductReader
{
    public BeamProductReader()
    {
    }
    
    public Product read(String prefix,
			String geoTiffFile,
			String inputDirectory) throws TopoSwapperException
    {
	Product product = null;
	try
	    {
		String fileName = prefix + "/" + inputDirectory + "/" + geoTiffFile;
		product = ProductIO.readProduct(fileName);
	    }
	catch (Exception e)
	    {
		e.printStackTrace();
		throw new TopoSwapperException(e.toString());
	    }
	return product;
    }
    
    public Coordinate getCoordinateOfSouthWesternCorner(Product product)
    {
	int height = product.getSceneRasterHeight();
	GeoCoding gc = product.getGeoCoding();
	PixelPos pp = new PixelPos(0,height);
	GeoPos gp1 = gc.getGeoPos(pp,null);
	int latitude = Math.round(gp1.getLat());
	int longitude = Math.round(gp1.getLon());
	Coordinate coordinate = new Coordinate(latitude,longitude);
	return coordinate;

    }
    public  GLCTile extractDataFromProduct(Product product,
					   GLCTile glcTile) 
	throws TopoSwapperException
    {
	Band[] bands = product.getBands();
	int rasterWidth = product.getSceneRasterWidth();
	int rasterHeight = product.getSceneRasterHeight();
	System.out.println("The rasterWidth is " + rasterWidth);
	System.out.println("The rasterWidth is " + rasterHeight);
	byte[] data = glcTile.getData();
	try
	    {
		System.out.println("The bands are " + bands.length);
		for (int k = 0;k < 1; k++)
		    {
			Band band = bands[k];
			band.loadRasterData();
			int l = 0;
			for (int i = 0; i < rasterHeight;i++)
			    {
				for (int j = 0; j < rasterWidth; j++)
				    {
					data[l] = (byte)band.getPixelInt(j,i);
					l++;
				    }
			    }

		    }
	    }
	catch (Exception e)
	    {
		e.printStackTrace();
		throw new TopoSwapperException(e.getMessage());
	    }
	glcTile.setData(data);
	return glcTile;
    }
}
