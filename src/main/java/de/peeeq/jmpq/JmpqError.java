package de.peeeq.jmpq;

import java.io.IOException;

public class JmpqError extends Exception {
	private static final long serialVersionUID = -8940862879326571591L;

	final static int ERROR_SUCCESS = 0;
	// inlined linux constants, might be different on windows:
	final static int ERROR_FILE_NOT_FOUND = 2;// ENOENT;
	final static int ERROR_ACCESS_DENIED = 1;// EPERM;
	final static int ERROR_INVALID_HANDLE = 9;// EBADF;
	final static int ERROR_NOT_ENOUGH_MEMORY = 12;// ENOMEM;
	final static int ERROR_NOT_SUPPORTED = 95;// ENOTSUP;
	final static int ERROR_INVALID_PARAMETER = 22;// EINVAL;
	final static int ERROR_DISK_FULL = 28;// ENOSPC;
	final static int ERROR_ALREADY_EXISTS = 17;// EEXIST;
	final static int ERROR_INSUFFICIENT_BUFFER = 105;// ENOBUFS;
	final static int ERROR_BAD_FORMAT = 1000; // No such error code under Linux
	final static int ERROR_NO_MORE_FILES = 1001; // No such error code under Linux
	final static int ERROR_HANDLE_EOF = 1002; // No such error code under Linux
	final static int ERROR_CAN_NOT_COMPLETE = 1003; // No such error code under
	final static int ERROR_FILE_CORRUPT = 1004; // No such error code under Linux
	
	public JmpqError(String msg, int errNr) {
		super(msg + " " + convertError(errNr));
	}
	
	public JmpqError(Exception e) {
		super(e);
	}

	public JmpqError(String msg) {
		super(msg);
	}

	private static String convertError(int err) {

		switch (err) {
		case ERROR_SUCCESS:
			return "success";
		case ERROR_FILE_NOT_FOUND:
			return "file not found";
		case ERROR_ACCESS_DENIED:
			return "access denied";
		case ERROR_INVALID_HANDLE:
			return "invalid handle";
		case ERROR_NOT_ENOUGH_MEMORY:
			return "not enough memory";
		case ERROR_NOT_SUPPORTED:
			return "not supported";
		case ERROR_INVALID_PARAMETER:
			return "invalid parameter";
		case ERROR_DISK_FULL:
			return "disk full";
		case ERROR_ALREADY_EXISTS:
			return "already exists";
		case ERROR_INSUFFICIENT_BUFFER:
			return "insufficient buffer";
		case ERROR_BAD_FORMAT:
			return "bad format";
		case ERROR_NO_MORE_FILES:
			return " no more files";
		case ERROR_HANDLE_EOF:
			return "handle end of file";
		case ERROR_CAN_NOT_COMPLETE:
			return "can not complete";
		case ERROR_FILE_CORRUPT:
			return "file corrupt";
		default:
			return "unknown error: " + err;
		}
	}
	
}
