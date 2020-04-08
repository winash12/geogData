package landcover;

import toposwapper.LookupManager;
import toposwapper.TopoSwapperException;

import org.esa.beam.framework.datamodel.Product;

import java.util.List;
import java.util.Properties;

public class MosaicOperatorImpl implements MosaicOperator
{
    Properties params;

    public MosaicOperatorImpl(Properties params)
    {
	this.params = params;
    }
    public Product mosaicTiles(List<GLCTile> glcTiles) 
	throws TopoSwapperException
    {
	
	GLCFileReaderHelper glcfrh = (GLCFileReaderHelper)LookupManager.lookup("GLCFileReaderHelper");
	Product[] products = glcfrh.readDataProduct(glcTiles);
	BeamMosaicOperator bmo = new BeamMosaicOperator(params);
	return bmo.performMosaicOperation(products);
    }
}
