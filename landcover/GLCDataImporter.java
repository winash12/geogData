package landcover;

import toposwapper.TopoSwapperException;

import java.util.List;
import java.util.Properties;

import org.esa.beam.framework.datamodel.Product;
import org.esa.beam.framework.datamodel.GeoCoding;
import org.esa.beam.framework.datamodel.GeoPos;
import org.esa.beam.framework.datamodel.PixelPos;

public class GLCDataImporter
{
    Properties params;
    public GLCDataImporter(Properties params)
    {
	this.params = params;
    }
    public void importGLCTiles(List<GLCTile> glcTiles) 
	throws TopoSwapperException
    {
	MosaicOperator mo = new MosaicOperatorImpl(params);
	Product product = mo.mosaicTiles(glcTiles);
	int height = product.getSceneRasterHeight();
	System.out.println("The height of raster is " + height);
	int width = product.getSceneRasterWidth();
	System.out.println("The width of raster is " + width);
	printCoordinateOfCorners(product);
	CropOperator co = new CropOperatorImpl(params);
	co.cropTile(product);
    }

    public void printCoordinateOfCorners(Product product)
    {
	int height = product.getSceneRasterHeight();
	int width = product.getSceneRasterWidth();
	GeoCoding gc = product.getGeoCoding();
	PixelPos pp = new PixelPos(0,height);
	GeoPos gp1 = gc.getGeoPos(pp,null);
	float latitude = gp1.getLat();
	float longitude = gp1.getLon();
	System.out.println("The value of sw latitude is " + Math.round(latitude));
	System.out.println("The value of sw longitude is " + Math.round(longitude));	
	PixelPos pp1 = new PixelPos(0,0);
	GeoPos gp2 = gc.getGeoPos(pp1,null);
	float latitude1 = gp2.getLat();
	float longitude1 = gp2.getLon();
	System.out.println("The value of nw latitude is " + Math.round(latitude1));
	System.out.println("The value of nw longitude is " + Math.round(longitude1));	
	PixelPos pp2 = new PixelPos(width,0);
	GeoPos gp3 = gc.getGeoPos(pp2,null);
	float latitude2 = gp3.getLat();
	float longitude2 = gp3.getLon();
	System.out.println("The value of ne latitude is " + Math.round(latitude2));
	System.out.println("The value of ne longitude is " + Math.round(longitude2));	
	PixelPos pp3 = new PixelPos(width,height);
	GeoPos gp4 = gc.getGeoPos(pp3,null);
	float latitude3 = gp4.getLat();
	float longitude3 = gp4.getLon();
	System.out.println("The value of se latitude is " + Math.round(latitude3));
	System.out.println("The value of se longitude is " + Math.round(longitude3));	
    }

}
