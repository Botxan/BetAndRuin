package utils;

import uicontrollers.NavBarController;

import java.util.Stack;

/**
 * This class is used to record the user's navigation.
 * It allows storing and restoring the windows previously accessed by the user.
 * @author Josefinators
 * @version v1
 */
public class History {
    private Stack<Window> previousWindows;
    private Window currentWindow;
    private Stack<Window> nextWindows;

    /**
     * Constructor for History class, used to initialize
     * an empty history.
     */
    public History() {
        this(null);
    }

    /**
     * Constructor for History class, used to initialize the
     * history with the first window visited.
     * @param window the initial window.
     */
    public History(Window window) {
        previousWindows = new Stack<Window>();
        currentWindow = window;
        nextWindows = new Stack<Window>();
    }

    /**
     * Returns the last visited window.
     * @return the last visited window.
     */
    public Window getPreviousWindow() {
        if (previousWindows.isEmpty()) return null;
        return previousWindows.peek();
    }

    /**
     * Adds a given window to the stack of previous windows.
     * @param window the window.
     */
    public void setPreviousWindow(Window window) {
        previousWindows.push(window);
    }

    /**
     * Retrieves the current window.
     * @return the current window.
     */
    public Window getCurrentWindow() {
        return currentWindow;
    }

    /**
     * Sets the current window.
     * @param window the current window.
     */
    public void setCurrentWindow(Window window) {
        currentWindow = window;
    }

    /**
     * Returns the next window.
     * @return the next window.
     */
    public Window getNextWindow() {
        if (nextWindows.isEmpty()) return null;
        return nextWindows.peek();
    }

    /**
     * Adds a given window to the stack of next windows.
     * @param window the window.
     */
    public void setNextWindow(Window window) {
        nextWindows.push(window);
    }

    /**
     * Returns the previous window visited by the user
     * and updates the history accordingly.
     * @return the previous window visited by the user.
     */
    public Window moveToPrevious() {
        if (previousWindows.isEmpty()) return null;

        nextWindows.push(currentWindow);
        currentWindow = previousWindows.pop();
        return currentWindow;
    }

    /**
     * Returns the next window and updates the
     * history accordingly.
     * @return the next window.
     */
    public Window moveToNext() {
        if (nextWindows.isEmpty()) return null;

        previousWindows.push(currentWindow);
        currentWindow = nextWindows.pop();
        return currentWindow;
    }

    /**
     * Stores the current window in previousWindows and
     * adds sets the given window as the currentWindow
     */
    public void moveToWindow(Window window) {
        previousWindows.push(currentWindow);
        currentWindow = window;
    }

    /**
     * Clears all the history
     */
    public void clear() {
        previousWindows.clear();
        nextWindows.clear();
    }
}
