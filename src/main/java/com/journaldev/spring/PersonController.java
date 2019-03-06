package com.journaldev.spring;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
 
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

@RestController
public class PersonController {
	
	@RequestMapping("/")
	public String welcome() {
		uploadFileToAwsEc2();
		return "Welcome to uploading a file to Aws Ec2...";
	}
	
	public void uploadFileToAwsEc2() {
        String server = "ec2-100-24-67-60.compute-1.amazonaws.com";
        int port = 21;
        String user = "username";
        String pass = "password";
        int seconds = 300*1000;
		
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.setConnectTimeout(seconds);
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
 
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
 
            // APPROACH #1: uploads first file using an InputStream
            File firstLocalFile = new File("E:/InputFiles/LocalFile.txt");
 
            String firstRemoteFile = "LocalFile.txt";
            InputStream inputStream = new FileInputStream(firstLocalFile);
 
            System.out.println("Start uploading first file");
			ftpClient.setSoTimeout(seconds);
            boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
            inputStream.close();
            if (done) {
                System.out.println("The first file is uploaded successfully.");
            }
  
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
	
}
