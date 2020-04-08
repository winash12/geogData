package srtm1;

import java.io.File;

public class SrtmOneFileHolder
{
    private File srtmFile;
    private File srtmZipFile;
    public SrtmOneFileHolder()
    {
    }
    
    public void setSrtmFile(File srtmFile)
    {
	this.srtmFile = srtmFile;
    }
    public void setSrtmZipFile(File srtmZipFile)
    {
	this.srtmZipFile = srtmZipFile;
    }
    public File getSrtmFile()
    {
	return srtmFile;
    }
    public File getSrtmZipFile()
    {
	return srtmZipFile;
    }
}
