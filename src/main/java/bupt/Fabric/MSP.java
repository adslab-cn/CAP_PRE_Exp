package bupt.Fabric;

import java.util.HashMap;
import java.util.Map;

public class MSP {

    public static int [][] creatMSP(int n){
        int[][] msp = new int[n][n];
        for (int i = 0;i<n;i++){
            for (int j =0;j<n;j++){
                if (i==j){
                    if(i==0){
                        msp[i][j]=1;
                    }else msp[i][j]=-1;
                }else if(j==i+1){
                    msp[i][j]=1;
                }else {
                    msp[i][j]=0 ;
                }
            }
        }

        return msp ;
    }
// convert policy string to msp
// FIXME: Hard-coded to support AND operations only

    public static Map<String, int[]> convert_policy_to_msp(String policy) {
        String [] attrs = policy.split(" and ");
        int n =attrs.length ;
        if ( n < 1) {
            System.err.println("MSP conversion error!");
            System.exit(1);
        }
        Map<String, int[]> msp = new HashMap<>();
        int[][] MSPs = creatMSP(n);
        for (int i=0; i<n; i++) {
            msp.put(attrs[i], MSPs[i]);
        }
        return msp;
    }
}
