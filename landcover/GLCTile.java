package landcover;


import toposwapper.Coordinate;
import toposwapper.Tile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class GLCTile extends Tile
{
    protected Coordinate coord;
    protected String directoryName;
    protected String geoTiffFileName;
    private byte data[] = new byte[3600*3600];

    public GLCTile()
    {
    }
    public GLCTile(Coordinate coord)
    {
	setCoordinate(coord);
    }
    public void setDirectoryName(String directoryName)
    {
	this.directoryName = directoryName;
    }
    public String getDirectoryName()
    {
	return directoryName;
    }

    public void setGeoTiffFileName(String geoTiffFileName)
    {
	this.geoTiffFileName = geoTiffFileName;
    }
    public String getGeoTiffFileName()
    {
	return geoTiffFileName;
    }

    public void setCoordinate(Coordinate coord)
    {
	this.coord = coord;
    }
    public Coordinate getCoordinate()
    {
	return coord;
    }
    public void setData(byte[] data)
    {
	this.data = data;
    }
    public byte[] getData()
    {
	return data;
    }
}
