package landcover;

import toposwapper.Coordinate;
import toposwapper.CoordinateComparator;
import toposwapper.LookupManager;
import toposwapper.TopoDirectoryLoader;
import toposwapper.TopoSwapperException;


import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;
import java.util.Properties;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class GLCDirectoryLoader extends TopoDirectoryLoader
{
    private Properties params;
    private List<GLCTile> geoTiffFiles = new ArrayList<GLCTile>();
    private Pattern p = Pattern.compile("^(n|s)([0-9]+)_([0-9]+)_([0-9]+)(lc)([0-9]+).tif");
    

    public GLCDirectoryLoader(Properties params) throws TopoSwapperException
    {
	super();
	this.params = params;
	createRequiredDirectories();
    }

    public void createRequiredDirectories() throws TopoSwapperException
    {
	boolean status = new File(params.getProperty("glc_output_dir")).mkdirs();
	if (status)
	    {
		return;
	    }
	else
	    {
		new TopoSwapperException("Unable to create directories");
	    }
    }

    
    public void extractGLCFilesFromZipFiles() throws TopoSwapperException
    {
	try
	    {
		File f = new File(params.getProperty("glc_dir"));
		FilenameFilter textFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
			    String lowercaseName = name.toLowerCase();
			    if (lowercaseName.endsWith(".zip")) {
				return true;
			    } else {
				return false;
			    }
			}
		    };
	
		File[] files = f.listFiles(textFilter);
		for (File file : files) {
		    extractAllFilesFromZipFile(file);
		}
	    }
	catch (Exception e)
	    {
		e.printStackTrace();
		throw new TopoSwapperException("I/O error");
	    }

    }

    protected void extractAllFilesFromZipFile(File zipFile) throws IOException {
	ZipFile zf = new ZipFile(zipFile);
	String prefix = params.getProperty("glc_extract_dir");
	try {
	    Enumeration<?> enu = zf.entries();
	    while (enu.hasMoreElements()) {
		ZipEntry zipEntry = (ZipEntry) enu.nextElement();
		String name = zipEntry.getName();
		long size = zipEntry.getSize();
		long compressedSize = zipEntry.getCompressedSize();
		System.out.printf(
				  "name: %-20s | size: %6d | compressed size: %6d\n",
				  name, size, compressedSize);

		File file = new File(prefix + "/landCoverData/" + name);
		if (name.endsWith("/")) {
		    file.mkdirs();
		    continue;
		}
		File parent = file.getParentFile();
		System.out.println(parent.getName());
		if (parent != null) {
		    parent.mkdirs();
		}
		InputStream is = zf.getInputStream(zipEntry);
		FileOutputStream fos = new FileOutputStream(file);
		byte[] bytes = new byte[1024];
		int length;
		while ((length = is.read(bytes)) >= 0) {
		    fos.write(bytes, 0, length);
		}
		is.close();
		fos.close();
	    }
	} catch (Exception ioe) {
	    throw ioe;
	} finally {
	    try {
		zf.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }

    public void sortGeoTiffFiles() throws TopoSwapperException
    {
	Comparator<GLCTile> cmp = Comparator.
	    comparing(GLCTile::getCoordinate,new CoordinateComparator());
	Collections.sort(geoTiffFiles,cmp);
	/*for (GLCTile glcTile : geoTiffFiles)
	    {
		System.out.println("The value of latitude is " + glcTile.getCoordinate().getLatitude());
		System.out.println("The value of longitude is " + glcTile.getCoordinate().getLongitude());
		System.out.println("The value of Coordinate is " + glcTile.getGeoTiffFileName());
				   
	    }*/
    }
    public List<GLCTile> getGLCTiles()
    {
	return geoTiffFiles;
    }

    public void collectGLCGeoTiffFiles() throws TopoSwapperException
    {
	String glcPath = params.getProperty("glc_dir");
	File landCoverList = new File(glcPath+"/landCoverData");
	String[] names = landCoverList.list();
	String[] tiffFile = new String[1];
	for (String name : names)
	    {
		File dirName =  new File(glcPath+"/landCoverData/"+name);
		if (dirName.isDirectory()){
		    tiffFile = dirName.list(new FilenameFilter(){
			    public boolean accept(File dir, String file){
				return file.endsWith(".tif");
			    }
			});

		    String fileName = tiffFile[0];
		    System.out.println("The value of tiffFile is " + fileName);
		    Matcher m = p.matcher(fileName);
		    int latitude =0;
		    int longitude =0;
		    if (m.matches())
			{
			    String firstValue = m.group(2);		    
			    longitude = Integer.parseInt(firstValue);
			    //Formula for converting UTM Zone to Longitude
			    //http://gis.stackexchange.com/questions/84218/
			    //get-approximate-lat-long-coordinate-for-utm-
			    //grid-zone
			    longitude = ((longitude -1)*6) - 177;
			    //System.out.println("The value of longitude is " + longitude);
			    String secondValue = m.group(3);
			    latitude = Integer.parseInt(secondValue);
			    //System.out.println("The value of latitude is " + latitude);
			}
		    Coordinate coordinate = new Coordinate(latitude,
							   longitude);
		    
		    GLCTile glcTile = new GLCTile(coordinate);
		    glcTile.setGeoTiffFileName(fileName);
		    glcTile.setDirectoryName(name);
		    geoTiffFiles.add(glcTile);
		}
	    }
    }
}
