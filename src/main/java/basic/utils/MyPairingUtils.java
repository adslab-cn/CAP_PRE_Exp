package basic.utils;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MyPairingUtils {
    public static Element[] mapListOfIntToListOfElement(int[] a, Field F){
        Element[] elements = new Element[a.length];
        for (int i = 0;i< a.length;i++){
            elements[i] = F.newElement(a[i]).getImmutable() ;
        }
        return elements ;
    }
    public static Element innerProduct(Element[] a,Element[] b,Field F){
        if (a.length!=b.length){
            System.err.println("the dimension of two vector not equal");
            System.exit(1);
        }
        Element  innerProduct = F.newZeroElement().getImmutable();
        for (int i = 0;i<a.length;i++){
            innerProduct=innerProduct.add(a[i].mul(b[i])).getImmutable();
        }
        return  innerProduct ;
    }


}
