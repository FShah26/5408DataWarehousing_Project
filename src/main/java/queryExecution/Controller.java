package queryExecution;

import constants.FileConstants;

import java.io.*;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

public class Controller implements Runnable{
    private String query="";
    Path filePath;
    WatchService watchService = null;
    ExecutionEngine executionEngine;
    boolean running = true;
    public Controller(ExecutionEngine executionEngine)
    {
        this.executionEngine = executionEngine;
        this.filePath = Paths.get(FileConstants.LOCAL_DB_LOCATION,"QueryRequestResponse");
        try {
            watchService = FileSystems.getDefault().newWatchService();
//            System.out.println(filePath);
            filePath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,StandardWatchEventKinds.ENTRY_DELETE,StandardWatchEventKinds.ENTRY_MODIFY);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void run()
    {
        while(running){
            WatchKey key = null;
            try {
                key =  watchService.poll(10, TimeUnit.SECONDS);
                if(key != null)
                {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent<Path> ev = (WatchEvent<Path>) event;
                        Path filename = ev.context();
                        System.out.println("The request file name in controller is:" +filename.getFileName());
                        if(filename.getFileName().equals("requestFile.txt"))
                        {
                            System.out.println("File modified");
                            //Perform Operation
                            File file = new File(filePath + "/requestFile.txt");
                            BufferedReader br = new BufferedReader(new FileReader(file));
                            String data;
                            while ((data = br.readLine()) != null) {
                                this.query = data;
                                System.out.println("The data in while loop is:"+data);
                            }
                            System.out.println("The data in after while loop is:"+data);
                            file.createNewFile();
                            //write to the remote file
                        }
                        else
                        {
                            System.out.println("else part in controller");
                            //Read response file and show output
                            File file = new File(filePath + "/responseFile.txt");
                            BufferedReader br = new BufferedReader(new FileReader(file));
                            String data;
                            while ((data = br.readLine()) != null) {
                                System.out.println(data);
                            }

                        }
                        
                    }
                    key.reset();
                }
                this.execute();
            } catch (InterruptedException | FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public boolean execute()
    {
        System.out.println("Inside execute function in controller");
        System.out.println("The query in execute methos is:"+this.query);
        if(!this.query.equals(""))
        {
            String[] queryToken = query.split(" ");
            System.out.println("The query token is:"+queryToken[0]);
            switch (queryToken[0].toUpperCase()) {
                case "COMMIT;":
                    break;
                case "SELECT":
                    executionEngine.Select(query);
                    break;
                case "INSERT":
                    executionEngine.Insert(query);
                    break;
                case "UPDATE":
                    executionEngine.update(query);
                    break;
                case "DELETE":
                    executionEngine.delete(query);
                    break;
                default :
                    System.out.println("Not a valid query");
                    break;
            }
            this.query="";
            return true;
        }
        this.query="";
        return false;
    }

    public void stop()
    {
        this.running = false;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
