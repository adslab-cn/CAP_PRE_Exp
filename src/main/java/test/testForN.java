package test;

import basic.utils.AESUtil;
import basic.utils.MyJXLUtils;
import bupt.IBET.*;
import bupt.CAPPRE.*;
import bupt.APPRE.*;
import bupt.APBPRE.*;
import bupt.Fabric.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.parameters.PropertiesParameters;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class testForN {
    //final int[] n ={5,10,15,20,25,30,35,40,45,50} ;
   // final int[] n ={1,1,2,3,4,5,6,7,8,9,10} ;
    final int[] n ={10,10,20,30,40,50,60,70,80,90,100} ;
    final int ROUND = 10;
    long[] setupT =new long[11];
    long[] keyGenT1 =new long[11];
    long[] keyGenT2 =new long[11];
    long[] encT =new long[11];
    long[] rkGenT =new long[11];
    long[] reEncT =new long[11];
    long[] dec1T =new long[11];
    long[] dec2T =new long[11];

    int[] skLength  =new int[11];
    int[] sk2Length =new int[11];
    int[] ctLength  =new int[11];
    int[] rkLength  =new int[11];
    int[] rctLength =new int[11];

    byte[] originalData = null;
    Pairing pairing =null;

    @Test
    public void testIBET(){
        String curves[]={"SS512"} ;
        //String curves[]={"SS512","SS768","SS1024"} ;
        for(String s :curves){
            //String path = "params/"+s+".properties" ;
            //Pairing pairing = PairingFactory.getPairing(path);
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(s+".properties");
            PropertiesParameters load = new PropertiesParameters().load(resourceAsStream);
            Pairing pairing = PairingFactory.getPairing(load);
            for(int i = 0 ;i< n.length;i++){

                IBET ibet = new IBET(pairing);
                IBET_MasterKey msk = null;

                long begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){msk = ibet.setup(n[i]);}
                long end = System.currentTimeMillis();
                setupT[i] = (end-begin)/ROUND ;

                String[] ids = new String[n[i]] ;
                for (int j =0;j<n[i];j++){
                    ids[j] = "id00"+(j+n[i]);
                }
                String id001 ="id001";
                String id002 ="id00"+(n[i]);

                IBET_PrivateKey sk1 =null ;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ sk1 = ibet.register(msk, id001);}
                end = System.currentTimeMillis();
                keyGenT1[i]= (end-begin)/ROUND ;

                IBET_PrivateKey sk2 = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ sk2 = ibet.register(msk, id002);}
                end = System.currentTimeMillis();
                keyGenT2[i]= (end-begin)/ROUND ;

                //get aeskey and encrypted file
                Element aesKey = pairing.getGT().newRandomElement().getImmutable();
                byte[] encryptedFile = AESUtil.encryptFile(aesKey, originalData);


                IBET_CipherText ct1 =null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ ct1 = ibet.encrypt(id001,aesKey);}
                end = System.currentTimeMillis();
                encT[i] =(end-begin)/ROUND ;

                IBET_Token rk = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){rk = ibet.authorize(sk1, ids);}
                end = System.currentTimeMillis();
                rkGenT[i] =(end-begin)/ROUND ;

                IBET_CipherTextPrime ct2 = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ct2 = ibet.transform(rk,ct1);}
                end = System.currentTimeMillis();
                reEncT[i] =(end-begin)/ROUND ;

                Element m1 =null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){m1 = ibet.decrypt1(ct1, sk1);}
                end = System.currentTimeMillis();
                dec1T[i] =(end-begin)/ROUND ;

                Element m2 = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){m2 = ibet.decrypt2(ct2, sk2, ids, id002);}
                end = System.currentTimeMillis();
                dec2T[i] =(end-begin)/ROUND ;

                byte[] decryptedData = AESUtil.decryptFile(m2, encryptedFile);
                boolean isConsistent = java.util.Arrays.equals(originalData, decryptedData);
                System.out.println("数据验证: " + (isConsistent ? "成功" : "失败"));
                //System.out.println(m1.equals(m2));

                skLength[i] =sk1.getByteLength();
                sk2Length[i] =sk2.getByteLength();
                ctLength[i] =ct1.getByteLength();
                rkLength[i] =rk.getByteLength();
                rctLength[i] =ct2.getByteLength();
            }
            MyJXLUtils.write(s+"ibetForN.xls",s,n,setupT,keyGenT1,keyGenT2,encT,rkGenT,reEncT,dec1T,dec2T,skLength,sk2Length,ctLength,rkLength,rctLength);
        }

    }
    @Test
    public void testFabric(){
        String curves[]={"MNT159"} ;
        //String curves[]={"MNT159","MNT224","BN224"} ;
        for(String s :curves){
            //String path = "params/"+s+".properties" ;
            //Pairing pairing = PairingFactory.getPairing(path);
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(s+".properties");
            PropertiesParameters load = new PropertiesParameters().load(resourceAsStream);
            Pairing pairing = PairingFactory.getPairing(load);
            for(int i = 0 ;i< n.length;i++){
                Fabric fabric = new Fabric(pairing);
                String[] attr1 =new String[n[i]];
                //String[] attr2 =new String[n[i]];
                String policy1 = "" ;
                //String policy2 = "" ;

                for (int j =0;j<n[i]-1;j++){
                    attr1[j] = "s00"+(j);
                    //attr2[j] = "s00"+(j);
                    policy1 = policy1+attr1[j]+" and " ;
                    //policy2 = policy2+attr2[j]+" and " ;
                }
                attr1[n[i]-1] = "s00"+(n[i]-1);
                //attr2[n[i]-1] = "s00"+(n[i]-1);
                policy1 = policy1+attr1[n[i]-1];
                //policy2 = policy2+attr2[n[i]-1];

                FabricMasterKey msk = null ;
                long begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ msk = fabric.setup();}
                long end = System.currentTimeMillis();
                setupT[i] = (end-begin)/ROUND ;

                FabricSecretKeyIBE sk1 = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ sk1 = fabric.keyGenIBE(msk,"id001");}
                end = System.currentTimeMillis();
                keyGenT1[i]= (end-begin)/ROUND ;

                FabricSecretKeyABE sk2 =null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ sk2 = fabric.keyGenABE(msk,attr1);}
                end = System.currentTimeMillis();
                keyGenT2[i]= (end-begin)/ROUND ;

                //get aeskey and encrypted file
                Element aesKey = pairing.getGT().newRandomElement().getImmutable();
                byte[] encryptedFile = AESUtil.encryptFile(aesKey, originalData);

                FabricCipherText ct = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ ct = fabric.encrypt("id001",aesKey);}
                end = System.currentTimeMillis();
                encT[i] =(end-begin)/ROUND ;

                FabricReEncryptionKey rk =null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ rk = fabric.reKeyGen(sk1, policy1);}
                end = System.currentTimeMillis();
                rkGenT[i] =(end-begin)/ROUND ;

                FabricCipherTextPrime ctp =null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ ctp = fabric.reEncrypt(ct, rk);}
                end = System.currentTimeMillis();
                reEncT[i] =(end-begin)/ROUND ;

                Element m1 =null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ m1 = fabric.decryptIBE(ct,sk1);}
                end = System.currentTimeMillis();
                dec1T[i] =(end-begin)/ROUND ;

                Element m2 =null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ m2 = fabric.decryptABE(ctp,sk2);}
                end = System.currentTimeMillis();
                dec2T[i] =(end-begin)/ROUND ;

                byte[] decryptedData = AESUtil.decryptFile(m2, encryptedFile);
                boolean isConsistent = java.util.Arrays.equals(originalData, decryptedData);
                System.out.println("数据验证: " + (isConsistent ? "成功" : "失败"));
                //System.out.println(m1.equals(m2));

                skLength[i] =sk1.getByteLength();
                sk2Length[i] =sk2.getByteLength();
                ctLength[i] =ct.getByteLength();
                rkLength[i] =rk.getByteLength();
                rctLength[i] =ctp.getByteLength();
            }
            MyJXLUtils.write(s+"FabricForN.xls",s,n,setupT,keyGenT1,keyGenT2,encT,rkGenT,reEncT,dec1T,dec2T,skLength,sk2Length,ctLength,rkLength,rctLength);
        }
    }
    @Test
    public void testCAPPRE(){
        String curves[]={"MNT159"} ;
        //String curves[]={"MNT159","MNT224","BN224"} ;
        for(String s :curves){
            //String path = "params/"+s+".properties" ;
            //Pairing pairing = PairingFactory.getPairing(path);
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(s+".properties");
            PropertiesParameters load = new PropertiesParameters().load(resourceAsStream);
            Pairing pairing = PairingFactory.getPairing(load);

            for (int i = 0;i<n.length;i++) {

                CAPPRE cappre = new CAPPRE(pairing);
                Element[] y = generate_x(cappre.Zr,n[i]);
                Element[] x = generate_perpendicular_vector(y, cappre.Zr,n[i]) ;
                String id = "id002" ;

                CAPPREMasterKey msk = null;
                long begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ msk = cappre.setup(n[i]);}
                long end = System.currentTimeMillis();
                setupT[i] =(end-begin)/ROUND;

                CAPPRESecretKeyIBE sk =null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ sk = cappre.registerIBE(msk, id);}
                end = System.currentTimeMillis();
                keyGenT1[i] =(end-begin)/ROUND ;

                CAPPRESecretKeyIPE skx = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ skx = cappre.registerIPE(msk, x);}
                end = System.currentTimeMillis();
                keyGenT2[i] =(end-begin)/ROUND ;


                //get aeskey and encrypted file
                Element aesKey = pairing.getGT().newRandomElement().getImmutable();
                byte[] encryptedFile = AESUtil.encryptFile(aesKey, originalData);

                CAPPRECipherText ct = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ ct = cappre.encrypt(id,aesKey);}
                end = System.currentTimeMillis();
                encT[i] =(end-begin)/ROUND ;

                ArrayList<Element[]> ys = new ArrayList<Element[]>();
                ys.add(y);
                CAPPREPath pa = cappre.createPath(id, ys);

                ArrayList<CAPPREReEncKey> rk =null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ rk = cappre.reEncKeyGen(sk, pa);}
                end = System.currentTimeMillis();
                rkGenT[i] =(end-begin)/ROUND ;

                CAPPRECipherTextPrime ctp = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ ctp = cappre.reEncrypt(ct, rk.get(0));}
                end = System.currentTimeMillis();
                reEncT[i] =(end-begin)/ROUND ;

                Element m1 =null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ m1 = cappre.decryptIBE(sk, ct);}
                end = System.currentTimeMillis();
                dec1T[i] =(end-begin)/ROUND ;

                Element m2 =null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ m2 = cappre.decryptIPE(ctp, skx, x);}
                end = System.currentTimeMillis();
                dec2T[i] =(end-begin)/ROUND ;

                byte[] decryptedData = AESUtil.decryptFile(m2, encryptedFile);
                boolean isConsistent = java.util.Arrays.equals(originalData, decryptedData);
                System.out.println("数据验证: " + (isConsistent ? "成功" : "失败"));

                System.out.println(m1.equals(m2));
                skLength[i] =sk.getByteLength();
                sk2Length[i] =skx.getByteLength();
                ctLength[i] =ct.getByteLength();
                rkLength[i] =rk.get(0).getByteLength1();
                rctLength[i] =ctp.getByteLength();
            }
            MyJXLUtils.write(s+"CAPPREForN.xls",s,n,setupT,keyGenT1,keyGenT2,encT,rkGenT,reEncT,dec1T,dec2T,skLength,sk2Length,ctLength,rkLength,rctLength);
        }
    }

    @Test
    public void  textAPPRE(){
        String curves[]={"SS512"} ;
        //String curves[]={"SS512","SS768","SS1024"} ;
        for(String s :curves){
            //String path = "params/"+s+".properties" ;
            //Pairing pairing = PairingFactory.getPairing(path);
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(s+".properties");
            PropertiesParameters load = new PropertiesParameters().load(resourceAsStream);
            Pairing pairing = PairingFactory.getPairing(load);
            for (int i = 0;i<n.length;i++) {
                APPRE appre = new APPRE(pairing);
                long begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){appre.setup();}
                long end = System.currentTimeMillis();
                setupT[i] =(end-begin)/ROUND;

                APPREKeyPair k1 =null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ k1 = appre.keyGen();}
                end = System.currentTimeMillis();
                keyGenT1[i] =(end-begin)/ROUND;

                APPREKeyPair k2 = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ k2 = appre.keyGen();}
                end = System.currentTimeMillis();
                keyGenT2[i] =(end-begin)/ROUND;

                //get aeskey and encrypted file
                Element aesKey = pairing.getGT().newRandomElement().getImmutable();
                byte[] encryptedFile = AESUtil.encryptFile(aesKey, originalData);

                APPRECipherText ct0 = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ ct0 = appre.encrypt(k1.pk,aesKey);}
                end = System.currentTimeMillis();
                encT[i] =(end-begin)/ROUND;

                Element m1 = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ m1 = appre.decrypt1(ct0, k1.sk);}
                end = System.currentTimeMillis();
                dec1T[i] =(end-begin)/ROUND;

                Element[] psks = new Element[1];
                psks[0] =k2.pk.getImmutable();

                APPREPath pa = appre.createPath(k1.pk, psks);

                APPREReEncryptionKey rk = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND*n[i];round++){ rk = appre.rKGen(k1.sk, pa);}
                end = System.currentTimeMillis();
                rkGenT[i] =((end-begin)/ROUND);

                APPRECipherTextPrime ct1 = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND*n[i];round++){ ct1 = appre.reEncrypt(pa, k1.pk, k2.pk, rk.rk.get(0), ct0);}
                end = System.currentTimeMillis();
                reEncT[i] =((end-begin)/ROUND);

                Element m2 = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ m2 = appre.decrypt2(ct1, k2.sk);}
                end = System.currentTimeMillis();
                dec2T[i] =(end-begin)/ROUND;

                byte[] decryptedData = AESUtil.decryptFile(m2, encryptedFile);
                boolean isConsistent = java.util.Arrays.equals(originalData, decryptedData);
                System.out.println("数据验证: " + (isConsistent ? "成功" : "失败"));
                //System.out.println(m1.equals(m2));

                skLength[i] =k1.getByteLength();
                sk2Length[i] =k2.getByteLength();
                ctLength[i] =ct0.getByteLength();
                rkLength[i] =rk.getByteLength()*n[i];
                rctLength[i] =ct1.getByteLength()*n[i];
            }
            MyJXLUtils.write(s+"APPREForN.xls",s,n,setupT,keyGenT1,keyGenT2,encT,rkGenT,reEncT,dec1T,dec2T,skLength,sk2Length,ctLength,rkLength,rctLength);
        }
    }

    @Test
    public void  textAPBPRE(){
        String curves[]={"SS512"} ;
        //String curves[]={"SS512","SS768","SS1024"} ;
        for(String s :curves){
            // String path = "params/"+s+".properties" ;
            // Pairing pairing = PairingFactory.getPairing(path);
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(s+".properties");
            PropertiesParameters load = new PropertiesParameters().load(resourceAsStream);
            Pairing pairing = PairingFactory.getPairing(load);

            for (int i = 0;i<n.length;i++) {
                APBPRE apbpre = new APBPRE(pairing);
                long begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){apbpre.setup();}
                long end = System.currentTimeMillis();
                setupT[i] =(end-begin)/ROUND;

                APBPREKeyPair k1 = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ k1 = apbpre.keyGen();}
                end = System.currentTimeMillis();
                keyGenT1[i] =(end-begin)/ROUND;

                APBPREKeyPair k2 = null ;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ k2 = apbpre.keyGen();}
                end = System.currentTimeMillis();
                keyGenT2[i] =(end-begin)/ROUND;

                Element[] psks = new Element[1];
                psks[0] =k2.pk.getImmutable();
                APBPREPath pa = apbpre.createPath(k1.pk, psks);

                APBPREReEncryptionKey rk = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND*n[i];round++){ rk = apbpre.mPRKGen(k1.sk, pa);}
                end = System.currentTimeMillis();
                rkGenT[i] =((end-begin)/ROUND);

                //get aeskey and encrypted file
                Element aesKey = pairing.getGT().newRandomElement().getImmutable();
                byte[] encryptedFile = AESUtil.encryptFile(aesKey, originalData);

                APBPRECipherText ct0 = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ ct0 = apbpre.encrypt(k1.pk,rk.omega,aesKey);}
                end = System.currentTimeMillis();
                encT[i] =(end-begin)/ROUND;

                Element m1 =null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ m1 = apbpre.decrypt1(ct0, k1.sk);}
                end = System.currentTimeMillis();
                dec1T[i] =(end-begin)/ROUND;

                APBPRECipherTextPrime ct1 = null;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND*n[i];round++){ ct1 = apbpre.reEncrypt(pa, k1.pk, k2.pk, rk.rk.get(0), ct0);}
                end = System.currentTimeMillis();
                reEncT[i] =((end-begin)/ROUND);

                Element m2 = null ;
                begin = System.currentTimeMillis();
                for (int round = 0; round<ROUND;round++){ m2 = apbpre.decrypt2(ct1, k2.sk);}
                end = System.currentTimeMillis();
                dec2T[i] =(end-begin)/ROUND;

                byte[] decryptedData = AESUtil.decryptFile(m2, encryptedFile);
                boolean isConsistent = java.util.Arrays.equals(originalData, decryptedData);
                System.out.println("数据验证: " + (isConsistent ? "成功" : "失败"));
                //System.out.println(m1.equals(m2));

                skLength[i] =k1.getByteLength();
                sk2Length[i] =k2.getByteLength();
                ctLength[i] =ct0.getByteLength();
                rkLength[i] =rk.getByteLength()*n[i];
                rctLength[i] =ct1.getByteLength()*n[i];
            }
            MyJXLUtils.write(s+"APBPREForN.xls",s,n,setupT,keyGenT1,keyGenT2,encT,rkGenT,reEncT,dec1T,dec2T,skLength,sk2Length,ctLength,rkLength,rctLength);
        }
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
    Element[] generate_x(Field F,int n){
        Element[] x =  new Element[n];
        for(int i =0 ;i < n; i++){
            x[i] = F.newRandomElement().getImmutable() ;
        }
        x[n-1] =F.newOneElement().getImmutable();
        return  x ;
    }

    @Test
    public void testAll() throws IOException {
        InputStream rs = this.getClass().getClassLoader().getResourceAsStream("HL7.xml");
        originalData = rs.readAllBytes();
        testIBET();
        textAPBPRE();
        testFabric();
        testCAPPRE();
        textAPPRE();
    }

}
