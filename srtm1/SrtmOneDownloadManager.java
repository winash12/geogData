package srtm1;

import toposwapper.TopoSwapperException;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.File;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Properties;
import java.util.Scanner;

public class SrtmOneDownloadManager
{
    private static String srtmPath;
    private static String url;
    private  static final Map<String, String> tileMap = new HashMap<String,String>();        
    private static SrtmOneDownloadManager instance;

    private SrtmOneDownloadManager()
    {
    }

    public static synchronized SrtmOneDownloadManager getInstance()
    {
	if (instance == null)
	    {
		instance = new SrtmOneDownloadManager();
	    }
	return instance;
    }


    public  void setParameters(Properties parameters)
    {
	System.out.println("Setting parameters for Srtm Download Manager");
	setPath(parameters.getProperty("srtm_dir"));
        setUrl(parameters.getProperty("srtm1_url"));
    }

    public void setPath(String path)
    {
	this.srtmPath = path;
    }

    public String getPath()
    {
	return srtmPath;
    }
    public void setUrl(String url)
    {
	this.url = url;
    }
    public String getUrl()
    {
	return url;
    }

    public  String findTile(String srtmFileName)
    {
	String name = srtmFileName.replace(".SRTMGL1.hgt", "");
        if (tileMap.containsKey(name)) 
	    {
		return tileMap.get(name);
	    }
	return null;
    }

    /*
     * Returns index file
     */
    public  void loadIndexFile() 
    {
	String srtmPath = getPath();
	String url = getUrl();
        if (tileMap.isEmpty()) 
	    {
		System.err.println("Downloading SRTM map data.");
		String indexPath = "";
		if (!srtmPath.equals("")) 
		    {
			indexPath = srtmPath;
			System.out.println("The indexPath is " + indexPath);
		    }
		File indexDir = new File(indexPath);
		if (!indexDir.exists()) {
		    System.out.println("Are we making dirs");
		    indexDir.mkdirs();
		}
		indexPath += ".index.html";
		System.out.println("The index path is " + indexPath);
		File indexFile = new File(indexPath);
		if (!indexFile.exists()) {
		    System.out.println("Does the index file exist");
		    try
			{
				downloadIndexFile(indexFile);
				// download error, try again with the next attempt
			}
		    catch (TopoSwapperException tsde)
			{
			    tileMap.clear();
			}
		    
		}
		try 
		    {
			Scanner scanner = new Scanner(indexFile);
			while (scanner.hasNext()) {
			    String line = scanner.next();
			    if (line.contains("href=\"")) {
				int index = line.indexOf(".SRTMGL1.hgt.zip") - 7;
				if (index >= 0) 
				    {
					String srtm = line.substring(index, index + 7);
					tileMap.put(srtm, "Y");
				    } 
			    }
			}
			scanner.close();
		    } 
		catch (FileNotFoundException ex) 
		    {
			Logger.getLogger(SrtmOneDownloadManager.class.getName()).log(Level.SEVERE, null, ex);
		    }
	    }
            System.out.println("SRTM map filled in with " + tileMap.size() + " entries.");
	    for (Map.Entry<String,String> entry : tileMap.entrySet())
		{
		    String key = entry.getKey();
		    System.out.println("The value of tile key is " +  key);
		}
    }

    public void downloadIndexFile(File indexFile) throws TopoSwapperException
    {
	try
	    {
		URL url = new URL(getUrl());
		Files.copy(url.openStream(),indexFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
	    }
	catch (MalformedURLException ex) {
            Logger.getLogger(SrtmOneDownloadManager.class.getName()).log(Level.SEVERE, "", ex);
	    throw new TopoSwapperException(ex.getMessage());
        }
	catch (IOException ioe)
	    {
		Logger.getLogger(SrtmOneDownloadManager.class.getName()).log(Level.SEVERE, "", ioe);
		throw new TopoSwapperException(ioe.getMessage());
	    }
    }
				     

    public void downloadZipFile(File output) throws TopoSwapperException
    {
	String url = null;
	url = getUrl()+"/"+output.getName();
	downloadZipFile(url,output);
    }

    public  void downloadZipFile(String url,File output) 
	throws TopoSwapperException
    {
        URL url1 = null;
	URLConnection conn = null;
        InputStream inputs = null;
        FileOutputStream out = null;
        try 
	    {
		url1 = new URL(url);
		conn = url1.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(false);
		conn.setRequestProperty("file-name", output.getName());
		conn.setRequestProperty("content-type","application/zip");
	    } 
	catch (MalformedURLException ex) {
            Logger.getLogger(SrtmOneDownloadManager.class.getName()).log(Level.SEVERE, "", ex);
	    throw new TopoSwapperException(ex.getMessage());
        }
	catch (IOException ioe)
	    {
		Logger.getLogger(SrtmOneDownloadManager.class.getName()).log(Level.SEVERE, "", ioe);
		throw new TopoSwapperException(ioe.getMessage());
	    }

        try 
	    {
		inputs = conn.getInputStream();
		out = new FileOutputStream(output);
		byte[] b = new byte[1024];
		int count;
		while ((count = inputs.read(b)) > -1)
		    {
			out.write(b,0,count);
		    }
		out.flush();
		inputs.close();
		out.close();

	    } 
	catch (FileNotFoundException ex) 
	    {
		throw new TopoSwapperException(ex.getMessage());
	    } 
	catch (IOException ex) 
	    {
		Logger.getLogger(SrtmOneDownloadManager.class.getName()).log(Level.SEVERE, "", ex);
		throw new TopoSwapperException(ex.getMessage());
	    }
	finally
	    {
		close(inputs);
		close(out);
	    }
    }
    public static void close(Closeable c)
    {
	if (c == null) return;
	try
	    {
		c.close();
	    }
	catch (IOException e)
	    {
		e.printStackTrace();
	    }
    }
}

