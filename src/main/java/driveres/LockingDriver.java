package driveres;

import locking.TableLocker;

import java.io.IOException;

public class LockingDriver {
    public static void main(String[] args) throws IOException {

        // Acquire lock on table "emptable@hashik" will be put in the file
        TableLocker.lockTable("hashik", "emptable");

//        Returns false in two cases,
//        1. Lock is held by same person that we are checking lock on, for example if we pass "hashik" here it'll be false, as emptable@hashik from above
//        2. If there is no lock entry for the file
        System.out.println(TableLocker.checkIfLocked("otherUser", "emptable")); // Returns true
        System.out.println(TableLocker.checkIfLocked("hashik", "emptable")); // Returns false


//         Removes hashik@emptable from the file
        TableLocker.unlockTable("hashik", "emptable");
        System.out.println(TableLocker.checkIfLocked("otherUser", "emptable"));
    }
}
