package srtm1.rules;

import srtm1.SrtmOneFileHolder;

import toposwapper.LookupManager;


public class ZipFileExistsCondition implements Condition
{

    public boolean isSatisfied()
    {
	SrtmOneFileHolder srtmFileHolder = (SrtmOneFileHolder)LookupManager.getInstance().lookup("SrtmOneFileHolder");
	return srtmFileHolder.getSrtmZipFile().exists() && srtmFileHolder.getSrtmZipFile().canRead();
    }
}
