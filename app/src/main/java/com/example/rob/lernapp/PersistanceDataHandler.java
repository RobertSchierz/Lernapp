package com.example.rob.lernapp;

public class PersistanceDataHandler {

    private static String uniqueClientId;
    private static String uniqueDatabaseId;


    public static String getUniqueClientId() {
        return uniqueClientId;
    }

    public static void setUniqueClientId(String uniqueClientId) {
        PersistanceDataHandler.uniqueClientId = uniqueClientId;
    }

    public static String getUniqueDatabaseId() {
        return uniqueDatabaseId;
    }

    public static void setUniqueDatabaseId(String uniqueDatabaseId) {
        PersistanceDataHandler.uniqueDatabaseId = uniqueDatabaseId;
    }


}
