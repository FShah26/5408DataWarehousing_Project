package remotestorage;

public class RemoteConnectionTest {
    public static void main(String[] args) {
        RemoteServerConnection rm = new RemoteServerConnection();
//        rm.pullFileFromRemote();
        rm.pushFileToRemote("select * from test");
//        System.out.println(rm.readFile("meta-test.txt"));

//        System.out.println(rm.readDirectory());

    }
}
