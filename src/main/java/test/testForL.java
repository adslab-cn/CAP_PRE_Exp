package test;

import basic.utils.AESUtil;
import bupt.APPRE.*;
import bupt.CAPPRE.*;
import bupt.APBPRE.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.parameters.PropertiesParameters;
import org.junit.Test;
import basic.utils.MyJXLUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class testForL {
    final int n =10 ;
    final int[] l ={1,1,2,3,4,5,6,7,8,9,10} ;
    final int ROUND = 3 ;
    long[] setupT =new long[11];
    long[] keyGenT1 =new long[11];
    long[] keyGenT2 =new long[11];
    long[] encT =new long[11];
    long[] rkGenT =new long[11];
    long[] reEncT1 =new long[11];
    long[] reEncT2 =new long[11];
    long[] dec1T =new long[11];
    long[] dec2T =new long[11];
    int[] skLength  =new int[11];
    int[] sk2Length =new int[11];
    int[] ctLength  =new int[11];
    int[] rkLength  =new int[11];
    int[] rctLength =new int[11];

    byte[] originalData = null;


    @Test
    public void testCAPPRE(){
        String curves[]={"MNT159"} ;
        //String curves[]={"MNT159","MNT224","BN224"} ;
        for(String s :curves){
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(s+".properties");
            PropertiesParameters load = new PropertiesParameters().load(resourceAsStream);
            Pairing pairing = PairingFactory.getPairing(load);
            //String path = "params/"+s+".properties" ;
            //Pairing pairing = PairingFactory.getPairing(path);

            for (int i = 0;i<l.length;i++) {
                CAPPRE cappre = new CAPPRE(pairing);
                ArrayList<Element[]> ys = new ArrayList<Element[]>();
                ArrayList<Element[]> xs = new ArrayList<Element[]>();
                for (int j = 0; j < l[i] ;j++){
                    Element[] y = generate_x(cappre.Zr,n);
                    Element[] x = generate_perpendicular_vector(y, cappre.Zr,n) ;
                    ys.add(y);
                    xs.add(x);
                }
                String id = "id002" ;

                CAPPREMasterKey msk = null;
                long begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ msk = cappre.setup(n);}
                long end = System.currentTimeMillis();
                setupT[i] =(end -begin)/ROUND;

                CAPPRESecretKeyIBE sk = null ;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ sk = cappre.registerIBE(msk, id);}
                end = System.currentTimeMillis();
                keyGenT1[i] =(end -begin)/ROUND ;

                CAPPRESecretKeyIPE skx = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ skx = cappre.registerIPE(msk, xs.get(0));}
                end = System.currentTimeMillis();
                keyGenT2[i] =(end -begin)/ROUND ;

                //get aeskey and encrypted file
                Element aesKey = pairing.getGT().newRandomElement().getImmutable();
                byte[] encryptedFile = AESUtil.encryptFile(aesKey, originalData);

                CAPPRECipherText ct = null ;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ ct = cappre.encrypt(id,aesKey);}
                end = System.currentTimeMillis();

                encT[i] =(end -begin)/ROUND ;

                CAPPREPath pa = cappre.createPath(id, ys);

                ArrayList<CAPPREReEncKey> rk = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ rk = cappre.reEncKeyGen(sk, pa);}
                end = System.currentTimeMillis();
                rkGenT[i] =(end -begin)/ROUND ;

                CAPPRECipherTextPrime ctp = null ;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ ctp = cappre.reEncrypt(ct, rk.get(0));}
                end = System.currentTimeMillis();
                reEncT1[i] =(end -begin)/ROUND ;

                Element m1 = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ m1 = cappre.decryptIBE(sk, ct);}
                end = System.currentTimeMillis();
                dec1T[i] =(end -begin)/ROUND ;

                Element m2 = null ;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ m2 = cappre.decryptIPE(ctp, skx, xs.get(0));}
                end = System.currentTimeMillis();
                dec2T[i] =(end -begin)/ROUND ;

                byte[] decryptedData = AESUtil.decryptFile(m2, encryptedFile);
                boolean isConsistent = java.util.Arrays.equals(originalData, decryptedData);
                System.out.println("数据验证: " + (isConsistent ? "成功" : "失败"));

                skLength[i] =sk.getByteLength();
                sk2Length[i] =skx.getByteLength();
                ctLength[i] =ct.getByteLength();
                rkLength[i] = 0 ;
                for ( int k = 0 ; k < rk.size(); k++  ){
                    if (k == 0){
                        rkLength[i] += rk.get(k).getByteLength1();
                    }else {
                        rkLength[i] += rk.get(k).getByteLength2();
                    }
                }
                rctLength[i] =ctp.getByteLength();
            }
            MyJXLUtils.write(s+"CAPPREForL.xls",s,l,setupT,keyGenT1,keyGenT2,encT,rkGenT,reEncT1,dec1T,dec2T,skLength,sk2Length,ctLength,rkLength,rctLength);
        }
    }

    @Test
    public void  textAPPRE(){
        String curves[]={"SS512"} ;
        //String curves[]={"SS512","SS768","SS1024"} ;
        for(String s :curves){
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(s+".properties");
            PropertiesParameters load = new PropertiesParameters().load(resourceAsStream);
            Pairing pairing = PairingFactory.getPairing(load);
           // String path = "params/"+s+".properties" ;
           // Pairing pairing = PairingFactory.getPairing(path);

            for (int i = 0;i<l.length;i++) {
                APPRE appre = new APPRE(pairing);

                long begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){appre.setup();}
                long end = System.currentTimeMillis();
                setupT[i] =(end -begin)/ROUND;

                APPREKeyPair k1 = null ;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ k1 = appre.keyGen();}
                end = System.currentTimeMillis();
                keyGenT1[i] =(end -begin)/ROUND;

                APPREKeyPair k2 = null ;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ k2 = appre.keyGen();}
                end = System.currentTimeMillis();
                keyGenT2[i] =(end -begin)/ROUND;

                //get aeskey and encrypted file
                Element aesKey = pairing.getGT().newRandomElement().getImmutable();
                byte[] encryptedFile = AESUtil.encryptFile(aesKey, originalData);

                APPRECipherText ct0 = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ ct0 = appre.encrypt(k1.pk,aesKey);}
                end = System.currentTimeMillis();
                encT[i] =(end -begin)/ROUND;

                Element m1 = null ;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ m1 = appre.decrypt1(ct0, k1.sk);}
                end = System.currentTimeMillis();
                dec1T[i] =(end -begin)/ROUND;

                Element[] psks = new Element[l[i]];
                psks[0] =k2.pk.getImmutable();
                for (int j = 1; j < l[i] ;j++){
                    psks[j] =appre.keyGen().pk.getImmutable();
                }

                APPREPath pa = appre.createPath(k1.pk, psks);

                APPREReEncryptionKey rk = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND*n;round++){ rk = appre.rKGen(k1.sk, pa);}
                end = System.currentTimeMillis();
                rkGenT[i] =(end -begin)/ROUND;

                APPRECipherTextPrime ct1 = null ;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND*n;round++){ct1 = appre.reEncrypt(pa, k1.pk, k2.pk, rk.rk.get(0), ct0);}
                end = System.currentTimeMillis();
                reEncT1[i] =(end -begin)/ROUND;

                Element m2 = null ;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ m2 = appre.decrypt2(ct1, k2.sk);}
                end = System.currentTimeMillis();
                dec2T[i] =(end -begin)/ROUND;

                byte[] decryptedData = AESUtil.decryptFile(m2, encryptedFile);
                boolean isConsistent = java.util.Arrays.equals(originalData, decryptedData);
                System.out.println("数据验证: " + (isConsistent ? "成功" : "失败"));

                //System.out.println(m1.equals(m2));
                skLength[i] =k1.getByteLength();
                sk2Length[i] =k2.getByteLength();
                ctLength[i] =ct0.getByteLength();
                rkLength[i] =rk.getByteLength()*n;
                rctLength[i] =ct1.getByteLength()*n;
            }
            MyJXLUtils.write(s+"APPREForL.xls",s,l,setupT,keyGenT1,keyGenT2,encT,rkGenT,reEncT1,dec1T,dec2T,skLength,sk2Length,ctLength,rkLength,rctLength);
        }
    }

    @Test
    public void  textAPBPRE(){
        String curves[]={"SS512"} ;
        //String curves[]={"SS512","SS768","SS1024"} ;
        for(String s :curves){
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(s+".properties");
            PropertiesParameters load = new PropertiesParameters().load(resourceAsStream);
            Pairing pairing = PairingFactory.getPairing(load);
            //String path = "params/"+s+".properties" ;
            //Pairing pairing = PairingFactory.getPairing(path);

            for (int i = 0;i<l.length;i++) {
                APBPRE apbpre = new APBPRE(pairing);
                long begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){apbpre.setup();}
                long end = System.currentTimeMillis();
                setupT[i] =(end -begin)/ROUND;

                APBPREKeyPair k1 = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ k1 = apbpre.keyGen();}
                end = System.currentTimeMillis();
                keyGenT1[i] =(end -begin)/ROUND;

                APBPREKeyPair k2 =null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ k2 = apbpre.keyGen();}
                end = System.currentTimeMillis();
                keyGenT2[i] =(end -begin)/ROUND;

                Element[] psks = new Element[l[i]];
                psks[0] =k2.pk.getImmutable();
                for (int j = 1; j < l[i] ;j++){
                    psks[j] =apbpre.keyGen().pk.getImmutable();
                }

                APBPREPath pa = apbpre.createPath(k1.pk, psks);

                APBPREReEncryptionKey rk = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND*n;round++){ rk = apbpre.mPRKGen(k1.sk, pa);}
                end = System.currentTimeMillis();
                rkGenT[i] =(end -begin)/ROUND;

                //get aeskey and encrypted file
                Element aesKey = pairing.getGT().newRandomElement().getImmutable();
                byte[] encryptedFile = AESUtil.encryptFile(aesKey, originalData);

                APBPRECipherText ct0 = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ ct0 = apbpre.encrypt(k1.pk,rk.omega,aesKey);}
                end = System.currentTimeMillis();
                encT[i] =(end -begin)/ROUND;

                Element m1 = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ m1 = apbpre.decrypt1(ct0, k1.sk);}
                end = System.currentTimeMillis();
                dec1T[i] =(end -begin)/ROUND;

                APBPRECipherTextPrime ct1 = null ;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND*n;round++){ ct1 = apbpre.reEncrypt(pa, k1.pk, k2.pk, rk.rk.get(0), ct0);}
                end = System.currentTimeMillis();
                reEncT1[i] =(end -begin)/ROUND;

                Element m2 = null ;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ m2 = apbpre.decrypt2(ct1, k2.sk);}
                end = System.currentTimeMillis();
                dec2T[i] =(end -begin)/ROUND;

                //System.out.println(m1.equals(m2));
                byte[] decryptedData = AESUtil.decryptFile(m2, encryptedFile);
                boolean isConsistent = java.util.Arrays.equals(originalData, decryptedData);
                System.out.println("数据验证: " + (isConsistent ? "成功" : "失败"));

                skLength[i] =k1.getByteLength();
                sk2Length[i] =k2.getByteLength();
                ctLength[i] =ct0.getByteLength();
                rkLength[i] =rk.getByteLength()*n;
                rctLength[i] =ct1.getByteLength()*n;
            }
            MyJXLUtils.write(s+"APBPREForL.xls",s,l,setupT,keyGenT1,keyGenT2,encT,rkGenT,reEncT1,dec1T,dec2T,skLength,sk2Length,ctLength,rkLength,rctLength);
        }
    }

    @Test
    public void testAll() throws IOException {
        InputStream rs = this.getClass().getClassLoader().getResourceAsStream("HL7.xml");
        originalData = rs.readAllBytes();
        //originalData = Files.readAllBytes(Paths.get("params/HL7.xml"));
        testCAPPRE();
        textAPBPRE();
        textAPPRE();
    }

    Element[] generate_perpendicular_vector(Element[] vec , Field F, int n){
        Element[] res = new Element[n];
        Element sum = F.newZeroElement().getImmutable() ;
        for(int i =0 ;i < n; i++){
            res[i] = F.newRandomElement().getImmutable() ;
            sum =sum.add(vec[i].mul(res[i])).getImmutable() ;
        }
        res[n-1] =res[n-1].add(sum.negate()).getImmutable();
        return res ;
    }
    Element[] generate_x(Field F, int n){
        Element[] x =  new Element[n];
        for(int i =0 ;i < n; i++){
            x[i] = F.newRandomElement().getImmutable() ;
        }
        x[n-1] =F.newOneElement().getImmutable();
        return  x ;
    }


}
