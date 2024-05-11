package com.example.horgaszujbolt;

public class Product {
    private String leiras;
    private String ar;
    private int kepResource;

    public Product() {}

    public Product( String leiras, String ar, int kepResource) {

        this.leiras = leiras;
        this.ar = ar;
        this.kepResource = kepResource;
    }

    public String getLeiras() {return leiras;}

    public String getAr() {return ar;}

    public int getKepResource() {return kepResource;}
}
