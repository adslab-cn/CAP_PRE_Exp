package bupt.CAPPRE;


import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;

public class CAPPREPath {
    String ID ;
    ArrayList<Element[]>  ys;

    public CAPPREPath(String ID, ArrayList<Element[]> ys) {
        this.ID = ID;
        this.ys = ys;
    }
}
