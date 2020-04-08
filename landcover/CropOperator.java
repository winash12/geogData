package landcover;

import toposwapper.TopoSwapperException;

import org.esa.beam.framework.datamodel.Product;

public interface CropOperator
{
    public void cropTile(Product product) throws TopoSwapperException;
}
