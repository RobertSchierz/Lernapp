package com.example.rob.lernapp;

public class PersistanceDataHolder {

    private static String uniqueClientId;

    public static String getUniqueClientId() {
        return uniqueClientId;
    }

    public static void setUniqueClientId(String uniqueClientId) {
        PersistanceDataHolder.uniqueClientId = uniqueClientId;
    }

    public static String getUniqueDatabaseId() {
        return uniqueDatabaseId;
    }

    public static void setUniqueDatabaseId(String uniqueDatabaseId) {
        PersistanceDataHolder.uniqueDatabaseId = uniqueDatabaseId;
    }

    private static String uniqueDatabaseId;

}
