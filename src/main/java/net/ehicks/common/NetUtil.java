package net.ehicks.common;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class NetUtil
{
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(NetUtil.class);

    public static FTPClient prepareFtpClient(String host, String login, String dir) throws IOException
    {
        FTPClient ftp = new FTPClient();
        FTPClientConfig config = new FTPClientConfig();
        ftp.configure(config);
        try
        {
            int reply;
            ftp.connect(host);
            log.info("Connected to " + host + ".");
            log.info(ftp.getReplyString());

            // After connection attempt, you should check the reply code to verify success.
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply))
            {
                ftp.disconnect();
                log.error("FTP server refused connection.");
                System.exit(1);
            }

            ftp.login(login, "");
            // transfer files
            ftp.enterLocalPassiveMode();
            ftp.changeWorkingDirectory(dir);

            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.setFileTransferMode(FTP.COMPRESSED_TRANSFER_MODE);

            return ftp;
        }
        catch (IOException e)
        {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    public static byte[] getBytesFromUrlConnection(URL url) throws IOException
    {
        URLConnection uc = url.openConnection();
        int len = uc.getContentLength();
        try (InputStream is = new BufferedInputStream(uc.getInputStream()))
        {
            byte[] data = new byte[len];
            int offset = 0;
            while (offset < len)
            {
                int read = is.read(data, offset, data.length - offset);
                if (read < 0)
                {
                    break;
                }
                offset += read;
            }
            if (offset < len)
            {
                throw new IOException(String.format("Read %d bytes; expected %d", offset, len));
            }
            return data;
        }
    }

    public static void writeFileToResponse(HttpServletResponse response, File file, String displayFilename, boolean deleteFile) throws IOException
    {
        response.setHeader("Content-Length", "" + (file.length()));
        response.setContentType(getMimeType(displayFilename));
        response.addHeader("Content-Disposition", "attachment; filename=" + displayFilename);

        // Write the file back to the client
        byte[] buffer = new byte[32_768];

        try (FileInputStream inputStream = new FileInputStream(file);
             ServletOutputStream outputStream = response.getOutputStream())
        {
            for (int bytesRead = inputStream.read(buffer); bytesRead >= 0; bytesRead = inputStream.read(buffer))
                outputStream.write(buffer, 0, bytesRead);
        }

        if (deleteFile)
            file.delete();
    }

    public static String getMimeType(String filename)
    {
        if (filename == null || filename.length() < 4) return "";
        filename = filename.toLowerCase();
        String mimeType = "";

        if (filename.indexOf('.') >= 0)
        {
            String extension = filename.substring(filename.toLowerCase().lastIndexOf('.') + 1);

            if (extension.equals("ics"))        mimeType = "text/calendar";
            if (extension.equals("ppt"))        mimeType = "application/ms-powerpoint";
            if (extension.equals("pptx"))       mimeType = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            if (extension.equals("doc"))        mimeType = "application/msword";
            if (extension.equals("rtf"))        mimeType = "application/msword";
            if (extension.equals("docx"))       mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            if (extension.equals("dotx"))       mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.template";
            if (extension.equals("xls"))        mimeType = "application/vnd.ms-excel";
            if (extension.equals("xlsx"))       mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            if (extension.equals("xltx"))       mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.template";
            if (extension.equals("pdf"))        mimeType = "application/pdf";
            if (extension.equals("jpg"))        mimeType = "image/jpeg";
            if (extension.equals("png"))        mimeType = "image/png";
            if (extension.equals("gif"))        mimeType = "image/gif";
            if (extension.equals("txt"))        mimeType = "text/plain";
            if (extension.equals("log"))        mimeType = "text/plain";
            if (extension.equals("csv"))        mimeType = "text/csv";
            if (extension.equals("flipchart"))  mimeType = "application/Inspire flipchart";
            if (extension.equals("ink"))        mimeType = "application/x-intkey";
            if (extension.equals("nbk"))        mimeType = "application/x-smarttech-notebook";
            if (extension.equals("xbk"))        mimeType = "application/x-smarttech-notebook";
            if (extension.equals("notebook"))   mimeType = "application/x-smarttech-notebook";
            if (extension.equals("ef"))         mimeType = "image/x-ef";
        }
        return mimeType;
    }
}
