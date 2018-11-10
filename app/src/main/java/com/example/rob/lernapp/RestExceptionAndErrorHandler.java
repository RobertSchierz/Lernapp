package com.example.rob.lernapp;

import android.widget.Toast;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.rest.spring.api.RestErrorHandler;
import org.springframework.core.NestedRuntimeException;
import org.springframework.web.client.HttpClientErrorException;

@EBean
public class RestExceptionAndErrorHandler implements RestErrorHandler {

    @RootContext
    Learngroups learngroupsactivity;

    @RootContext
    LearngroupViewActivity learngroupViewActivity;

    @Override
    public void onRestClientExceptionThrown(NestedRuntimeException e) {

        if (e instanceof HttpClientErrorException) {
            HttpClientErrorException exception = (HttpClientErrorException) e;
            switch (exception.getStatusText()) {

                case "servererror":
                    setMessageToUser("Serverfehler", true);
                    break;

                case "linkinvite_allreadyIn":
                    setMessageToUser("Du bist bereits in der Gruppe", true);
                    break;

                case "linkinvite_usernotfound":
                    setMessageToUser("Anwender nicht in Datenbank", true);
                    break;

                case "linkinvite_groupnotfound":
                    setMessageToUser("Gruppe mit diesem Link nicht in Datenbank", true);
                    break;
                case "postuser_usernotfound":
                    setMessageToUser("Anwender nicht in Datenbank", false);
                    break;

                case "linkinvite_posterror":
                    setMessageToUser("Fehler beim Hinzufügen des Members", true);
                    break;

                case "postuser_posterror":
                    setMessageToUser("Fehler beim Hinzufügen des Members", false);
                    break;

                case "deletegroup_groupnotfound":
                    setMessageToUser("Zu löschende Gruppe nicht gefunden", true);
                    break;

                case "deletemember_membernotingroup":
                    setMessageToUser("Verlassen der Gruppe nicht funktioniert", true);
                    break;

                case "creategroup_creatornotfound":
                    setMessageToUser("Ersteller der Gruppe nicht in der Datenbank", true);
                    break;

                case "getcreatorgroups_nogroupsfound":
                   // setMessageToUser("Keine Gruppen als Creator vorhanden", true);
                    break;

                case "getgroups_nogroupsfound":
                   // setMessageToUser("Keine Gruppen vorhanden", true);
                    break;

                case "getusers_nouserfound":
                    break;

                case "linkinvite_iscreator":
                    setMessageToUser("Du bist bereits in der Gruppe als Creator", true);
                    break;

                case "createcategory_creatornotfound":
                    setMessageToUser("Falsche Creatorid übermittelt", false);
                    break;

                case "createcategory_groupnotfound":
                    setMessageToUser("Falsche Gruppenid übermittelt", false);
                    break;




                default:
                    break;
            }

        }
    }

    @UiThread
    void setMessageToUser(String message, Boolean activitydecision) {
        if (activitydecision) {
            Toast.makeText(learngroupsactivity, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(learngroupViewActivity, message, Toast.LENGTH_SHORT).show();
        }

    }
}
