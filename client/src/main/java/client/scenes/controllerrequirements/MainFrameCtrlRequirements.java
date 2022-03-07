package client.scenes.controllerrequirements;

import javafx.scene.input.KeyEvent;

public interface MainFrameCtrlRequirements {

    void displayUsernameError(boolean show);

    void displayServerIPError(boolean show);

    /**
     * Provides functionality for keybindings to accelerate certain actions
     *
     * @param e Information about a keypress performed by the user
     *          <p>
     *          This should only be called by the MainCtrl showQuestionFrame method
     */
    void keyPressed(KeyEvent e);
}
