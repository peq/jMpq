package de.peeeq.jmpq;

import java.io.File;
import java.io.IOException;

public class Main {

	private final String[] args;
	private int argsRead = 0;

	public Main(String[] args) {
		this.args = args;

		try {
			String command = readArg();
			File mpq = new File(readArg());
			
			switch (command) {
			case "extract":
				extract(mpq, readArg(), new File(readArg()));
				break;
			case "add":
				add(mpq, new File(readArg()), readArg());
				break;
			case "delete":
				delete(mpq, readArg());
				break;
			case "compact":
				compact(mpq);
				break;
			default:
				System.out.println("Unknown command: " + command);
				System.exit(2);
			}
		} catch (JmpqError e) {
			throw new RuntimeException(e);
		}
	}

	private void compact(File mpq) throws JmpqError {
		try (JmpqEditor editor = new JmpqEditor(mpq)) {
			editor.compact();
		}
	}

	private void delete(File mpq, String fileName) throws JmpqError {
		try (JmpqEditor editor = new JmpqEditor(mpq)) {
			editor.delete(fileName);
		}
	}

	private void add(File mpq, File fileToInject, String nameInMpq) throws JmpqError {
		try (JmpqEditor editor = new JmpqEditor(mpq)) {
			editor.injectFile(fileToInject, nameInMpq);
		}
	}

	private void extract(File mpq, String fileToExtract, File extractTo) throws JmpqError {
		try (JmpqEditor editor = new JmpqEditor(mpq)) {
			editor.extractFile(fileToExtract, extractTo);
		}
	}

	private String readArg() {
		if (argsRead >= args.length) {
			System.out.println("not enough parameters");
			System.exit(1);
		}
		return args[argsRead++];
	}

	public static void main(String[] args) throws IOException {
		new Main(args);
	}


}
