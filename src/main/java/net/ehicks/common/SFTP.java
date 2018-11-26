package net.ehicks.common;

import com.jcraft.jsch.*;
import org.slf4j.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Vector;

import static net.ehicks.common.Common.createTemporaryFile;

public class SFTP
{
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SFTP.class);

    public static File getSftpFile(String host, String username, String password, String directory, String fileName)
    {
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");

        try
        {
            JSch ssh = new JSch();
            Session session = ssh.getSession(username, host, 22);
            session.setConfig(config);
            session.setPassword(password);
            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();

            ChannelSftp sftp = (ChannelSftp) channel;

            if (directory != null && directory.length() > 0) sftp.cd(directory);

            Vector files = sftp.ls("*");
            log.info("Found {} files in dir {}", files.size(), directory);

            ChannelSftp.LsEntry remoteFile = null;
            for (Object possibleFile : files)
            {
                if (!(possibleFile instanceof ChannelSftp.LsEntry))
                    continue;

                ChannelSftp.LsEntry file = (ChannelSftp.LsEntry) possibleFile;

                if (file.getAttrs().isDir())
                    continue;

                if (file.getFilename().equals(fileName))
                {
                    remoteFile = file;
                    break;
                }
            }

            if (remoteFile != null)
            {
                log.info("Reading file : {}", remoteFile.getFilename());
                BufferedInputStream bis = new BufferedInputStream(sftp.get(remoteFile.getFilename()));
                DataInputStream dis = new DataInputStream(bis);
                String tempFile;

                try (ByteArrayOutputStream buffer = new ByteArrayOutputStream())
                {
                    int nRead;
                    byte[] data = new byte[16384];

                    while ((nRead = dis.read(data, 0, data.length)) != -1)
                    {
                        buffer.write(data, 0, nRead);
                    }

                    buffer.flush();

                    dis.close();
                    channel.disconnect();
                    session.disconnect();

                    tempFile = createTemporaryFile("temp");
                    Files.write(Paths.get(tempFile), buffer.toByteArray());
                }

                return new File(tempFile);
            }

            channel.disconnect();
            session.disconnect();
        }
        catch (IOException | JSchException | SftpException e)
        {
            log.error(e.getMessage(), e);
        }

        return null;
    }
}
