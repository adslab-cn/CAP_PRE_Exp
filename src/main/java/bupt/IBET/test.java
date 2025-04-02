package bupt.IBET;

import it.unisa.dia.gas.jpbc.Element;
import org.junit.Test;
/*
public class test {
    @Test
    public void testReEncDecTwo(){
        IBET ibet = new IBET("params/SS512.properties");
        IBET_MasterKey masterKey = ibet.setup(10);
        String id = "id002" ;
        String id2 = "id003" ;
        IBET_PrivateKey sk_id002 = ibet.register(masterKey, id);
        IBET_PrivateKey sk_id003 = ibet.register(masterKey, id2);
        String[] ids={"id001","id002","id003","id004"};

        IBET_CipherText ct =ibet.encrypt(id);
        Element m1 = ibet.decrypt1(ct, sk_id002);
        System.out.println("dec1"+m1);
        IBET_Token tk = ibet.authorize(sk_id002, ids);
        IBET_CipherTextPrime ctp = ibet.transform(tk,ct);
        Element m2 = ibet.decrypt2(ctp, sk_id003, ids,id2);
        System.out.println("dec2"+m2);
    }
}*/
