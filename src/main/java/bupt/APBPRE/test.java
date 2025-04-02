package bupt.APBPRE;

import it.unisa.dia.gas.jpbc.Element;
import org.junit.Test;
/*
public class test {
    @Test
    public void testReEncDecTwo(){
        APBPRE apbpre = new APBPRE("params/MNT159.properties");
        apbpre.setup();
        APBPREKeyPair k1 = apbpre.keyGen();
        APBPREKeyPair k2 = apbpre.keyGen();
        APBPREKeyPair k3 = apbpre.keyGen();

        Element[] psks = new Element[2];
        psks[0] =k2.pk.getImmutable();
        psks[1] =k3.pk.getImmutable();

        APBPREPath path = apbpre.createPath(k1.pk, psks);
        APBPREReEncryptionKey rk = apbpre.mPRKGen(k1.sk, path);

        APBPRECipherText ct0 = apbpre.encrypt(k1.pk, rk.omega);
        Element m1 = apbpre.decrypt1(ct0, k1.sk);
        System.out.println("dec1"+m1);

        APBPRECipherTextPrime ct1 = apbpre.reEncrypt(path, k1.pk, k2.pk, rk.rk.get(0), ct0);
        APBPRECipherTextPrime ct2 = apbpre.reEncrypt(path, k2.pk, k3.pk, rk.rk.get(1), ct1);

        Element m2 = apbpre.decrypt2(ct1, k2.sk);
        System.out.println("dec2"+m2);

        Element m3 = apbpre.decrypt2(ct2, k3.sk);
        System.out.println("dec3"+m3);
    }
}
*/