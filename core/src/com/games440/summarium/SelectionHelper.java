package com.games440.summarium;

import java.util.ArrayList;
import java.util.List;

public class SelectionHelper
{
    private static int _width;
    private static int _height;


    public static void CheckSelection(ModelCell[][] field) {

        _width = field.length;
        _height = field[0].length;

       /* System.out.println();
        printArr(field);*/


        FieldIsland maxIsland = null;
        List<FieldIsland> islandsList = new ArrayList<FieldIsland>();

        //found all islands, and found max size island
        for (int i=0;i<_width;i++)
        {
            for (int j=0;j<_height;j++)
            {
                if (field[i][j].selectionValue==1)
                {
                    int islandSize = fill(i,j,1,-1, field, 0);
                    FieldIsland island =new FieldIsland(j,i,islandSize);
                    islandsList.add(island);
                    if (maxIsland==null || islandSize > maxIsland.size)
                    {
                        maxIsland = island;
                    }
                }
            }
        }
        islandsList.remove(maxIsland);
        /*System.out.println();
        printArr(field);*/

        /*System.out.println();
        for (FieldIsland island : islandsList) {
            System.out.println(island);
        }*/

       /* System.out.println();
        System.out.println("maxIsland:"+maxIsland);*/



        for (FieldIsland island : islandsList) {
            fill(island.y,island.x,-1,0, field, 0);
        }
        if(maxIsland!=null) {
            fill(maxIsland.y, maxIsland.x, -1, 1, field, 0);
        }

        /*System.out.println();
        printArr(field);*/

    }


    private static void printArr(ModelCell[][] arr)
    {
        for (int i=0;i<_width;i++)
        {
            for (int j=0;j<_height;j++)
            {
                System.out.print((arr[i][j].selectionValue)+"\t");
            }
            System.out.println();
        }
    }



    private static int  fill(int i, int j, int oldValue, int newValue, ModelCell[][] arr, int amount)
    {
        if (arr[i][j].selectionValue==oldValue)
        {
            amount++;
            arr[i][j].selectionValue=newValue;

            if (i>0 && arr[i-1][j].selectionValue==oldValue)
            {
                amount = fill(i-1,j, oldValue, newValue, arr,amount);
            }
            if (j>0 && arr[i][j-1].selectionValue==oldValue)
            {
                amount = fill(i,j-1, oldValue, newValue, arr,amount);
            }

            if (i<_width-1 && arr[i+1][j].selectionValue==oldValue)
            {
                amount = fill(i+1,j, oldValue, newValue, arr,amount);
            }
            if (j<_height-1 && arr[i][j+1].selectionValue==oldValue)
            {
                amount = fill(i,j+1, oldValue, newValue, arr,amount);
            }
        }
        return amount;
    }

}



class FieldIsland
{
    public int x;
    public int y;
    public int size;

    public FieldIsland(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    @Override
    public String toString() {
        return "FieldIsland{" +
                "x=" + x +
                ", y=" + y +
                ", size=" + size +
                '}';
    }

}
