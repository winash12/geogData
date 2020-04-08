package srtm1;

import toposwapper.Coordinate;
import toposwapper.LookupManager;
import toposwapper.TopoFileReaderHelper;
import toposwapper.TopoSwapperException;
import toposwapper.SrtmDownloadManager;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.FileChannel;
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SrtmTopoFileReaderHelper extends TopoFileReaderHelper
{
    private Properties params;
    private static final int VOID_VAL = -32768;
    
    public  SrtmTopoFileReaderHelper(Properties params) 
    {
	super(params);
	this.params = params;
    }


    public boolean  populateSrtmTile(File fileInput) throws TopoSwapperException

    {
	boolean voidStatus = false;
	try
	    (
		FileChannel fileInputChannel = new FileInputStream(fileInput).getChannel();
	     )
		{
		// move this File to extracted directory
		    ByteBuffer bb = populateByteBuffer(fileInputChannel);
		    //Inject Dependency
		    SrtmOneTile tile = (SrtmOneTile)LookupManager.getObject();
		    voidStatus = setSrtmData(tile,bb);
		    //
		    moveFile(fileInput);
		}
	catch (Exception e)
	    {
		e.printStackTrace();
		throw new TopoSwapperException(e.getMessage());
	    }
	return voidStatus;
    }

    private ByteBuffer populateByteBuffer(FileChannel channel) throws IOException
    {
	ByteBuffer bb  = ByteBuffer.allocateDirect((int)channel.size());
	while (bb.hasRemaining())
	    channel.read(bb);
	bb.flip();
	return bb;
    }
    /*
    ** For both versions
    */
    public boolean populateSrtmTileFromZipFile(String fname,
					       File srtmZipFile) 
	throws TopoSwapperException
    {
	InputStream in = null;
	InputStream in2 = null;
	ByteBuffer bb;
	ReadableByteChannel rbc = null;
	ZipFile zf = null;
	boolean voidStatus = false;
	try
	    {
		zf = new ZipFile(srtmZipFile);
		ZipEntry entry  = zf.getEntry(fname);
		in = zf.getInputStream(entry);
		File outputFile = writeFileToDisk(fname,
						  in,
						  srtmZipFile);
		//Create a new InputStream to read data into memory
		in2 = zf.getInputStream(entry);

		//Create a readable byte channel from zip input stream
		rbc =  Channels.newChannel(in2);

		//Allocate a byte buffer
		bb = ByteBuffer.allocate((int)entry.getSize());
		// Read in the data into bytebuffer via readablebytechannel
		int len =0 ;
		while (bb.hasRemaining())
		    rbc.read(bb);
		bb.flip();
		//Inject Dependency
		SrtmOneTile tile = (SrtmOneTile)LookupManager.getObject();
		voidStatus = setSrtmData(tile,bb);
		moveFile(outputFile);
	    }
	catch (Exception ioe)
	    {
		ioe.printStackTrace();
		throw new TopoSwapperException(ioe.getMessage());
	    }
	finally
	    {
		close(in);
		close(in2);
		close(rbc);
		close(zf);
	    }
	return voidStatus;
    }


    private boolean setSrtmData(SrtmOneTile tile,ByteBuffer bb)
    {
	ShortBuffer sb = bb.order(ByteOrder.BIG_ENDIAN).asShortBuffer();
	int size = (int)bb.capacity()/2;
	for (int i =0 ; i <= size;i++)
	    {
		short value = sb.get();
		if (value == VOID_VAL)
		    {
			System.out.println("The tile with void latitude is " + tile.getCoordinate().getLatitude());
			System.out.println("The tile with void longitude is " + tile.getCoordinate().getLongitude());
			return true;
		    }
		else
		    {
			tile.addElevation(value);
			//				System.out.println("The value of data is " + value);
		    }
	    }
	
	return false;
    }


    private File writeFileToDisk(String fname,
				 InputStream in,
				 File srtmZipFile) throws IOException
    {
	OutputStream out = null;
	File outputFile = null;
	try
	    {
	       	outputFile = new File(srtmZipFile.getParent(),
				      fname);
		out = new FileOutputStream(outputFile);
		int len;
		byte[] bytes = new byte[1024];
		while ((len = in.read(bytes)) > 0)
		    {
			out.write(bytes,0,len);
		    }
	    }
	catch (Exception ioe)
	    {
		ioe.printStackTrace();
		throw ioe;
	    }
	finally 
	    {
		try
		    {
			out.close();
		    }
		catch (Exception e)
		    {
		    }
	    }		
	return outputFile;
    }


    /*
     * The UNIX mv file replacement
     */
    public void moveFile(File srcFile) throws IOException
    {
	try
	    {
		File destDir = new File(params.getProperty("srtm_output_dir"));
		Path src = srcFile.toPath();
		System.out.println("The path is " + src.toString());
		Path dest = destDir.toPath();
		System.out.println("The path is " + dest.toString());
		Path target = Files.move(src,
					 dest.resolve(src.getFileName()),
					 StandardCopyOption.ATOMIC_MOVE);
	    }
	catch (Exception e )
	    {
		e.printStackTrace();
		throw new IOException();
	    }
	
    }


    public void createBlankSrtmTile() throws TopoSwapperException
    {
	//Inject Dependency
	SrtmOneTile tile = (SrtmOneTile)LookupManager.getObject();
	//
	int size = 3601*3601;
	size *= 2;
	byte[] bytes = new byte[size];
	ByteBuffer.allocate(size);
	ByteBuffer bb = ByteBuffer.wrap(bytes);
	ShortBuffer sb=null;
	String oceanicRegion = params.getProperty("OCEANIC_REGION");
	String path = params.getProperty("srtm_dir");
	String oceanicPath = path+"/"+oceanicRegion+"/"+ getFileName(tile.getCoordinate());
	try
	    {
		FileOutputStream fos = new FileOutputStream(oceanicPath);
		sb = bb.order(ByteOrder.BIG_ENDIAN).asShortBuffer();
		short[][] data = new short[3601][3601];
		for (int i=0;i<3600;i++)
		    {
			for (int j=0;j<3600;j++)
			    {
				data[i][j] = sb.get();
				fos.write(data[i][j]);
				fos.write(data[i][j]);
			    }
		    }
		moveFile(new File(oceanicPath));
	    }
	catch (Exception e)
	    {
		e.printStackTrace();
		throw new TopoSwapperException(e.getMessage());
	    }
    }

    void createSrtmFileHolder()
    {
	//Inject Dependency
	SrtmOneTile tile = (SrtmOneTile)LookupManager.getObject();
        File srtmFile = null;
        File srtmZipFile = null;
	String path = params.getProperty("srtm_dir");
        String fname = getFileName(tile.getCoordinate());
	if (!path.equals("")) 
	    {
		srtmFile = new File(path + "/" + fname);
		srtmZipFile = new File(path + "/" +  fname + ".zip");
	    } 
	else 
	    {
		srtmFile = new File(fname);
		srtmZipFile = new File(fname + ".zip");
	    }

	SrtmOneFileHolder srtmFileHolder = new SrtmOneFileHolder();
	srtmFileHolder.setSrtmFile(srtmFile);
	srtmFileHolder.setSrtmZipFile(srtmZipFile);
	LookupManager.getInstance().add("SrtmOneFileHolder",srtmFileHolder);
    }

    private String getFileName(Coordinate coord) 
    {
	int lat = coord.getLatitude();
	int lon = coord.getLongitude();
        String dirlat = "N";
        if (lat < 0) 
	    {
		dirlat = "S";
	    }
        String dirlon = "E";
        if (lon < 0) 
	    {
		dirlon = "W";
	    }
        String st = String.valueOf(Math.abs(lat));
        while (st.length() < 2) 
	    {
		st = "0" + st;
	    }
        String fname = dirlat + st;
        st = String.valueOf(Math.abs(lon));
        while (st.length() < 3) 
	    {
		st = "0" + st;
	    }
        fname = fname + dirlon + st + ".SRTMGL1" +  ".hgt";
	System.out.println("The value of file name is " + fname);
	return fname;
    }


    private static void close(Closeable c)
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
