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


public class ZipFileExistsAction implements Action
{
    public void execute() throws TopoSwapperException
    {
	System.out.println("Executing action of zip file exists");
	SrtmTopoFileReaderHelper tfrh = (SrtmTopoFileReaderHelper)LookupManager.getInstance().lookup("TopoFileReaderHelper");
	//	System.out.println("TopoFileReaderHelper" + tfrh);
	SrtmOneFileHolder srtmFileHolder = (SrtmOneFileHolder)LookupManager.getInstance().lookup("SrtmOneFileHolder");
	//	System.out.println("SRTMFileHolder "+srtmFileHolder);
	boolean voidStatus = tfrh.populateSrtmTileFromZipFile(srtmFileHolder.getSrtmFile().getName(),
							      srtmFileHolder.getSrtmZipFile());
    }
}
