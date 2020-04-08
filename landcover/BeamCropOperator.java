package landcover;

import toposwapper.TopoSwapperException;

import org.esa.beam.framework.datamodel.Product;
import org.esa.beam.framework.gpf.GPF;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.HashMap;


public class BeamCropOperator
{

    public Product  cropTile(Product product,
			     Rectangle region) throws TopoSwapperException
    {
	Product targetProduct = null;
	try
	    {
		GPF.getDefaultInstance().getOperatorSpiRegistry().loadOperatorSpis();
		HashMap<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("region", region);
		parameterMap.put("copyMetaData",true);
		targetProduct = GPF.createProduct("Subset", 
						  parameterMap, 
						  product);
		int height = targetProduct.getSceneRasterHeight();
		System.out.println("The height of raster is " + height);
		int width = targetProduct.getSceneRasterWidth();
		System.out.println("The width of raster is " + width);
	    }
	catch (Exception e)
	    {
		e.printStackTrace();
		throw new TopoSwapperException(e.getMessage());
	    }
	return targetProduct;
    }
}
