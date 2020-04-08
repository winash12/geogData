package srtm1.rules;

import srtm1.SrtmOneFileHolder;
import toposwapper.LookupManager;

public class SrtmHgtFileExistsCondition implements Condition
{
    @Override
    public boolean isSatisfied()
    {
	SrtmOneFileHolder srtmFileHolder = (SrtmOneFileHolder)LookupManager.getInstance().lookup("SrtmOneFileHolder");	
	return srtmFileHolder.getSrtmFile().exists() && srtmFileHolder.getSrtmFile().canRead();
    }
}
