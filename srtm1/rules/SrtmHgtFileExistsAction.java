package srtm1.rules;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;


import srtm1.SrtmOneFileHolder;
import srtm1.SrtmTopoFileReaderHelper;
import srtm1.SrtmOneTile;

import toposwapper.LookupManager;
import toposwapper.SrtmFileCheckOrder;
import toposwapper.TopoSwapperException;

public class SrtmHgtFileExistsAction implements Action
{
    public void execute() 
	throws TopoSwapperException
    {
	SrtmTopoFileReaderHelper tfrh = (SrtmTopoFileReaderHelper)LookupManager.getInstance().lookup("SrtmTopoFileReaderHelper");
	SrtmOneFileHolder srtmFileHolder = (SrtmOneFileHolder)LookupManager.getInstance().lookup("SrtmOneFileHolder");
	boolean voidStatus = tfrh.populateSrtmTile(srtmFileHolder.getSrtmFile());
    }
}

