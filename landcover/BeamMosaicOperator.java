package landcover;

import toposwapper.TopoSwapperException;

import org.esa.beam.framework.datamodel.GeoCoding;
import org.esa.beam.framework.datamodel.GeoPos;
import org.esa.beam.framework.datamodel.PixelPos;
import org.esa.beam.framework.datamodel.Band;
import org.esa.beam.framework.dataio.ProductIO;
import org.esa.beam.framework.datamodel.Product;
import org.esa.beam.framework.gpf.GPF;
import org.esa.beam.gpf.operators.standard.MosaicOp.Variable;
import org.esa.beam.gpf.operators.standard.MosaicOp.Condition;




import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class BeamMosaicOperator
{
    Properties params;
    public BeamMosaicOperator(Properties params)
    {
	this.params = params;
    }

    public Product performMosaicOperation(Product[] sourceProducts) throws TopoSwapperException
    {
	Product targetProduct = null;
	try
	    {
		GPF.getDefaultInstance().getOperatorSpiRegistry().loadOperatorSpis();
		//Config file params
		int cellSize = new Integer(params.getProperty("cellSize")).intValue();
		double pixelSize = 1/3600;
		double southBound = new Float(params.getProperty("minLatitude")).doubleValue();
		double northBound = new Integer(params.getProperty("maxLatitude")).doubleValue();
		double westBound = new Integer(params.getProperty("minLongitude")).doubleValue();
		double eastBound = new Integer(params.getProperty("maxLongitude")).doubleValue();
		//
		HashMap<String, Object> parameters = new HashMap<>();
		Variable[] variable = new Variable[1];
		Variable v = new Variable("band_1","band_1");
		variable[0] = v;
		Condition[] conditions = new Condition[]{
		    new Condition("b1_cond", "band_1 != 0", false)
		};
		parameters.put("conditions", conditions);
		parameters.put("crs", "EPSG:4326");
		parameters.put("westBound", westBound);
		parameters.put("northBound",northBound);
		parameters.put("eastBound", eastBound);
		parameters.put("southBound",southBound);
		parameters.put("variables",variable);
		parameters.put("pixelSizeX",0.000277778);
		parameters.put("pixelSizeY",0.000277778);
		targetProduct = GPF.createProduct("Mosaic", parameters, sourceProducts);
		//ProductIO.writeProduct(targetProduct, "/home/aswin/ChineseGLC/mosaic", "GEOTIFF");
	    }
	catch(Exception e)
	    {
		e.printStackTrace();
		throw new TopoSwapperException(e.getMessage());
	    }
	return targetProduct;
    }
}
