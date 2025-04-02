package bupt.CAPPRE;


import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.util.ArrayList;


public class CAPPRE {
    private Pairing curveParams;
    private Field G;
    private Field H;
    private Field GT;
    public Field Zr;
    private CAPPREPublicKey pk ;
    int n;

    public CAPPRE( Pairing curveParams) {
        this.curveParams = curveParams;
        G = curveParams.getG1();
        H = curveParams.getG2();
        GT = curveParams.getGT();
        Zr = curveParams.getZr();
    }

    public CAPPREMasterKey setup(int n){
        this.n =  n;
        Element g = G.newRandomElement().getImmutable();
        Element h = H.newRandomElement().getImmutable();

        Element alpha = Zr.newRandomElement().getImmutable();
        Element beta = Zr.newRandomElement().getImmutable();
        Element gamma = Zr.newRandomElement().getImmutable();
        Element delta = Zr.newRandomElement().getImmutable();
        Element z= Zr.newRandomElement().getImmutable();
        Element w= Zr.newRandomElement().getImmutable();

        Element[] gi = new  Element[n] ;
        Element[] hi = new  Element[n] ;

        for (int i = 0; i < n ;i++){
            Element a_i = Zr.newRandomElement().getImmutable();
            gi[i] = g.powZn(a_i).getImmutable() ;
            hi[i] = h.powZn(a_i).getImmutable() ;
        }

        Element g_bar = g.powZn(alpha).getImmutable();
        Element h_bar = h.powZn(alpha).getImmutable();

        Element g_til = g.powZn(delta).getImmutable();
        Element h_til = h.powZn(delta).getImmutable();

        Element h0 = h.powZn(gamma).getImmutable();
        Element g0 = g.powZn(gamma).getImmutable();

        Element g_hat = g.powZn(z).getImmutable();
        Element h_hat = h.powZn(alpha.mul(beta)).getImmutable();

        Element u = g.powZn(w).getImmutable();
        Element v = h.powZn(w).getImmutable();
        Element Y = curveParams.pairing(g,h_hat).getImmutable() ;
        pk = new CAPPREPublicKey(g,g_bar,g_til,g0,gi,g_hat,h,h_bar,h_til,u,v,Y);

        return new CAPPREMasterKey(h_hat,h0,hi);
    }
    public CAPPRESecretKeyIBE registerIBE(CAPPREMasterKey msk, String id){
        Element ID = Zr.newElementFromBytes(id.getBytes());
        Element s = Zr.newRandomElement().getImmutable();
        Element sk0 = msk.h_hat.mul(pk.h_bar.powZn(ID).mul(pk.h_til).powZn(s)).getImmutable();
        Element sk1 = pk.h.powZn(s).getImmutable();
        return new CAPPRESecretKeyIBE(sk0,sk1);
    }
    public CAPPRESecretKeyIPE registerIPE(CAPPREMasterKey msk, Element[] xi){
        Element t0 = Zr.newRandomElement().getImmutable();
        Element t1 = Zr.newRandomElement().getImmutable();
        Element pi = H.newOneElement().getImmutable();

        for (int i = 0; i < n; i++){
            pi = pi.mul(msk.hi[i].powZn(xi[i].mul(t1))).getImmutable();
        }
        Element K0 = pk.h.powZn(t0).getImmutable() ;
        Element K1 = pk.h.powZn(t1).getImmutable() ;
        Element K2= msk.h_hat.mul(msk.h_0.powZn(t0)).mul(pi).getImmutable();
        return new CAPPRESecretKeyIPE(K0,K1,K2) ;
    }
    public CAPPREPath createPath(String ID, ArrayList<Element[]> ys){
        return new CAPPREPath(ID,ys) ;
    }

