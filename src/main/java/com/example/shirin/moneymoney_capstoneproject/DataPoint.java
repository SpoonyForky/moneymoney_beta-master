package com.example.shirin.moneymoney_capstoneproject;

/**
 * Created by Shirin on 10/11/2016.
 */
import java.io.Serializable;

public class DataPoint implements Serializable {
    private static final long serialVersionUID = 4990064484446119372L;
    public String letter;
    public float frequency;

    public DataPoint(final String letter, final float frequency) {
        this.letter = letter;
        this.frequency = frequency;
    }
}
