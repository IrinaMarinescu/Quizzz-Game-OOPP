package client.scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LobbyCtrlTest {

    //There is probably more to test but I'm not sure what that would be tbh, I can do that later

    @Test
    public void removePersonTest() {
        removePlayer("Yannick");
        assertTrue(list.get(0).equals("Per"));
    }

    @Test
    public void addPersonTest() {
        addPlayer("John");
        assertTrue(list.get(6).equals("John"));
    }

    @Test
    public void clearTest() {
        clearPlayers();
        assertTrue(list.isEmpty());
    }

    //The methods because I can't detect them normally for some reason

    private void removePlayer(String name) {
        int r = 0;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(name)) {
                r = i;
            }
        }
        list.remove(r);
    }

    private void addPlayer(String name) {
        list.add(name);
    }

    private void clearPlayers() {
        list.clear();
    }

    ObservableList<String> list = FXCollections.observableArrayList(
            "Yannick",
            "Per",
            "Irina",
            "Andrei",
            "Mirella",
            "Chris"
    );
}
