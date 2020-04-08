package srtm1;

import java.io.File;

import java.util.Collection;
import java.util.Arrays;

import java.util.ArrayList;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import srtm1.rules.Action;
import srtm1.rules.Condition;

import srtm1.rules.SrtmHgtFileExistsAction;
import srtm1.rules.ZipFileExistsAction;
import srtm1.rules.ZipFileDoesNotExistAction;
import srtm1.rules.SrtmHgtFileExistsCondition;
import srtm1.rules.ZipFileExistsCondition;
import srtm1.rules.ZipFileDoesNotExistCondition;

import srtm1.rules.ZipFileDownloadCondition;
import srtm1.rules.ZipFileDownloadAction;

public class SrtmFileCheckOrder
{

    public SrtmFileCheckOrder()
    {
    }

    public static Collection<Map.Entry<Condition,Action>> getSrtmDownloadCheckOrder()
    {
	Collection<Map.Entry<Condition,Action>> steps = Arrays.
	    asList(
		   (new AbstractMap.SimpleImmutableEntry<Condition,Action>
		    (
		     new ZipFileDownloadCondition(),
		     new ZipFileDownloadAction()
		     )
		    )
		   );
	return steps;
    }

    public static Collection<Map.Entry<Condition,Action>> getSrtmFileCheckOrder()
    {
	Collection<Map.Entry<Condition,Action>> steps = Arrays.
	    asList(
		   (new AbstractMap.SimpleImmutableEntry<Condition,Action>
		    (
		     new SrtmHgtFileExistsCondition(),
		     new SrtmHgtFileExistsAction()
		     )
		    )
		    ,
		    (
		     new AbstractMap.SimpleImmutableEntry<Condition,Action>
		     (
		      new ZipFileExistsCondition(),
		      new ZipFileExistsAction()
		      )
		     )
		    ,
		   (new AbstractMap.SimpleImmutableEntry<Condition,Action>
		     (
		      new ZipFileDoesNotExistCondition(),
		      new ZipFileDoesNotExistAction()
		     )
		     )
		   );
		   
	return steps;
    }
}
