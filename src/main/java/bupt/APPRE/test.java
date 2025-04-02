package bupt.APPRE;

import it.unisa.dia.gas.jpbc.Element;
import org.junit.Test;
/*
public class test {
    @Test
    public void testReEncDecTwo(){
        APPRE appre = new APPRE("params/MNT159.properties");
        appre.setup();
        APPREKeyPair k1 = appre.keyGen();
        APPREKeyPair k2 = appre.keyGen();
        APPREKeyPair k3 = appre.keyGen();

        APPRECipherText ct0 = appre.encrypt(k1.pk);
        Element m1 = appre.decrypt1(ct0, k1.sk);
        System.out.println("dec1"+m1);
        Element[] psks = new Element[2];
        psks[0] =k2.pk.getImmutable();
        psks[1] =k3.pk.getImmutable();

        APPREPath path = appre.createPath(k1.pk, psks);
        APPREReEncryptionKey rk = appre.rKGen(k1.sk, path);

        APPRECipherTextPrime ct1 = appre.reEncrypt(path, k1.pk, k2.pk, rk.rk.get(0), ct0);
        APPRECipherTextPrime ct2 = appre.reEncrypt(path, k2.pk, k3.pk, rk.rk.get(1), ct1);

        Element m2 = appre.decrypt2(ct1, k2.sk);
        System.out.println("dec2"+m2);

        Element m3 = appre.decrypt2(ct2, k3.sk);
        System.out.println("dec3"+m3);
    }
}*/
