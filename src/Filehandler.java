//package IGotTheKeys;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;



public class Filehandler {
	private SystemHandler sysHandler;
	
	public Filehandler() {
		this.sysHandler = new SystemHandler();
	}
	
	public void moveFile(Path quelle, String ZielPfad, String FileName) {
		File f = new File(ZielPfad);
		Path ZielFile = Paths.get(ZielPfad + this.sysHandler.getFolderSeparator() + FileName);
		
        if (!f.exists()) {
        	f.mkdir();
        }
        if(!Files.exists(ZielFile)) {
			try {
				Files.move(quelle, ZielFile);
				System.out.println(ZielFile.getFileName() + " verschoben nach " + ZielPfad);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
   	}
}

