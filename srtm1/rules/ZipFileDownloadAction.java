package srtm1.rules;


import java.io.File;
import java.io.IOException;

import java.util.zip.ZipFile;

import srtm1.SrtmOneFileHolder;
import srtm1.SrtmOneDownloadManager;

import toposwapper.LookupManager;

import toposwapper.TopoSwapperException;



public class ZipFileDownloadAction extends ZipFileDoesNotExistAction
{
    public void execute() throws TopoSwapperException
    {
	System.out.println("Downloading and reading One Arc Second");
	SrtmOneFileHolder srtmFileHolder = (SrtmOneFileHolder)LookupManager.getInstance().lookup("SrtmOneFileHolder");
	File zipFile = srtmFileHolder.getSrtmZipFile();
	boolean isCorrupted = false;
	if (zipFile.exists())
	    {
		ZipFile z = null;
		try
		    {
			z = new ZipFile(zipFile);
		    }
		catch (Exception e)
		    {
			System.out.println("File corrupted");
			isCorrupted = true;
			e.printStackTrace();
		    }
		finally 
		    {
			try
			    {
				if (z != null)
				    z.close();
			    }
			catch (Exception e)
			    {
				e.printStackTrace();
			    }
		    }
	if (isCorrupted)
	    SrtmOneDownloadManager.getInstance().downloadZipFile(zipFile);
	System.out.println("Finished downloading One Arc Second");
	return;
	    }
	SrtmOneDownloadManager.getInstance().downloadZipFile(zipFile);
	System.out.println("Finished downloading One Arc Second");
	return;
    }
}
