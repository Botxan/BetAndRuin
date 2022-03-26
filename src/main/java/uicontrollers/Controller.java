package uicontrollers;

import ui.MainGUI;

public interface Controller {
    /**
     * Setter for the main GUI
     * @param mainGUI the main GUI
     */
    void setMainApp(MainGUI mainGUI);

    /**
     * Refreshes all the texts inside the window
     */
    public void redraw();
}
