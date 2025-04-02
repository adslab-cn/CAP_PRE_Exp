package bupt.Fabric;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fabric {
    private Pairing curveParams;
    private Field G, H, GT, Zr;
    private FabricPublicKey pk;
    public Fabric(Pairing curveParams) {
        this.curveParams = curveParams;
        G = curveParams.getG1();
        H = curveParams.getG2();
        GT = curveParams.getGT();
        Zr = curveParams.getZr();
    }
    public FabricMasterKey setup(){
        Element g = G.newRandomElement().getImmutable();
        Element h = H.newRandomElement().getImmutable();
        Element d1 = Zr.newRandomElement().getImmutable();
        Element d2 = Zr.newRandomElement().getImmutable();
        Element d3 = Zr.newRandomElement().getImmutable();
        Element[] gdN =new Element[3];
        gdN[0]  = g.powZn(d1).getImmutable();
        gdN[1]  = g.powZn(d2).getImmutable();
        gdN[2]  = g.powZn(d3).getImmutable();

        Element[] aN =new Element[2];
        aN[0] = Zr.newRandomElement().getImmutable();
        aN[1] = Zr.newRandomElement().getImmutable();
        Element b1 = Zr.newRandomElement().getImmutable();
        Element b2 = Zr.newRandomElement().getImmutable();

        Element H1 = h.powZn(aN[0]).getImmutable();
        Element H2 = h.powZn(aN[1]).getImmutable();
        Element G1 = g.powZn(aN[0]).getImmutable();
        Element G2 = g.powZn(aN[1]).getImmutable();
        Element egh = curveParams.pairing(g, h).getImmutable();
        Element T1 = egh.powZn(d1.mul(aN[0]).add(d3)).getImmutable();
        Element T2 = egh.powZn(d2.mul(aN[1]).add(d3)).getImmutable();
        pk = new FabricPublicKey(g,h,H1,H2,G1,G2,T1,T2);
        return new FabricMasterKey(gdN,aN,b1,b2);
    }
    public FabricSecretKeyIBE keyGenIBE(FabricMasterKey msk, String id){
        Element r1 = Zr.newRandomElement().getImmutable();
        Element r2 = Zr.newRandomElement().getImmutable();
        Element sigma = Zr.newRandomElement().getImmutable();
        Element[] isk0 = new Element[3];
        isk0[0] = pk.h.powZn(msk.b1.mul(r1)).getImmutable() ;
        isk0[1] = pk.h.powZn(msk.b2.mul(r2)).getImmutable() ;
        isk0[2] = pk.h.powZn(r1.add(r2)).getImmutable() ;

        Element[] isk1 = new Element[3];
        for (int t =0; t < 2; t++) {
            String input_for_hash1 = (t+1) + "1" + id +"0";
            String input_for_hash2 = (t+1) + "2" + id +"0";
            String input_for_hash3 = (t+1) + "3" + id +"0";
            Element hash1 = pk.HashH(input_for_hash1.getBytes(), G).getImmutable();
            Element hash2 = pk.HashH(input_for_hash2.getBytes(), G).getImmutable();
            Element hash3 = pk.HashH(input_for_hash3.getBytes(), G).getImmutable();
            Element tmp1 = hash1.powZn(msk.b1.mul(r1).div(msk.aN[t])).getImmutable();
            Element tmp2 = hash2.powZn(msk.b2.mul(r2).div(msk.aN[t])).getImmutable();
            Element tmp3 = hash3.powZn((r1.add(r2)).div(msk.aN[t])).getImmutable();
            isk1[t] = tmp1.mul(tmp2).mul(tmp3).mul(msk.gdN[t]).mul(pk.g.powZn(sigma.div(msk.aN[t]))).getImmutable();
        }
        isk1[2] = msk.gdN[2].mul(pk.g.powZn(sigma.negate())).getImmutable();
        return new FabricSecretKeyIBE(isk0,isk1);
    }
    public FabricSecretKeyABE keyGenABE(FabricMasterKey msk, String[] S){
        Element r1p = Zr.newRandomElement().getImmutable();
        Element r2p = Zr.newRandomElement().getImmutable();
        Element sigma_p = Zr.newRandomElement().getImmutable();
        Element[] sk0 = new Element[3];
        sk0[0] = pk.h.powZn(msk.b1.mul(r1p)).getImmutable() ;
        sk0[1] = pk.h.powZn(msk.b2.mul(r2p)).getImmutable() ;
        sk0[2] = pk.h.powZn(r1p.add(r2p)).getImmutable() ;

        Map<String, Element[]> map = new HashMap<>();
        for(String y : S){
            Element sigma_y = Zr.newRandomElement().getImmutable();
            Element[] sky= new Element[3];
            for (int t = 0; t<2; t++){
                String input_for_hash1 = (t+1)+"1"+y;
                String input_for_hash2 = (t+1)+"2"+y;
                String input_for_hash3 = (t+1)+"3"+y;
                Element hash1 = pk.HashH(input_for_hash1.getBytes(), G).getImmutable();
                Element hash2 = pk.HashH(input_for_hash2.getBytes(), G).getImmutable();
                Element hash3 = pk.HashH(input_for_hash3.getBytes(), G).getImmutable();
                Element tmp1 = hash1.powZn(msk.b1.mul(r1p).div(msk.aN[t])).getImmutable();
                Element tmp2 = hash2.powZn(msk.b2.mul(r2p).div(msk.aN[t])).getImmutable();
                Element tmp3 = hash3.powZn((r1p.add(r2p)).div(msk.aN[t])).getImmutable();
                sky[t] =tmp1.mul(tmp2).mul(tmp3).mul(pk.g.powZn(sigma_y.div(msk.aN[t]))).getImmutable();
            }
            sky[2] =pk.g.powZn(sigma_y.negate()).getImmutable();
            map.put(y,sky);
        }
        Element[] skp = new Element[3] ;
        for (int t = 0; t<2; t++) {
            //t=1
            String input_for_hash1 = (t+1) + "110";
            String input_for_hash2 = (t+1) + "210";
            String input_for_hash3 = (t+1) + "310";
            Element hash1 = pk.HashH(input_for_hash1.getBytes(), G).getImmutable();
            Element hash2 = pk.HashH(input_for_hash2.getBytes(), G).getImmutable();
            Element hash3 = pk.HashH(input_for_hash3.getBytes(), G).getImmutable();
            Element tmp1 = hash1.powZn(msk.b1.mul(r1p).div(msk.aN[t])).getImmutable();
            Element tmp2 = hash2.powZn(msk.b2.mul(r2p).div(msk.aN[t])).getImmutable();
            Element tmp3 = hash3.powZn((r1p.add(r2p)).div(msk.aN[t])).getImmutable();
            skp[t] = tmp1.mul(tmp2).mul(tmp3).mul(msk.gdN[t]).mul(pk.g.powZn(sigma_p.div(msk.aN[t]))).getImmutable();
        }
        skp[2] = msk.gdN[2].mul(pk.g.powZn(sigma_p.negate())).getImmutable();
        return new FabricSecretKeyABE(sk0,map,skp,S);
    }
    public FabricReEncryptionKey reKeyGen(FabricSecretKeyIBE isk, String policy){
        Map<String, int[]> msp = MSP.convert_policy_to_msp(policy);
        Element s1p = Zr.newRandomElement().getImmutable();
        Element s2p = Zr.newRandomElement().getImmutable();
        Element k1 = Zr.newRandomElement().getImmutable();
        Element k2 = Zr.newRandomElement().getImmutable();
        Element k3 = Zr.newRandomElement().getImmutable();
        Element[] rk0 = new Element[3] ;
        rk0[0] = pk.H1.powZn(s1p).getImmutable();
        rk0[1] = pk.H2.powZn(s2p).getImmutable();
        rk0[2] = pk.h.powZn(s1p.add(s2p)).getImmutable();

        Map<String, ArrayList<Element>> rk1 = new HashMap<>();
        for (Map.Entry<String, int []> entry : msp.entrySet()){
            String attr = entry.getKey();
            int [] row = entry.getValue();
            ArrayList<Element> rk1il = new ArrayList<>();
            for (int l=0; l<3; l++) {
                int cols = row.length;
                String input_for_hash1 = "1" + (l+1) + attr;
                String input_for_hash2 = "2" + (l+1) + attr;
                Element h1 = pk.HashH(input_for_hash1.getBytes(), G).getImmutable();
                Element h2 = pk.HashH(input_for_hash2.getBytes(), G).getImmutable();
                Element prod = h1.powZn(s1p).mul(h2.powZn(s2p)).getImmutable();

                Element prod1 = G.newOneElement().getImmutable();
                for (int j=0; j<cols; j++) {
                    Element rowj = Zr.newElement(row[j]);

                    String input_for_hash3 = "1" + (l+1) + String.valueOf(j+1)+"0";
                    String input_for_hash4 = "2" + (l+1) + String.valueOf(j+1)+"0";
                    Element h3 = pk.HashH(input_for_hash3.getBytes(), G).getImmutable();
                    Element h4 = pk.HashH(input_for_hash4.getBytes(), G).getImmutable();
                    prod1 =prod1.mul(h3.powZn(s1p.mul(rowj)).mul(h4.powZn(s2p.mul(rowj)))).getImmutable();
                }
                prod = prod.mul(prod1).getImmutable();
                rk1il.add(prod);
            }
            rk1.put(attr, rk1il);
        }
        Element input_for_F = pk.T1.powZn(s1p).mul(pk.T2.powZn(s2p)).getImmutable();
        Element[] eta = pk.HashF(input_for_F, H);
        Element[] rk2 = new Element[3];
        rk2[0] = eta[0].mul(pk.h.powZn(k1)).getImmutable();
        rk2[1] = eta[1].mul(pk.h.powZn(k2)).getImmutable();
        rk2[2] = eta[2].mul(pk.h.powZn(k3)).getImmutable();

        Element[] rk3 = new Element[3];
        rk3[0] = isk.isk1[0].mul(pk.g.powZn(k1));
        rk3[1] = isk.isk1[1].mul(pk.g.powZn(k2));
        rk3[2] = isk.isk1[2].mul(pk.g.powZn(k3));

        Element[] rk4 = isk.isk0;

        return new FabricReEncryptionKey(rk0,rk1,rk2,rk3,rk4,policy);

    }
    public FabricCipherText encrypt(String id,Element msg ){
        //Element msg = GT.newRandomElement().getImmutable();
        //System.out.println("message:"+msg);

        Element s1 = Zr.newRandomElement().getImmutable();
        Element s2 = Zr.newRandomElement().getImmutable();
        Element c0 = msg.mul(pk.T1.powZn(s1)).mul(pk.T2.powZn(s2)).getImmutable();

        Element[] c1 = new Element[3];
        Element[] c2 = new Element[3];
        Element[] c3 = new Element[3];

        c1[0] = pk.H1.powZn(s1).getImmutable();
        c1[1] = pk.H2.powZn(s2).getImmutable();
        c1[2] = pk.h.powZn(s1.add(s2)).getImmutable();

        c2[0] = pk.G1.powZn(s1).getImmutable();
        c2[1] = pk.G2.powZn(s2).getImmutable();
        c2[2] = pk.g.powZn(s1.add(s2)).getImmutable();

        for (int l = 0; l< 3; l++){
            String input_for_hash1 = "1"+(l+1)+id+"0";
            String input_for_hash2 = "2"+(l+1)+id+"0";
            Element h1 = pk.HashH(input_for_hash1.getBytes(), G).getImmutable();
            Element h2 = pk.HashH(input_for_hash2.getBytes(), G).getImmutable();
            c3[l] = h1.powZn(s1).mul(h2.powZn(s2)).getImmutable();
        }
        return new FabricCipherText(c0,c1,c2,c3);
    }
    public FabricCipherTextPrime reEncrypt(FabricCipherText ct,FabricReEncryptionKey rk){
        Element prod1 = GT.newOneElement().getImmutable();
        Element prod2 = GT.newOneElement().getImmutable();
        for (int i =0 ; i<3 ;i++){
            prod1 = prod1.mul(curveParams.pairing(rk.rk3[i],ct.c1[i])).getImmutable();
            prod2 = prod2.mul(curveParams.pairing(ct.c3[i],rk.rk4[i])).getImmutable();
        }
        Element RC = prod1.div(prod2).getImmutable();
        Element c0p = ct.c0.div(RC).getImmutable();
        return new FabricCipherTextPrime(c0p, ct.c2, rk.rk0, rk.rk1, rk.rk2,rk.policy);
    }
    public Element decryptIBE(FabricCipherText ct, FabricSecretKeyIBE isk){
        Element prod1 = GT.newOneElement();
        Element prod2 = GT.newOneElement();
        for (int i =0 ; i<3 ;i++){
             prod1.mul(curveParams.pairing(ct.c3[i],isk.isk0[i]));
             prod2.mul(curveParams.pairing(isk.isk1[i],ct.c1[i]));
        }
        Element m = ct.c0.mul(prod1.div(prod2)).getImmutable();
        return m;
    }
    public Element decryptABE(FabricCipherTextPrime ctp, FabricSecretKeyABE sk){
        for (String attr : ctp.c3p.keySet()) {
            if (!sk.sky.containsKey(attr)) {
                System.err.println("Policy not satisfied. ("+attr+")");
                System.exit(2);
            }
        }

        Element D1 = GT.newOneElement().getImmutable();
        Element D2 = GT.newOneElement().getImmutable();
        for (int i=0; i<3; i++) {
            Element prod_H = G.newOneElement().getImmutable();
            Element prod_G = G.newOneElement().getImmutable();
            for (String node : ctp.c3p.keySet()) {
                String attr = node;             // will be useful if MSP is complete
                prod_H = prod_H.mul(sk.sky.get(attr)[i]).getImmutable();
                prod_G = prod_G.mul(ctp.c3p.get(attr).get(i)).getImmutable();
                //System.out.println("attr_stripped=\""+attr_stripped+"\", i="+i);
            }
            Element kp_prodH = sk.skp[i].mul(prod_H).getImmutable();

            D1 = D1.mul(curveParams.pairing(prod_G, sk.sk0[i])).getImmutable();
            D2 = D2.mul(curveParams.pairing(kp_prodH, ctp.c2p[i])).getImmutable();
        }
        Element tmp = D2.div(D1).duplicate().getImmutable();
        Element[] eta = pk.HashF(tmp, H);
        Element prod = GT.newOneElement().getImmutable();
        for (int i = 0; i < 3; i++){
            prod = prod.mul(curveParams.pairing(ctp.c1p[i],ctp.c4p[i].div(eta[i]))).getImmutable();
        }
        Element msg = ctp.c0p.mul(prod).getImmutable();
        return msg ;
    }

}
