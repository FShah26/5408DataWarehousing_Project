package parsing;

import com.google.gson.internal.bind.util.ISO8601Utils;
import constants.FileConstants;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

public class UserAuthentication {



    Path path = Paths.get(FileConstants.LOCAL_DB_LOCATION,"Credentials.txt");
    String credentialsPath = path.toString();

    public boolean validation(String userName, String password) {
        FileReader fileReader = null;
        String line = null;
        String[] splitCredentials;
        boolean flag = false;
        System.out.println("The credentials path is:"+credentialsPath);
        HashMap<String,String> credential = new HashMap<>();
        File file = new File(credentialsPath);
        if (file.exists())
        {
            try {
                fileReader = new FileReader(credentialsPath);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                while((line = bufferedReader.readLine()) != null)
                {
                    splitCredentials = line.split("@");
                    credential.put(splitCredentials[0],splitCredentials[1]);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (password.trim().equals(credential.get(userName).trim()))
            {
                flag = true;
            }
            else
                flag= false;
        }
        else
            System.out.println("File Path does not exists");
        return flag;
    }
}

