package de.peeeq.jmpq;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;
import com.sun.jna.LastErrorException;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

public class JmpqEditor implements AutoCloseable {
	static StormLib stormLib = null;
	private final Pointer mpq;

	/** creates a new mpqeditor opening the given mpq file */
	public JmpqEditor(File mpqFile) throws JmpqError {
		//Native.setLastError(0);
		initStormlib();
		PointerByReference phMPQ = new PointerByReference();
		short flags = 0;
		short prio = 0;
		
		Native.setLastError(0);
		Native.setPreserveLastError(true);
		handleResult("openArchive " + mpqFile.getAbsolutePath(), stormLib.SFileOpenArchive(mpqFile.getAbsolutePath(), prio, flags, phMPQ));
		mpq = phMPQ.getValue();
	}



	private void handleResult(String phase, boolean r) throws JmpqError {
		if (!r) {
			throw new JmpqError("Error in " + phase + ": ", Native.getLastError());
		}
		Native.setLastError(0);
	}



	private void initStormlib() throws JmpqError {
		try {
			if (stormLib == null) {
				stormLib = (StormLib) Native.loadLibrary("Storm", StormLib.class);
			}
		} catch (Throwable e) {
			throw new JmpqError(e);
		}
	}
	
	
	public void close() throws JmpqError {
		handleResult("flushArchive", stormLib.SFileFlushArchive(mpq));
		handleResult("closeArchive", stormLib.SFileCloseArchive(mpq));
	}
	
	public void extractFile(String fileToExtract, File extractTo) throws JmpqError {
		handleResult("extractFile ", stormLib.SFileExtractFile(mpq, fileToExtract, extractTo.getAbsolutePath(), 0));
	}
	public void injectFile(File fileToInject, String nameInMpq) throws JmpqError {
		if (!fileToInject.exists()) {
			throw new JmpqError("File "  + fileToInject + " not found.");
		}
		
		int dwCompression = StormLib.MPQ_COMPRESSION_ZLIB;
		int dwFlags = StormLib.MPQ_FILE_REPLACEEXISTING + StormLib.MPQ_FILE_COMPRESS;
		if(!stormLib.SFileAddFileEx(mpq, fileToInject.getAbsolutePath(), nameInMpq, dwFlags, dwCompression, dwCompression)){
			//TODO double current size
			stormLib.SFileSetMaxFileCount(mpq, 256);
			handleResult("inject file", stormLib.SFileAddFileEx(mpq, fileToInject.getAbsolutePath(), nameInMpq, dwFlags, dwCompression, dwCompression));
		}
	}
	
	public void delete(String fileName) throws JmpqError {
		handleResult("delete file  ", stormLib.SFileRemoveFile(mpq, fileName, 0));
	}

	public void compact() throws JmpqError {
		handleResult("compact archive", stormLib.SFileCompactArchive(mpq, "", false));
	}

	
	
	// TODO public?
	public interface StormLib extends Library {

		boolean SFileOpenArchive(String szMpqName, int dwPriority, int dwFlags,
				PointerByReference phMPQ);

		boolean SFileExtractFile(Pointer hMpq, String szToExtract,
				String szExtracted, int dwSearchScope);

		boolean SFileCloseArchive(Pointer hMpq);

		boolean SFileFlushArchive(Pointer hMpq);
		
		boolean SFileCompactArchive(Pointer hMpq, String szListFile, boolean bReserved);

		boolean SFileRemoveFile(Pointer hMpq, String szFileName,
				int dwSearchScope);
		
		boolean SFileSetMaxFileCount(Pointer hMpq, int dwMaxFileCount);

		boolean SFileAddFileEx(Pointer hMpq, String szFileName,
				String szArchivedName, int dwFlags, int dwCompression,
				int dwCompressionNext);

		final int MPQ_FILE_IMPLODE = (0x00000100);;
		final int MPQ_FILE_COMPRESS = (0x00000200);
		final int MPQ_FILE_ENCRYPTED = (0x00010000);
		final int MPQ_FILE_FIX_KEY = (0x00020000);
		final int MPQ_FILE_DELETE_MARKER = (0x02000000);
		final int MPQ_FILE_SECTOR_CRC = (0x04000000);
		final int MPQ_FILE_SINGLE_UNIT = (0x01000000);
		final int MPQ_FILE_REPLACEEXISTING = (0x80000000);

		final int MPQ_COMPRESSION_HUFFMANN = (0x01);
		final int MPQ_COMPRESSION_ZLIB = (0x02);
		final int MPQ_COMPRESSION_PKWARE = (0x08);
		final int MPQ_COMPRESSION_BZIP2 = (0x10);
		final int MPQ_COMPRESSION_SPARSE = (0x20);
		final int MPQ_COMPRESSION_ADPCM_MONO = (0x40);
		final int MPQ_COMPRESSION_ADPCM_STEREO = (0x80);
		final int MPQ_COMPRESSION_LZMA = (0x12);

	}



	






	
	
	
}
