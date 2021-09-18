package queryExecution;

import constants.FileConstants;
import driveres.Main;
import ds.*;
import fileloding.LoadFromFile;
import filewriting.WriteToFile;
import locking.TableLocker;
import metadata_ds.Metadata;
import metadata_ds.TableColumn;
import remotestorage.GDDReader;
import remotestorage.RemoteServerConnection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExecutionEngine {
    public String query;
    public String tableName;
    public String selectFields;
    public Metadata md;
    public Map<String, String[]> gdd = new HashMap<>();
    public Map<String, String> queryTokens = new HashMap<>();
    public String where = "";
    public String[] tableLoc;
    public Map<String, Table> fileTables = new HashMap<>();
    public Table table;
    public RemoteServerConnection remoteConnection;
    public GDDReader gddReader;

    public ExecutionEngine(Metadata md) {
        this.md = md;
        this.gdd = md.getGDD();
        remoteConnection = new RemoteServerConnection();
    }

    public void Select(String query) {
        try {
            this.table = LoadFiles(query);

        String[] columns = this.selectFields.split(",");
        List<Integer> lstAttributes = new ArrayList<>();
        if (!columns[0].equals("*")) {
            for (String col : columns
            ) {
                lstAttributes.add(md.getColumnIndex(this.tableName, col));
            }
        }
        if (where != "") {
            String[] whereClause = where.split("=");
            Map<Integer, TableColumn> listColumn = md.getTableColumns(tableName);
            int attributeLocation = md.getColumnIndex(this.tableName, whereClause[0]);
            boolean isInt = listColumn.get(attributeLocation).getDataType().equalsIgnoreCase("int");
            Value valueToSearchFor = new ValueImpl(whereClause[1], isInt);
            List<Row> rowList = table.get(valueToSearchFor, attributeLocation);
            if(this.tableLoc[1].equals("local"))
            {
                if (lstAttributes.size() > 0) {
                    for (Row row : rowList
                    ) {
                        System.out.println(row.toString(lstAttributes));
                    }

                } else {
                    System.out.println(rowList.toString());
                }
            }
            else {
                if (lstAttributes.size() > 0) {
                    System.out.println("In if lstAttributes.size() > 0)");
                    for (Row row : rowList) {
                            FileWriter fileWriter = new FileWriter(FileConstants.GCP_RESPONSE,true);
                            fileWriter.write(row.toString(lstAttributes));
                        }
                    } else {
                    System.out.println("In else lstAttributes.size() > 0)");
                        FileWriter fileWriter = new FileWriter(FileConstants.GCP_RESPONSE,true);
                        fileWriter.write(rowList.toString());
                    }
            }

        } else {
            if(this.tableLoc[1].equals("local"))
            {
                if (lstAttributes.size() > 0) {
                    System.out.println(table.toString(lstAttributes));
                } else {
                    System.out.println(table.toString());
                }
            }
            else
            {
                if (lstAttributes.size() > 0) {
                    System.out.println("In if lstAttributes.size() > 0)");
                    FileWriter fileWriter = new FileWriter(FileConstants.GCP_RESPONSE,true);
                    fileWriter.write(table.toString(lstAttributes));
                } else {
                    System.out.println("In else lstAttributes.size() > 0)");
                    FileWriter fileWriter = new FileWriter(FileConstants.GCP_RESPONSE,true);
                    fileWriter.write(table.toString());
                }


            }


        }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public boolean Insert(String query) {
        try {
            boolean flag = false;
            this.table = LoadFiles(query);
            String queryValues = query.split(" ")[3];
            String[] values = queryValues.substring(7, queryValues.length() - 2).split(",");
            Row row = new RowImpl();
            Map<Integer, TableColumn> columns = md.getTableColumns(tableName);
            for (int i = 0; i < columns.size(); i++) {
                if (columns.get(i).getDataType().equalsIgnoreCase("int")) {
                    row.insertAttribute(Integer.parseInt(values[i]));
                } else {
                    row.insertAttribute(values[i]);
                }

            }
            table.insert(row);
            fileTables.replace(this.tableName, table);
            if(this.tableLoc[1].equals("local"))
            {
                System.out.println("Insertion Successful");
                System.out.println(table.toString());
            }
            else
            {
                FileWriter fileWriter = new FileWriter(FileConstants.LOCAL_DB_LOCATION,true);
                fileWriter.write("Insertion Successful");
                fileWriter.write(table.toString());

            }

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public boolean update(String query) {
        try {
            this.table = LoadFiles(query);
            Map<Integer, TableColumn> columns = md.getTableColumns(tableName);
            if (where != "") {
                //create Value object for where condition
                String[] whereClause = where.split("=");
                int attributeLocationWhere = md.getColumnIndex(this.tableName, whereClause[0]);
                boolean isInt = columns.get(attributeLocationWhere).getDataType().equalsIgnoreCase("int");
                Value keyToReplaceRow = new ValueImpl(whereClause[1], isInt);
                Map<Integer, Value> valuesToUpdateInTheRow = new HashMap<>();

                //create Values object for each column to be updated
                String[] updatedValues = selectFields.split(",");
                for (String condition : updatedValues) {
                    String[] values = condition.split("=");
                    int attributeLocation = md.getColumnIndex(this.tableName, values[0]);
                    isInt = columns.get(attributeLocation).getDataType().equalsIgnoreCase("int");
                    valuesToUpdateInTheRow.put(attributeLocation, new ValueImpl(values[1], isInt));
                }
                table.update(keyToReplaceRow, attributeLocationWhere, valuesToUpdateInTheRow);
                fileTables.replace(this.tableName, table);
                if(this.tableLoc[1].equals("local"))
                {
                    System.out.println("update successful");
                    System.out.println(table.toString());
                }
                else
                {
                    FileWriter fileWriter = new FileWriter(FileConstants.LOCAL_DB_LOCATION,true);
                    fileWriter.write("update successful");
                    fileWriter.write(table.toString());

                }

                return true;
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public boolean delete(String query) {
        Boolean status = false;
        try {
            this.table = LoadFiles(query);


        if (where != "") {
            String[] whereClause = where.split("=");
            int attributeLocation = md.getColumnIndex(this.tableName, whereClause[0]);
            Map<Integer, TableColumn> columns = md.getTableColumns(tableName);
            boolean dataflag = columns.get(attributeLocation).getDataType().equalsIgnoreCase("int");
            Value valueToSearchFor = new ValueImpl(whereClause[1], dataflag);
            status = table.delete(valueToSearchFor, attributeLocation);
            if(this.tableLoc[1].equals("local"))
            {
                if (status) {
                System.out.println("Deletion is successfull");
                }
                else
                {
                    System.out.println("Deletion failed");
                }
                System.out.println(table.toString());
            }
            else
            {
                FileWriter fileWriter = new FileWriter(FileConstants.LOCAL_DB_LOCATION,true);
                if (status) {
                    fileWriter.write("Deletion is successful");
                }
                else
                {
                    fileWriter.write("Deletion failed");
                }
                fileWriter.write(table.toString());

            }

        } else
            table.cleanTable();
        } catch (IOException e) {
            e.printStackTrace();
            status=false;
        }
        return status;
    }

    public void UpdateMetadata() {
        md.updateMetadata();
        this.gdd = md.getGDD();
    }

    public Table LoadFiles(String query) throws IOException{
        this.query = query;
        this.queryTokens = md.createPropertiesFromQueryWithWhere(query);
        this.where = queryTokens.get("where");
        this.tableName = queryTokens.get("table");
        this.selectFields = queryTokens.get("fields");
        this.tableLoc = md.getTableLocation(tableName);
        Table table = this.fileTables.get(tableName);
        if (table == null) {
            try {
                if (tableLoc[1].equals("local")) {
                    LoadFromFile file = new LoadFromFile(tableLoc[0], tableName);
                    table = file.read();
                    TableLocker.lockTable(Main.userName, this.tableName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.fileTables.put(tableName, table);
        }
        return table;
    }
    
    public boolean dataCommitToFile(Map<String, Table> data) {
        //System.out.println("in commit");
        BufferedReader bufferedReader;
        FileReader fileReader;
        String line = null;
        try {
//            fileReader = new FileReader(FileConstants.GDD_FILE + "\\gdd.txt");
//            String fileContent = new String(gddReader.readGDD());
//            bufferedReader = new BufferedReader(fileReader);
            for (String tablename : data.keySet()) {
                String[] splitTablename = null;
                if(this.gdd.containsKey(tablename))
                {
                    var tableProp = this.gdd.get(tablename);
                    if(tableProp[1].equalsIgnoreCase("local"))
                    {
                        if (!TableLocker.checkIfLocked(Main.userName, this.tableName)) {
                            WriteToFile writeLocalFile = new WriteToFile(tableProp[0]);
                            writeLocalFile.write(data.get(tablename));
                            TableLocker.unlockTable(Main.userName, this.tableName);
                        }
                        break;

                    }
                }
//                while ((line = bufferedReader.readLine()) != null) {
//                    splitTablename = line.split("~");
//                    if (tablename.equalsIgnoreCase(splitTablename[0])) {
//                        if (splitTablename[2].equalsIgnoreCase("local")) {
//                            if (!TableLocker.checkIfLocked(Main.userName, this.tableName)) {
//                                WriteToFile writeLocalFile = new WriteToFile(splitTablename[1]);
//                                writeLocalFile.write(data.get(tablename));
//                                TableLocker.unlockTable(Main.userName, this.tableName);
//                            }
//                            break;
//                        }
//                    }
//                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public String getTableLocFromQuery(String query)
    {
        this.query = query;
        this.queryTokens = md.createPropertiesFromQueryWithWhere(query);
        this.where = queryTokens.get("where");
        this.tableName = queryTokens.get("table");
        this.selectFields = queryTokens.get("fields");
        this.tableLoc = md.getTableLocation(tableName);
//        if(tableLoc.equals("remote"))
//        {
//            remoteConnection.pushFileToRemote(query);
//        }
        return tableLoc[1];
    }

}
