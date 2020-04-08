package srtm1.rules;

import srtm1.SrtmOneFileHolder;
import srtm1.SrtmOneDownloadManager;
import toposwapper.LookupManager;

public class ZipFileDownloadCondition implements Condition
{
    @Override
    public boolean isSatisfied()
    {
	SrtmOneFileHolder srtmFileHolder = (SrtmOneFileHolder)LookupManager.getInstance().lookup("SrtmOneFileHolder");	
	String tilePresent = SrtmOneDownloadManager.getInstance().findTile(srtmFileHolder.getSrtmFile().getName());
	//	System.out.println("The value of tilePresent is " + tilePresent);
	if (tilePresent == null)
	    {
		return false;
	    }
	return true;
    }
}
