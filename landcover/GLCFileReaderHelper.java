package landcover;

import toposwapper.TopoSwapperException;

import java.util.List;
import java.util.Properties;

import org.esa.beam.framework.datamodel.Product;

public class GLCFileReaderHelper
{
    Properties params;

    public GLCFileReaderHelper(Properties params)
    {
	this.params = params;
    }
    public Product[] readDataProduct(List<GLCTile> glcTiles) throws TopoSwapperException
     {
	 Product[] products = new Product[glcTiles.size()];
	 BeamProductReader bpr = new BeamProductReader();
	 int i = 0;
	 String prefix = params.getProperty("glc_extract_dir");
	 prefix = prefix + "/landCoverData";
	 for (GLCTile glcTile : glcTiles)
	     {
		 Product product = bpr.read(prefix,
					    glcTile.getGeoTiffFileName(),
					    glcTile.getDirectoryName());
		 products[i] = product;
		 i++;
	    }
	 return products;
    }
}
