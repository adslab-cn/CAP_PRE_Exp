package bupt.IBET;


import basic.algorithms.PolynomialExpansion;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.math.BigInteger;
import java.util.Arrays;

public class IBET {
    private Pairing curveParams;
    private Field G, GT, Zr;

    public IBET_PublicKey pk ;

    public IBET(Pairing curveParams) {
        this.curveParams = curveParams;
        G = curveParams.getG1();
        GT = curveParams.getGT();
        Zr = curveParams.getZr();
    }
    public IBET_MasterKey setup(int m){
        Element g = G.newRandomElement().getImmutable();
        Element h = G.newRandomElement().getImmutable();
        Element u = G.newRandomElement().getImmutable();
        Element alpha = Zr.newRandomElement().getImmutable();
        Element g1 = g.powZn(alpha).getImmutable();
        Element u_a = u.powZn(alpha).getImmutable();
        Element[] h_a = new Element[m+1] ;
        h_a[0] = h;
        for (int i =1; i<m+1; i++){
            h_a[i] = h.powZn(alpha.pow(new BigInteger(String.valueOf(i)))).getImmutable();
        }
        Element egh = curveParams.pairing(g, h).getImmutable();
        pk = new IBET_PublicKey(g1,u,u_a,h_a,egh);
        return new IBET_MasterKey(g,alpha);
    }
    public IBET_PrivateKey register(IBET_MasterKey msk,String ID){
        Element sk = msk.g.powZn((msk.alpha).add(pk.H0(ID.getBytes(), Zr)).invert()).getImmutable();
        return new IBET_PrivateKey(sk);
    }
    public IBET_CipherText encrypt(String ID,Element m){
        //Element m = GT.newRandomElement().getImmutable();
        //System.out.println("message:"+m);

        Element s = Zr.newRandomElement().getImmutable();
        Element C0 = m.mul(pk.egh.powZn(s)).getImmutable();
        Element hid = pk.H0(ID.getBytes(), Zr).getImmutable();
        Element C1 = pk.ha[1].mul(pk.ha[0].powZn(hid)).powZn(s).getImmutable();
        Element C2 = pk.ua.mul(pk.u.powZn(hid)).powZn(s).getImmutable();
        return new IBET_CipherText(C0,C1,C2);
    }
    public IBET_Token authorize(IBET_PrivateKey sk, String[] S){
        Element t = Zr.newRandomElement().getImmutable();
        Element r = Zr.newRandomElement().getImmutable();
        Element d1 = pk.g1.powZn(t.negate()).getImmutable();

        Element temp1 =G.newOneElement().getImmutable();
        Element[] H_id_i =new Element[S.length];
        //calculate H(id_i)
        for (int i =0;i< S.length;i++){
            H_id_i[i] = pk.H0(S[i].getBytes(), Zr).getImmutable();
        }
        //calculate cofficients of Π(α+H(id_i))
        Element[] coefficients = PolynomialExpansion.calculateCoefficient(H_id_i, Zr);
        //calculate  g^[Π(α+H(id_i))]
        for (int i = 0; i < coefficients.length; i++) {
            temp1 = temp1.mul(pk.ha[i].powZn(coefficients[i])).getImmutable();
        }
        Element d2 = temp1.powZn(t).getImmutable();

        Element d3 = pk.H1(pk.egh.powZn(t), G).mul(pk.ha[0].powZn(r)).getImmutable();
        Element d4 = sk.sk.mul(pk.u.powZn(r.negate())).getImmutable();
        return new IBET_Token(d1,d2,d3,d4);
    }
    public IBET_CipherTextPrime transform(IBET_Token tk,IBET_CipherText ct){
        Element tmp = curveParams.pairing(ct.C1, tk.d4).getImmutable();
        Element c5 = ct.C0.div(tmp);
        return new IBET_CipherTextPrime(tk.d1,tk.d2,tk.d3, ct.C2, c5);
    }
    public Element decrypt1(IBET_CipherText ct, IBET_PrivateKey sk){
        Element tmp = curveParams.pairing(sk.sk, ct.C1);
        Element m = ct.C0.div(tmp).getImmutable();
        return m;
    }
    public Element decrypt2(IBET_CipherTextPrime ctp, IBET_PrivateKey sk, String[] S, String ID){
        //get S_tilde  S~
        String[] S_tilde = getS_tilde(S, ID);
        //calculate h^ΔS~（α）:h_delta
        Element[] H_id_i =new Element[S_tilde.length];
        Element pi_hid =Zr.newOneElement().getImmutable();
        for (int i =0;i< S_tilde.length;i++){
            H_id_i[i] = pk.H0(S_tilde[i].getBytes(), Zr).getImmutable();//calculate H(id_i)
            pi_hid = pi_hid.mulZn(H_id_i[i]).getImmutable() ;   //calculate ΠH(id_i)
        }
        Element h_delta = calculate_h_delta_S_tilde(S_tilde,H_id_i);
        Element p1 = curveParams.pairing(ctp.c1, h_delta).getImmutable();
        Element p2 = curveParams.pairing(sk.sk,ctp.c2).getImmutable();
        Element B = p1.mul(p2).powZn(pi_hid.invert()).getImmutable();
        Element hr = ctp.c3.div(pk.H1(B, G)).getImmutable();
        Element p3 = curveParams.pairing(hr, ctp.c4).getImmutable();
        Element m = ctp.c5.div(p3).getImmutable();
        return m ;
    }

    private String[] getS_tilde(String[] S,String id){
        if (!Arrays.asList(S).contains(id)){
            System.err.println("wrong id");
        }
        String[] S_tilde= new String[S.length-1] ;
        int index = 0;
        for (int i = 0;i < S.length;i++){
            if (!S[i].equals(id)){
                S_tilde[index]=S[i] ;
                index++ ;
            }
        }
        return  S_tilde ;
    }

    //calculate g^ΔS~（α）:g_delta_S_tilde
    private Element calculate_h_delta_S_tilde(String[] S_tilde,Element[] H_id_i){

        Element[] coefficients = PolynomialExpansion.calculateCoefficient(H_id_i, Zr);
        Element h_delta_S_tilde =G.newOneElement().getImmutable();
        //g_delta_S_tilde=g^(α^-)^(Π(α+H(id_i))-ΠH(id_i))=g^(c1+c2x+...+cnx^n-1)
        for (int i = 1; i < coefficients.length; i++) {
            h_delta_S_tilde = h_delta_S_tilde.mul(pk.ha[i-1].powZn(coefficients[i])).getImmutable();
        }
        return h_delta_S_tilde ;
    }

}
