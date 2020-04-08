package srtm1;

import toposwapper.Coordinate;
import toposwapper.Tile;

import java.util.List;
import java.util.ArrayList;

public class SrtmOneTile extends Tile
{
    private short data[] = new short[3601*3601*2];
    private List<Short> srtmArray  =  new ArrayList<Short>();
    protected Coordinate coord;
    protected boolean containsVoid;

    public static final int SECONDS_PER_MINUTE = 60;
    public static final int  HGT_RES = 1;

    public static final int HGT_ROW_LENGTH = 3601;

    public SrtmOneTile()
    {
    }

    public SrtmOneTile(Coordinate coord) 
    {
	setCoordinate(coord);
    }

    public void addElevation(Short value)
    {
	srtmArray.add(value);
    }

    public void setVoidStatus(boolean containsVoid)
    {
	this.containsVoid = containsVoid;
    }
    
    public boolean doesContainVoid()
    {
	return containsVoid;
    }

    public void setCoordinate(Coordinate coord)
    {
	this.coord = coord;
    }
    public Coordinate getCoordinate()
    {
	return coord;
    }
    
    public short[] getInputData()
    {
	int i = 0;
	for (Short s : srtmArray)
	    {
		data[i] = s;
		i++;
	    }
	return data;
    }
    
    public short getElevation(double lon, double lat)
    {
	double fLat = frac(coord.getLatitude())*SECONDS_PER_MINUTE;
	double fLon = frac(coord.getLongitude())*SECONDS_PER_MINUTE;

	//compute offset within data
	int row = (int)Math.round(fLat * SECONDS_PER_MINUTE/HGT_RES);
	int col = (int)Math.round(fLon * SECONDS_PER_MINUTE/HGT_RES);
	
	row = HGT_ROW_LENGTH - row;
	int cell = (HGT_ROW_LENGTH * (row-1)) + col;
	
	short elevation =  data[cell];
	return elevation;
    }

    public static double frac(double d) 
    {
	long iPart;
	double fPart;
	// Get user input
	iPart = (long) d;
	fPart = d - iPart;
	return fPart;
    }
}
