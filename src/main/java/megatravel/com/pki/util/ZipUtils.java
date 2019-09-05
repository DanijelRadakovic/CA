package megatravel.com.pki.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    private List <String> fileList;
    private String sourceFolder;

    public ZipUtils() {
        this.fileList = new ArrayList<>();
        this.sourceFolder = null;
    }

    public void zipIt(String zipFile) {
        byte[] buffer = new byte[1024];
        String source = new File(sourceFolder).getName();
        FileOutputStream fos;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);

            FileInputStream in = null;

            for (String file: this.fileList) {
                ZipEntry ze = new ZipEntry(source + File.separator + file);
                zos.putNextEntry(ze);
                try {
                    in = new FileInputStream(sourceFolder + File.separator + file);
                    int len;
                    while ((len = in .read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                } finally {
                    if (in != null) in.close();
                }
            }
            zos.closeEntry();

        } catch (IOException ignored) {
        } finally {
            try {
                if (zos != null) zos.close();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * Traverse a directory and get all files,
     * and add the file into fileList
     * @param node file or directory
     */
    public void generateFileList(File node) {
        if (sourceFolder == null) sourceFolder = node.toString();

        // add file only
        if (node.isFile()) {
            fileList.add(generateZipEntry(node.toString()));
        }

        if (node.isDirectory()) {
            String[] subNote = node.list();
            if (subNote != null) {
                for (String filename: subNote) {
                    generateFileList(new File(node, filename));
                }
            }
        }
    }

    /**
     * Format the file path for zip
     * @param file file path
     * @return Formatted file path
     */
    private String generateZipEntry(String file){
        return file.substring(sourceFolder.length()+1);
    }
}