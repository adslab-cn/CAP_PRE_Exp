package bupt.Fabric;

import it.unisa.dia.gas.jpbc.Element;
import org.junit.Test;
/*
public class testFabric {
    @Test
    public void testEncDec2(){
        Fabric fabric = new Fabric("params/MNT159.properties");

        String[] attr1 ={"s001","s002","s003","s004","s005"};
        String[] attr2 ={"s001","s002"};
        String policy1="s001 and s002 and s003 and s004" ;

        FabricMasterKey msk = fabric.setup();
        FabricSecretKeyIBE sk1 = fabric.keyGenIBE(msk, "id001");
        FabricCipherText ct = fabric.encrypt("id001");
        Element m = fabric.decryptIBE(ct, sk1);
        System.out.println("dec1="+m);

        FabricSecretKeyABE sk2 = fabric.keyGenABE(msk, attr1);
        FabricSecretKeyABE ske = fabric.keyGenABE(msk, attr2);
        FabricReEncryptionKey rk = fabric.reKeyGen(sk1, policy1);
        FabricCipherTextPrime ctp = fabric.reEncrypt(ct, rk);
        Element m2 = fabric.decryptABE(ctp, sk2);
        System.out.println("dec2="+m2);

    }

    @Test
    public void test1(){
        String id = "id001";
        String s1 = "2"+id+"0";
        int t = 1;
        String s2 = (t+1)+id+"0";
        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s1.equals(s2));
    }
}
*/