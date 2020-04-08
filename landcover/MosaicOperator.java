package landcover;

import toposwapper.TopoSwapperException;
import java.util.List;

import org.esa.beam.framework.datamodel.Product;

public interface MosaicOperator
{
    public Product mosaicTiles(List<GLCTile> glcTiles) throws TopoSwapperException;
}
