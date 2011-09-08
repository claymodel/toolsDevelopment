package com.wr.confluence.macro;


import com.atlassian.confluence.servlet.download.ExportDownload;
import com.atlassian.confluence.util.io.IOUtils;
import com.atlassian.core.util.PropertyUtils;
import com.atlassian.renderer.RenderContext;
import com.atlassian.renderer.v2.RenderMode;
import com.atlassian.renderer.v2.macro.BaseMacro;
import com.atlassian.renderer.v2.macro.MacroException;

import java.io.*;
import java.util.Map;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Vladimir Ruzhansky
 * Date: 15.03.11
 * Time: 15:45
 */

public class QsdeMacro extends BaseMacro {

    private String sdPath;
    private String pngPath;

    private File pngFile;

    public boolean isInline() {
        return false;
    }

    public boolean hasBody() {
        return true;
    }

    public RenderMode getBodyRenderMode() {
        return RenderMode.NO_RENDER;
    }


    private File createTempFile(String suffix) throws MacroException {
        File temp;
        try {
            temp = ExportDownload.createTempFile("qsde", suffix);
        } catch (IOException ioe) {
            throw new MacroException("Couldn't create sd temporary file", ioe);
        }
        return temp;
    }

    private String getCanonicalPath(File file) throws MacroException {
        String filePath;
        try {
            filePath = file.getCanonicalPath();
        } catch (IOException ioe) {
            throw new MacroException("Couldn't get canonical path", ioe);
        }
        return filePath;
    }

    private void writeBodyToSd(File file, String sdBody) throws MacroException {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(sdBody.getBytes("UTF-8"));
        } catch (IOException ioe) {
            throw new MacroException("Error writing sd to temp file", ioe);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ioe) {
                    throw new MacroException("Error closing sd temp outputStream", ioe);
                }
            }
        }

    }

    public String execute(Map map, String body, RenderContext renderContext) throws MacroException {

        File sdFile = createTempFile(".sd");
        pngFile = createTempFile(".png");

        sdPath = getCanonicalPath(sdFile);
        writeBodyToSd(sdFile, body);
        pngPath = getCanonicalPath(pngFile);

        return runQsde();
    }

    private String runQsde() throws MacroException {
        Properties prop = PropertyUtils.getProperties("qsde.properties", QsdeMacro.class);
        String qsdeJar = prop.getProperty("qsde.jar");

        String[] args = new String[6];

        args[0] = "java";
        args[1] = "-jar";
        args[2] = qsdeJar;
        args[3] = "-tpng";
        args[4] = "-o" + pngPath;
        args[5] = sdPath;

        Process p = null;
        int exitCode = -1;
        String error = "";
        String output = "";

        try {
            p = Runtime.getRuntime().exec(args);
            // capture error and stdout of QSDE subprocess
            InputStream stderr = p.getErrorStream();
            InputStream stdin = p.getInputStream();
            String line;

            BufferedReader readerErr = new BufferedReader(new InputStreamReader(stderr));
            while ((line = readerErr.readLine()) != null)
                error += line;

            BufferedReader readerIn = new BufferedReader(new InputStreamReader(stdin));
            while ((line = readerIn.readLine()) != null)
                output += line;

            try {
                exitCode = p.waitFor();
            } catch (InterruptedException ie) {
                //do nothing
            }

        } catch (IOException ioe) {
            throw new MacroException("Unable to run qsde", ioe);
        } finally {
            if (p != null) {
                destroyProcess(p);
            }
        }
        return (exitCode == 0) ? "<img src='" + ExportDownload.getUrl(pngFile, "image/png") + "'/>" : "<div class=errorBox>\n" + error + "\n</div>";
    }

    private void destroyProcess(Process p) {
        if (p != null) {
            IOUtils.close(p.getInputStream());
            IOUtils.close(p.getOutputStream());
            IOUtils.close(p.getErrorStream());
            p.destroy();
        }
    }
}
