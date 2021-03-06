package srtm1.rules;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;


import srtm1.SrtmOneFileHolder;
import srtm1.SrtmTopoFileReaderHelper;
import srtm1.SrtmOneTile;
import srtm1.SrtmOneDownloadManager;

import toposwapper.LookupManager;
import toposwapper.TopoSwapperException;

public class ZipFileDoesNotExistAction implements Action
{
    public void execute() throws TopoSwapperException
    {
	System.out.println("Downloading and reading One Arc Second");
	SrtmOneFileHolder srtmFileHolder = (SrtmOneFileHolder)LookupManager.getInstance().lookup("SrtmOneFileHolder");
	SrtmOneDownloadManager.getInstance().downloadZipFile(srtmFileHolder.getSrtmZipFile());
	SrtmTopoFileReaderHelper tfrh = (SrtmTopoFileReaderHelper)LookupManager.getInstance().lookup("SrtmTopoFileReaderHelper");
	boolean voidStatus = tfrh.
	    populateSrtmTileFromZipFile(srtmFileHolder.getSrtmFile().getName(),
					srtmFileHolder.getSrtmZipFile());
	System.out.println("Finished downloading One Arc Second");
    }
}
