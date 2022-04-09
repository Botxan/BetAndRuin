package utils.skin;

import javafx.scene.control.DatePicker;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.StackPane;

public class MyDatePickerSkin extends DatePickerSkin {

    StackPane arrowButtonAlias;
    public MyDatePickerSkin(DatePicker control) {
        super(control);
        arrowButtonAlias = (StackPane) control.lookup(".arrow-button");
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        super.layoutChildren(x, y, w, h);
        getChildren().remove(getEditor());
        arrowButtonAlias.resizeRelocate(x, y, w, h);
    }

}
