package remotestorage;

import com.jcraft.jsch.*;
import constants.FileConstants;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RemoteServerConnection {
    String hostName;
    String userName;
    Path privateKey;
    int port;
    public RemoteServerConnection(){
        hostName = "34.67.20.154";
        userName = "sravanireddy30";
//        System.out.println("Hel0o");
        //privateKey = Paths.get("C://Users//Home//.ssh//id_rsa");
        privateKey = Paths.get("C:/Users/Home/.ssh/id_rsa");
        port = 22;
    }
    public void pushFileToRemote(String query)
    {
        String que ="select  from query";
        String createCommand = "cat " + FileConstants.GCP_DB_Directory + "/QueryRequestResponse/requestFile.txt";
        String command = "echo \"" + query + " \" >" + FileConstants.GCP_DB_Directory + "/QueryRequestResponse/requestFile.txt";

        Session session;
        JSch jSch = new JSch();
        try {
            jSch.addIdentity(privateKey.toString());
            session = jSch.getSession(userName,hostName,port);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking","no");
            session.setConfig(config);
            session.connect();
            Channel ch = session.openChannel("exec");
            ((ChannelExec) ch).setCommand(createCommand);
            ((ChannelExec) ch).setCommand(command);
            ((ChannelExec) ch).setPty(false);
            ch.connect();
            ch.disconnect();
            session.disconnect();

        } catch (JSchException e) {
            e.printStackTrace();
        }
    }
    public void pullFileFromRemote()
    {
        Session session;
        JSch jSch = new JSch();
        try {
            jSch.addIdentity(privateKey.toString());
            session = jSch.getSession(userName,hostName,port);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking","no");
            session.setConfig(config);
            session.connect();
            Channel ch = session.openChannel("sftp");
            ch.connect();
            ChannelSftp chSftp = (ChannelSftp) ch;
            System.out.println("File will be downloaded");
            chSftp.get(FileConstants.GCP_DB_Directory+"/QueryRequestResponse/responseFile.txt",FileConstants.LOCAL_DB_LOCATION+"/QueryRequestResponse/");
            chSftp.exit();
            ch.disconnect();
            session.disconnect();

        } catch (JSchException | SftpException e) {
            e.printStackTrace();
        }
    }

    public String readDirectory()
    {
        String createCommand = "ls " + FileConstants.GCP_DB_Directory;
        createCommand = createCommand.substring(0,createCommand.length() - 1);
//        String command = "echo " + createCommand;

        Session session;
        JSch jSch = new JSch();
        String out="";

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        try {
            jSch.addIdentity(privateKey.toString());
            session = jSch.getSession(userName,hostName,port);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking","no");
            session.setConfig(config);
            session.connect();
            Channel ch = session.openChannel("exec");
            ((ChannelExec) ch).setCommand(createCommand);
            ((ChannelExec) ch).setPty(false);
            ch.connect();
            ch.setOutputStream(bao);
            Thread.sleep(2000);
            ch.disconnect();
            out = new String(bao.toByteArray());
            session.disconnect();

        } catch (JSchException | InterruptedException e) {
            e.printStackTrace();
        }
        return out;

    }

    public String readFile(String fileName)
    {
        String createCommand = "scp -f " + FileConstants.GCP_DB_Directory+ fileName;
//        String command = "echo " + createCommand;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Session session;
        JSch jSch = new JSch();
        String out="";
        String data="";
        InputStream stream=null;
        try {
            jSch.addIdentity(privateKey.toString());
            session = jSch.getSession(userName,hostName,port);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking","no");
            session.setConfig(config);
            session.connect();
            Channel ch = session.openChannel("sftp");
            ch.connect();
            ChannelSftp chSftp = (ChannelSftp) ch;
            stream = chSftp.get(FileConstants.GCP_DB_Directory+ fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            while((data = br.readLine()) != null)
            {
                out += data;
                out += "\n";
            }
            chSftp.exit();
            ch.disconnect();
            session.disconnect();

        } catch (JSchException | SftpException | IOException e) {
            e.printStackTrace();
        }

        return out;

    }


}
