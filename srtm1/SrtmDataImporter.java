package srtm1;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import java.util.logging.Level;
import java.util.logging.Logger;

import srtm1.rules.Action;
import srtm1.rules.Condition;

import srtm1.rules.ZipFileDownloadCondition;
import srtm1.rules.ZipFileDownloadAction;


import toposwapper.Coordinate;
import toposwapper.LookupManager;
import toposwapper.TopoSwapperException;
/**
 *
 * 
 */
public class SrtmDataImporter 
{
    private Coordinate coord;
    private SrtmOneTile tile;

    public SrtmDataImporter() 
    {
    }


    public void download() throws TopoSwapperException
    {
	SrtmTopoFileReaderHelper tfrh = (SrtmTopoFileReaderHelper)LookupManager.getInstance().lookup("SrtmTopoFileReaderHelper");
	tfrh.createSrtmFileHolder();
	Collection<Map.Entry<Condition,Action>> steps = 
	    SrtmFileCheckOrder.getSrtmDownloadCheckOrder();
	for (Map.Entry<Condition,Action> node: steps)
	    {
		if(!node.getKey().isSatisfied())
		    continue;
		node.getValue().execute();
		return;
	    }
    }

    public boolean load() throws TopoSwapperException
    {
	System.out.println("Entering Data Importer");
	SrtmTopoFileReaderHelper tfrh = (SrtmTopoFileReaderHelper)LookupManager.getInstance().lookup("SrtmTopoFileReaderHelper");
	tfrh.createSrtmFileHolder();
	Collection<Map.Entry<Condition,Action>> steps = 
	    SrtmFileCheckOrder.getSrtmFileCheckOrder();
	// Chain of Responsibility + State + Strategy Design Pattern
	//Iterating over outer collection which contains a collection of lists
	for (Map.Entry<Condition,Action> node: steps)
	    {
		if(!node.getKey().isSatisfied())
		    continue;
		node.getValue().execute();
		return true;
	    }
	System.out.println("Will create oceanic tile");
	// Instance of Null Object Pattern
	tfrh.createBlankSrtmTile();
	return true;
    }
}
