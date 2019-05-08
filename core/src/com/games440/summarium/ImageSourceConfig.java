package com.games440.summarium;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.Hashtable;

public class ImageSourceConfig {
    private final String FieldBackground ="game_field_bg.png";
    private final String CellIdleBackground ="cell_bg.png";
    private final String CellSelectedBackground = "cell_highlighter.png";
    private final String CellBorder = "cell_border.png";
    private final String CellClearedBackground ="cell_cleared_bg.png";
    private final String CellClearedSelectedBackground = "cell_highlighter2.png";
    private final String TopBar = "top_bar.png";
    private final String[] Numbers = new String[10];
    private final String[] NegativeNumbers = new String[20];
    private final String[] aimNumbers = new String[10];
    private final Hashtable<ViewCellState,String[]> StateBackgrounds = new Hashtable<ViewCellState, String[]>();

    private static final ImageSourceConfig IMAGE_SOURCE_CONFIG = new ImageSourceConfig();

    private ImageSourceConfig(){
        InitImageSources();
    }

    public static ImageSourceConfig getImageSourceConfig() {
        return IMAGE_SOURCE_CONFIG;
    }

    public Image getTopBar()
    {
        return new Image(new Texture(TopBar));
    }

    public Image getFieldBackground() {
        return new Image(new Texture(Gdx.files.internal(FieldBackground)));    }

    public Image getCellBorder() {
        return new Image(new Texture(Gdx.files.internal(CellBorder)));
    }

    public Image getNumber(int number)
    {
        /*if(number>9||number<0)
        {
            return null;
        }*/
        return new Image(new Texture(Gdx.files.internal(Numbers[number-1])));
    }

    public Image getNegativeNumber(int number)
    {
        /*if(number>9||number<0)
        {
            return null;
        }*/
        return new Image(new Texture(Gdx.files.internal(NegativeNumbers[number+9])));
    }

    public Image getAimNumber(int number)
    {
        if(number<=19||number>=10)
        {
            Image tempAimImage = new Image(new Texture(aimNumbers[number-10]));
            tempAimImage.setScale(0.7f);
            return tempAimImage;
        }
        return null;
    }

    public Image getCellBackgroundByState(ViewCellState state, boolean isCleared)
    {
        return new Image(new Texture(Gdx.files.internal(StateBackgrounds.get(state)[isCleared?1:0])));
    }

    private void InitImageSources()
    {
        StateBackgrounds.put(ViewCellState.Idle, new String[]{CellIdleBackground,CellClearedBackground});
        StateBackgrounds.put(ViewCellState.Selected, new String[]{CellSelectedBackground,CellClearedSelectedBackground});
        for(int i = 0; i<Numbers.length;i++)
        {
            Numbers[i] = "Numbers/"+Integer.toString(i+1)+".png";
        }
        for(int i = 0; i<NegativeNumbers.length;i++)
        {
            NegativeNumbers[i] = "NegativeModeNumbers/"+Integer.toString(i-9)+".png";
        }
        for(int i = 0; i<aimNumbers.length;i++)
        {
            aimNumbers[i] = "Aims/"+Integer.toString(i+10)+".png";
        }
    }
}
