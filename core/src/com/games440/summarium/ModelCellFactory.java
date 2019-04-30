package com.games440.summarium;

public class ModelCellFactory {
    private static int _id = 0;

    public static ModelCell getModelCell(int value)
    {
        return new ModelCell(value,_id++);
    }

}
