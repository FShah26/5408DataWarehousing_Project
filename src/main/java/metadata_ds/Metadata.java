package metadata_ds;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import constants.FileConstants;
import remotestorage.GCPStorage;
import remotestorage.GDDReader;
import remotestorage.GDDWriter;
import remotestorage.RemoteServerConnection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class Metadata {

    public Map<String, TablePropertiesDS> databaseMap;
    String location = "";
    String databaseName = "";
    String locationGDD = "";
    GDDWriter gddWriter;
    GDDReader gddReader;
    RemoteServerConnection remoteServerConnection;

    public Metadata() {
        this.databaseMap = new HashMap<>();
        gddWriter = new GDDWriter();
        gddReader = new GDDReader();
        if (FileConstants.flag == 0) {
            remoteServerConnection = new RemoteServerConnection();
        }
    }

    public void updateMetadataProps(String location) {
        this.databaseMap = new HashMap<>();
        this.location = location; //+ "\\" + databaseName + "\\" ;
        this.locationGDD = FileConstants.GDD_FILE;
        this.databaseName = "createDB";
    }


    public TablePropertiesDS loadMetaData() {
        try {
            if (this.checkDBExist(this.databaseName)) {
                File directoryPath = new File(this.location);
                File[] files = directoryPath.listFiles();
                TablePropertiesDS tablePropertiesDS = new TablePropertiesDS();
                for (File file : files) {

                    String[] filename = file.getName().split("\\-");
                    if (filename[0].equals("meta")) {
                        TableProperties tableProperties = new TableProperties();
                        tableProperties.setTableName(filename[1].split("\\.")[0]);
                        tableProperties.setStructureUpdated(false);
                        tableProperties.setLocation(filename[1]);
                        tableProperties.setLocationType("local");
                        TableColDS tableColDS = new TableColDS();
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        String data;
                        int index = 0;
                        while ((data = br.readLine()) != null) {
                            String[] fields = data.split("\\~");
                            List<String> field = new ArrayList<>();
                            field = Arrays.asList(fields.clone());
                            TableColumn column = new TableColumn();
                            column.setColumnIndex(index);
                            column.setColumnName(fields[0]);
                            column.setDataType(fields[1]);
                            column.setConstraints(field.subList(2, field.size()));
                            tableColDS.insertCol(column);
                            index += 1;
                        }
                        tableProperties.setColumns(tableColDS);
                        tablePropertiesDS.insertProp(tableProperties);
                    }
                }
                if (FileConstants.flag == 0) {

                String remoteFiles = "";
                remoteFiles = remoteServerConnection.readDirectory();
                Scanner sc = new Scanner(remoteFiles);
                while (sc.hasNextLine()) {
                    String file = sc.nextLine();
                    String[] filename = file.split("\\-");
                    if (filename[0].equals("meta")) {
                        TableProperties tableProperties = new TableProperties();
                        tableProperties.setTableName(filename[1].split("\\.")[0]);
                        tableProperties.setStructureUpdated(false);
                        tableProperties.setLocation(filename[1]);
                        tableProperties.setLocationType("remote");
                        TableColDS tableColDS = new TableColDS();
                        String fileContent = new String(remoteServerConnection.readFile(file));
                        Scanner scContents = new Scanner(fileContent);
                        scContents.nextLine();
                        int index = 0;
                        while (scContents.hasNextLine()) {
                            String[] fields = scContents.nextLine().split("\\~");
                            List<String> field = new ArrayList<>();
                            field = Arrays.asList(fields.clone());
                            TableColumn column = new TableColumn();
                            column.setColumnIndex(index);
                            column.setColumnName(fields[0]);
                            column.setDataType(fields[1]);
                            column.setConstraints(field.subList(2, field.size()));
                            tableColDS.insertCol(column);
                            index += 1;
                        }
                        tableProperties.setColumns(tableColDS);
                        tablePropertiesDS.insertProp(tableProperties);

                    }
                }
                }


//                Bucket gcp = GCPStorage.getStorage();
//                Page<Blob> blobs = gcp.list();
//                for (Blob blob : blobs.iterateAll()) {
//                    String[] filename = blob.getName().split("\\-");
//                    if (filename[0].equals("meta")) {
//                        TableProperties tableProperties = new TableProperties();
//                        tableProperties.setTableName(filename[1].split("\\.")[0]);
//                        tableProperties.setStructureUpdated(false);
//                        tableProperties.setLocation(filename[1]);
//                        tableProperties.setLocationType("remote");
//                        TableColDS tableColDS = new TableColDS();
//                        String fileContent = new String(blob.getContent());
//                        Scanner sc = new Scanner(fileContent);
//                        sc.nextLine();
//                        int index = 0;
//                        while (sc.hasNextLine()) {
//                            String[] fields = sc.nextLine().split("\\~");
//                            List<String> field = new ArrayList<>();
//                            field = Arrays.asList(fields.clone());
//                            TableColumn column = new TableColumn();
//                            column.setColumnIndex(index);
//                            column.setColumnName(fields[0]);
//                            column.setDataType(fields[1]);
//                            column.setConstraints(field.subList(2, field.size()));
//                            tableColDS.insertCol(column);
//                            index += 1;
//                        }
//                        tableProperties.setColumns(tableColDS);
//                        tablePropertiesDS.insertProp(tableProperties);
//                    }
//                }
                databaseMap.put(this.databaseName, tablePropertiesDS);
                return tablePropertiesDS;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean checkWithMetaData(String query) {
        Map<String, String> queryTokenss = createPropertiesFromQueryWithWhere(query);
        String tablename = queryTokenss.get("table");
        String queryType = query.split(" ")[0].toLowerCase(Locale.ROOT);
        switch (queryType) {
            case "select":
                return checkIfTableExists(queryType, tablename) && checkIfColumnExists(tablename, queryTokenss.get("fields")) && checkDataType(queryTokenss.get("where"), tablename);
            case "delete":
                return checkIfTableExists(queryType, tablename) && checkIfColumnExists(tablename, queryTokenss.get("fields")) && checkDataType(queryTokenss.get("where"), tablename);
            case "create":
                return checkIfTableExists(queryType, tablename);
            case  "drop":
                return checkIfTableExists(queryType, tablename);
            case "insert":
                return checkIfTableExists(queryType, tablename);
            case "update":
                return (checkIfTableExists(queryType, tablename) && checkDataTypeForUpdate(queryTokenss.get("fields"), tablename) && checkDataType(queryTokenss.get("where"), tablename));
            default:
                break;
        }
        return false;
    }

    public boolean checkDataType(String values, String tableName) {
        boolean result = true;
        values = values.contains(";") ? values.replace(";", "").trim() : values;
        String[] value = values.split("=");
        int index;
        Map<Integer, TableColumn> columns = getTableColumns(tableName);
        if(!values.equals(""))
        {
            try {
                index = getColumnIndex(tableName, value[0]);
            } catch (Exception e) {
                index = -1;
            }
            if (index >= 0) {
                TableColumn column = columns.get(index);
                if (column.getDataType().contains("int")) {
                    try {
                        int intValue = Integer.parseInt(value[1]);
                        result = true;
                    } catch (NumberFormatException e) {
                        result = false;
                        System.out.println("Column : " + value[0] + " expects a valid integer.");
                    }
                }
            } else {
                result = false;
                System.out.println("Column : " + value[0] + " does not exist in table " + tableName);
            }
        }

        return result;
    }

    public boolean checkDataTypeForUpdate(String values, String tableName) {
        boolean result = false;
        values = values.contains(";") ? values.replace(";", "").trim() : values;
        String[] conditions = values.split(",");
        StringBuilder fields = new StringBuilder();
        for (String condition : conditions) {
            String[] value = condition.split("=");
            fields.append(value[0]).append(",");
        }
        fields = new StringBuilder(fields.substring(0, fields.length() - 1));
        if (checkIfColumnExists(tableName, fields.toString())) {
            for (String condition : conditions) {
                if (!checkDataType(condition, tableName)) {
                    result = false;
                    break;
                }
                result = true;
            }
        }
        return result;


    }

    public boolean checkIfTableExists(String queryType, String TableName) {
        TablePropertiesDS ds = this.databaseMap.get(this.databaseName);
        if (queryType.equals("create")) {
            if (ds != null) {
                if (ds.keys().contains(TableName.toLowerCase(Locale.ROOT))) {
                    System.out.println("Table " + TableName + " already exists.");
                    return false;
                }
            }
            return true;
        } else {
            if (ds != null) {
                if (ds.keys().contains(TableName.toLowerCase(Locale.ROOT))) {
                    return true;
                }
            }
            System.out.println("Table " + TableName + " does not exist.");
            return false;
        }

    }

    public boolean checkIfColumnExists(String tbl_name, String fields) {
        TablePropertiesDS props = this.databaseMap.get(this.databaseName);
        TableProperties tblProps = props.properties.get(tbl_name);
        Set<String> columns = tblProps.getColumns().keys();
        if (fields != null) {
            String[] cols = fields.split(",");
            if (!cols[0].equals("*")) {
                for (String col : cols
                ) {
                    if (!columns.contains(col)) {
                        System.out.println("Table " + tbl_name + " has no column with name " + col);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Map<String, String> createPropertiesFromQueryWithWhere(String query) {
        query = query.contains(";") ? query.replace(";", "").trim() : query;
        Map<String, String> props = new HashMap<>();
        String whereCondition = "";
        String values = "";
        String tableName = "";
        String columnSet = "";
        String[] columns = new String[1];
        String[] tokens = query.split(" ");
        switch (tokens[0].toLowerCase(Locale.ROOT)) {
            case "select":
                columnSet = tokens[1];
                tableName = tokens[3];
                if (query.contains("where")) {
                    whereCondition = tokens[5];
                }
                props.put("fields", columnSet);
                tableName = tableName.contains(";") ? tableName.replace(";", "").trim() : tableName;
                props.put("table", tableName);
                props.put("where", whereCondition);
                break;
            case "create":
                tableName = tokens[2];
                props.put("table", tableName);
                break;
            case "delete":
                //columnSet = tokens[1];
                tableName = tokens[2];
                columns[0] = "*";
                if (query.contains("where")) {
                    whereCondition = tokens[4];
                }

                tableName = tableName.contains(";") ? tableName.replace(";", "").trim() : tableName;
                props.put("table", tableName);
//                props.put("columns",columns);
                props.put("where", whereCondition);
                break;
            case "insert":
                tableName = tokens[2];
                values = tokens[3];
                tableName = tableName.contains(";") ? tableName.replace(";", "").trim() : tableName;
                props.put("table", tableName);
                props.put("values", values);
                break;
            case "update":
                tableName = tokens[1];
                if (query.contains("where")) {
                    whereCondition = tokens[5];
                    whereCondition = whereCondition.contains(";") ? whereCondition.replace(";", "").trim() : whereCondition;
                }
                columnSet = tokens[3];
                props.put("table", tableName);
                props.put("where", whereCondition);
                props.put("fields", columnSet);
                break;

            default:
                props = null;
                break;
        }
        return props;
    }

    public void writeMetadataToGDD() {
        TablePropertiesDS ds = this.databaseMap.get(this.databaseName);
//        File file = new File(this.locationGDD + "\\gdd.txt");
        try {
//            file.createNewFile();
            gddWriter.createNewGDD();
//            FileWriter fileWriter = new FileWriter(this.locationGDD + "\\gdd.txt");
            for (TableProperties prop : ds.values()
            ) {
                gddWriter.appendGDD(prop.getTableName() + "~" + prop.getLocation() + "~" + prop.getLocationType());
//                gddWriter.appendGDD("\n");
            }
//            fileWriter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Map<String, String[]> getGDD() {
        TablePropertiesDS ds = this.databaseMap.get(this.databaseName);
//        File file = new File(this.locationGDD + "\\gdd.txt");
        Map<String, String[]> gdd = new HashMap<>();
        try {
            String fileContent = new String(gddReader.readGDD());
            Scanner sc = new Scanner(fileContent);
//            sc.nextLine();
            String[] queryTokens;
            int index = 0;
            do  {
                queryTokens = sc.nextLine().split("~");
                if(queryTokens[0] != "")
                {
                    gdd.put(queryTokens[0], new String[]{queryTokens[1], queryTokens[2]});
                }

            }while (sc.hasNextLine());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return gdd;
    }

    public String[] getTableLocation(String tableName) {
        Map<String, String[]> gdd = new HashMap<>();
        gdd = this.getGDD();
        return gdd.get(tableName);

    }

    public int getColumnIndex(String tableName, String columnName) {
        TablePropertiesDS ds = this.databaseMap.get(this.databaseName);
        TableProperties prop = ds.get(tableName);
        TableColDS colDs = prop.getColumns();
        TableColumn column = colDs.get(columnName);
        return column.getColumnIndex();
    }

    public Map<Integer, TableColumn> getTableColumns(String tableName) {
        Map<Integer, TableColumn> tableColumns = new HashMap<>();
        TablePropertiesDS ds = this.databaseMap.get(this.databaseName);
        TableProperties prop = ds.get(tableName);
        TableColDS colDs = prop.getColumns();
        for (TableColumn col : colDs.values()
        ) {
            tableColumns.put(col.getColumnIndex(), col);
        }
        return tableColumns;
    }

    public void updateMetadata() {
        loadMetaData();
        writeMetadataToGDD();
    }

    public boolean checkDBExist(String DBName) {
        String dbLocation = FileConstants.LOCAL_DB_LOCATION; //+ DBName + "\\" ;
        File file = new File(dbLocation);
        if (file.isDirectory()) {
            return true;
        } else {
            System.out.println("No such Database exist");
            return false;
        }

    }


}
