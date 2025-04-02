package bupt.CAPPRE;

import basic.utils.AESUtil;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class test {
    int n= 5 ;
    @Test
    public void testReEncDecTwo() throws IOException {

        Pairing pairing = PairingFactory.getPairing("params/MNT159.properties");
        CAPPRE CAPPRE = new CAPPRE(pairing);
        CAPPREMasterKey masterKey = CAPPRE.setup(5);
        String id = "id002" ;
        String id2 = "id003" ;
        CAPPRESecretKeyIBE sk_id002 = CAPPRE.registerIBE(masterKey, id);
        CAPPRESecretKeyIBE sk_id003 = CAPPRE.registerIBE(masterKey, id2);

        //randomly pick an element ‘m’ as aeskey;
        Element aesKey = pairing.getGT().newRandomElement().getImmutable();
        Path filePath1 = Paths.get("params/HL7.xml");
        byte[] originalData = Files.readAllBytes(filePath1);
        byte[] encryptedFile = AESUtil.encryptFile(aesKey, originalData);

        CAPPRECipherText ct = CAPPRE.encrypt(id,aesKey);

        Element m1 = CAPPRE.decryptIBE(sk_id002,ct);
        System.out.println("dec1"+m1);

        Element[] y = generate_x(pairing.getZr());
        Element[] x = generate_perpendicular_vector(y, pairing.getZr()) ;
        Element[] y1 = generate_x(pairing.getZr());
        Element[] x1 = generate_perpendicular_vector(y1, pairing.getZr()) ;
        Element[] y2 = generate_x(pairing.getZr());
        Element[] x2 = generate_perpendicular_vector(y2, pairing.getZr()) ;

        CAPPRESecretKeyIPE sk = CAPPRE.registerIPE(masterKey, x);
        CAPPRESecretKeyIPE sk1 = CAPPRE.registerIPE(masterKey, x1);
        CAPPRESecretKeyIPE sk2 = CAPPRE.registerIPE(masterKey, x2);

        ArrayList<Element[]> ys = new ArrayList<Element[]>();
        ys.add(y);
        ys.add(y1);
        ys.add(y2);
        CAPPREPath path = CAPPRE.createPath(id, ys);

        ArrayList<CAPPREReEncKey> rk = CAPPRE.reEncKeyGen(sk_id002, path);
        CAPPRECipherTextPrime ctp = CAPPRE.reEncrypt(ct, rk.get(0));
        CAPPRECipherTextPrime ctp1 = CAPPRE.reEncrypt(ctp, rk.get(1));
        CAPPRECipherTextPrime ctp2 = CAPPRE.reEncrypt(ctp1, rk.get(2));

        Element m2 = CAPPRE.decryptIPE(ctp, sk, x);
        byte[] decryptedData = AESUtil.decryptFile(m2, encryptedFile);

        boolean isConsistent = java.util.Arrays.equals(originalData, decryptedData);
        System.out.println("数据验证: " + (isConsistent ? "成功" : "失败"));

        Element m3 = CAPPRE.decryptIPE(ctp1, sk1, x1);
        Element m4 = CAPPRE.decryptIPE(ctp2, sk2, x2);

        System.out.println("dec2"+ m2);
        System.out.println("dec3"+m3);
        System.out.println("dec4"+m4);


    }

    Element[] generate_perpendicular_vector(Element[] vec , Field F){
        Element[] res = new Element[n];
        Element sum = F.newZeroElement().getImmutable() ;
        for(int i =0 ;i < n; i++){
            res[i] = F.newRandomElement().getImmutable() ;
            sum =sum.add(vec[i].mul(res[i])).getImmutable() ;
        }
        res[n-1] =res[n-1].add(sum.negate()).getImmutable();
        return res ;
    }
    Element[] generate_x(Field F){
        Element[] x =  new Element[n];
        for(int i =0 ;i < n; i++){
            x[i] = F.newRandomElement().getImmutable() ;
        }
        x[n-1] =F.newOneElement().getImmutable();
        return  x ;
    }
}
