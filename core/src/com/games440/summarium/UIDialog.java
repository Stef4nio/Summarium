package com.games440.summarium;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class UIDialog extends Dialog {
    public UIDialog(String title, Skin skin, String windowStyleName) {
        super(title, skin, windowStyleName);
        this.setVisible(false);
    }

    @Override
    public Dialog show(Stage stage, Action action) {
        if(action!=null) {
            Color color = this.getColor();
            color.a = 0;
            this.setColor(color);
        }
        this.setVisible(true);
        return super.show(stage, action);
    }

    @Override
    public Dialog show(Stage stage) {
        this.setVisible(true);
        Color color = this.getColor();
        color.a = 0;
        this.setColor(color);
        return super.show(stage);
    }

    @Override
    public void hide(Action action) {
        this.setVisible(false);
        super.hide(action);
    }

    @Override
    public void hide() {
        this.setVisible(false);
        super.hide();
    }
}