    public CAPPRECipherText encrypt(String id, Element m){

        Element ID = Zr.newElementFromBytes(id.getBytes());

        // Element m =GT.newElementFromBytes(message);
        //Element m = GT.newRandomElement().getImmutable();
        //System.out.println("message:"+ m);

        Element r0 = Zr.newRandomElement().getImmutable();
        Element C = m.mul(pk.Y.powZn(r0)).getImmutable();
        Element C0 = pk.g.powZn(r0).getImmutable();
        Element C1 = pk.g_bar.powZn(ID).mul(pk.g_til).powZn(r0).getImmutable();
        Element C2 = pk.u.powZn(r0).getImmutable();

        return new CAPPRECipherText(C,C0,C1,C2);
    }
    public ArrayList<CAPPREReEncKey> reEncKeyGen(CAPPRESecretKeyIBE skid, CAPPREPath Pa){
        ArrayList<CAPPREReEncKey> rk = new ArrayList<CAPPREReEncKey>();
        // j = 1
        Element r1 = Zr.newRandomElement().getImmutable();
        Element sigma1 = Zr.newRandomElement().getImmutable();
        Element[] y1 = Pa.ys.get(0);
        Element[] Ri = new Element[n];
        for (int i = 0;i< n;i++){
            Ri[i] = pk.g_hat.powZn(y1[i].mul(sigma1)).mul(pk.gi[i].powZn(sigma1)).getImmutable() ;
        }
        Element d0 = skid.sk0.mul(pk.v.powZn(r1));
        Element d1 = skid.sk1.getImmutable();
        Element h_y = pk.H(pk.Y.powZn(sigma1), H).getImmutable();
        Element R = pk.h.powZn(r1).mul(h_y).getImmutable();
        Element R_til = pk.g.powZn(sigma1).getImmutable();
        Element R0 = pk.g_0.powZn(sigma1).getImmutable();
        CAPPREReEncKey rk01 = new CAPPREReEncKey(d0, d1, R, R_til, R0, Ri);
        rk.add(rk01);
        //j>1
        Element tmp_r = r1.getImmutable() ;
        Element tmp_h = h_y.getImmutable() ;
        for (int j = 0; j <Pa.ys.size()-1; j++){
            Element sigmaj = Zr.newRandomElement().getImmutable();
            Element rj = Zr.newRandomElement().getImmutable();
            Element sub_r = rj.sub(tmp_r).getImmutable();
            Element d = pk.v.powZn(sub_r).getImmutable();
            Element h_yj = pk.H(pk.Y.powZn(sigmaj), H).getImmutable();
            Element R_j = pk.h.powZn(sub_r).mul(h_yj.div(tmp_h)).getImmutable();
            Element R_til_j = pk.g.powZn(sigmaj).getImmutable();
            Element R0_j = pk.g_0.powZn(sigmaj).getImmutable();
            Element[] Rij = new Element[n];
            for (int i = 0;i< n;i++){
                Rij[i] = pk.g_hat.powZn(Pa.ys.get(j+1)[i].mul(sigmaj)).mul(pk.gi[i].powZn(sigmaj)).getImmutable() ;
            }
            rk.add(new CAPPREReEncKey(d,R_j,R_til_j,R0_j,Rij));
            tmp_r = rj.getImmutable();
            tmp_h = h_yj.getImmutable();
        }
        return rk;
    }
    public CAPPRECipherTextPrime reEncrypt(CAPPRECipherText ct, CAPPREReEncKey rk){
        Element p0 = curveParams.pairing(ct.C0, rk.d0).getImmutable();
        Element p1 = curveParams.pairing(ct.C1, rk.d1).getImmutable();
        Element B = p0.div(p1).getImmutable();
        CAPPRECipherTextPrime ctp = new CAPPRECipherTextPrime(ct.C, rk.R_til, rk.R_0, rk.Ri, rk.R, ct.C0, B, ct.C2);
        return ctp;
    }
    public CAPPRECipherTextPrime reEncrypt(CAPPRECipherTextPrime ct, CAPPREReEncKey rk){
        Element Ej = rk.R.mul(ct.E).getImmutable();
        Element E1j = ct.E1.mul(curveParams.pairing(ct.E0, rk.d0)).getImmutable();
        CAPPRECipherTextPrime ctp = new CAPPRECipherTextPrime(ct.C, rk.R_til, rk.R_0, rk.Ri, Ej, ct.E0, E1j, ct.E2);
        return ctp ;
    }
    public Element decryptIBE(CAPPRESecretKeyIBE skid, CAPPRECipherText ct){
        Element p0 = curveParams.pairing(ct.C0, skid.sk0).getImmutable();
        Element p1 = curveParams.pairing(ct.C1, skid.sk1).getImmutable();
        Element A = p0.div(p1).getImmutable();
        Element m = ct.C.div(A).getImmutable();
        return m ;
    }
    public Element decryptIPE(CAPPRECipherTextPrime ct, CAPPRESecretKeyIPE skx, Element[] x){
        Element p1 = curveParams.pairing(ct.C_til, skx.K2).getImmutable();
        Element p2 = curveParams.pairing(ct.C_0, skx.K0).getImmutable();
        Element pi = G.newOneElement().getImmutable();
        for (int i = 0;i < n ;i++){
            pi = pi.mul(ct.C_i[i].powZn(x[i])) ;
        }
        Element p3 = curveParams.pairing(pi, skx.K1).getImmutable();
        Element A = p1.div(p2.mul(p3)).getImmutable();
        Element ha = pk.H(A, H).getImmutable();
        Element Ap = ct.E.div(ha).getImmutable();
        Element p = curveParams.pairing(ct.E2, Ap).getImmutable();
        Element m = ct.C.mul(p).div(ct.E1).getImmutable();

        return m;
    }

}
