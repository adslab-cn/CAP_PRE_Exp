package basic.utils;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.io.File;

public class MyJXLUtils {

    public  static void write(String filename,String curves, int[] num,long[] setup,long[] keyGen,long[] enc,long[] rkG,long[] reEnc,long[] dec1,long[] dec2,int[] skl,int[] ctl, int[] rkl,int[] rctl ){
        try {
            // 1、创建工作簿(WritableWorkbook)对象，打开excel文件，若文件不存在，则创建文件
            WritableWorkbook writeBook = Workbook.createWorkbook(new File(filename));

            // 2、新建工作表(sheet)对象，并声明其属于第几页
            WritableSheet Sheet = writeBook.createSheet(curves, 1);// 第一个参数为工作簿的名称，第二个参数为页数

            // 3、创建单元格(Label)对象 第一个参数指定单元格的列数、第二个参数指定单元格的行数，第三个指定写的字符串内容

            // 写入标题
            Label t0 = new Label(0, 0,"num");
            Label t1 = new Label(1, 0,"setup");
            Label t2 = new Label(2, 0,"keyGen");
            Label t3 = new Label(3, 0,"enc");
            Label t4 = new Label(4, 0,"rkGen");
            Label t5 = new Label(5, 0,"reEnc");
            Label t6 = new Label(6, 0,"dec1");
            Label t7 = new Label(7, 0,"dec2");
            Label t8 = new Label(8, 0,"skl");
            Label t9 = new Label(9, 0,"ctl");
            Label t10 = new Label(10, 0,"rkl");
            Label t11 = new Label(11, 0,"rctl");
            Sheet.addCell(t0);
            Sheet.addCell(t1);
            Sheet.addCell(t2);
            Sheet.addCell(t3);
            Sheet.addCell(t4);
            Sheet.addCell(t5);
            Sheet.addCell(t6);
            Sheet.addCell(t7);
            Sheet.addCell(t8);
            Sheet.addCell(t9);
            Sheet.addCell(t10);
            Sheet.addCell(t11);

            for(int i = 0; i< setup.length;i++){
                Label temp0 = new Label(0, 1+i,String.valueOf(num[i]));
                Label temp1 = new Label(1, 1+i,String.valueOf(setup[i]));
                Label temp2 = new Label(2, 1+i,String.valueOf(keyGen[i]));
                Label temp3 = new Label(3, 1+i,String.valueOf(enc[i]));
                Label temp4 = new Label(4, 1+i,String.valueOf(rkG[i]));
                Label temp5 = new Label(5, 1+i,String.valueOf(reEnc[i]));
                Label temp6 = new Label(6, 1+i,String.valueOf(dec1[i]));
                Label temp7 = new Label(7, 1+i,String.valueOf(dec2[i]));
                Label temp8 = new Label(8, 1+i,String.valueOf(skl[i]));
                Label temp9 = new Label(9, 1+i,String.valueOf(ctl[i]));
                Label temp10 = new Label(10, 1+i,String.valueOf(rkl[i]));
                Label temp11 = new Label(11, 1+i,String.valueOf(rctl[i]));
                Sheet.addCell(temp0);
                Sheet.addCell(temp1);
                Sheet.addCell(temp2);
                Sheet.addCell(temp3);
                Sheet.addCell(temp4);
                Sheet.addCell(temp5);
                Sheet.addCell(temp6);
                Sheet.addCell(temp7);
                Sheet.addCell(temp8);
                Sheet.addCell(temp9);
                Sheet.addCell(temp10);
                Sheet.addCell(temp11);
            }
            // 4、打开流，开始写文件
            writeBook.write();
            // 5、关闭流
            writeBook.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public  static void write(String filename,String curves, int[] num,long[] setup,long[] keyGen1,long[] keyGen2,long[] enc,long[] rkG,long[] reEnc,long[] dec1,long[] dec2 ){
        try {
            // 1、创建工作簿(WritableWorkbook)对象，打开excel文件，若文件不存在，则创建文件
            WritableWorkbook writeBook = Workbook.createWorkbook(new File(filename));

            // 2、新建工作表(sheet)对象，并声明其属于第几页
            WritableSheet Sheet = writeBook.createSheet(curves, 1);// 第一个参数为工作簿的名称，第二个参数为页数

            // 3、创建单元格(Label)对象 第一个参数指定单元格的列数、第二个参数指定单元格的行数，第三个指定写的字符串内容

            // 写入标题
            Label t0 = new Label(0, 0,"num");
            Label t1 = new Label(1, 0,"setup");
            Label t2 = new Label(2, 0,"keyGen1");
            Label t3 = new Label(3, 0,"keyGen2");
            Label t4 = new Label(4, 0,"enc");
            Label t5 = new Label(5, 0,"rkGen");
            Label t6 = new Label(6, 0,"reEnc");
            Label t7 = new Label(7, 0,"dec1");
            Label t8 = new Label(8, 0,"dec2");

            Sheet.addCell(t0);
            Sheet.addCell(t1);
            Sheet.addCell(t2);
            Sheet.addCell(t3);
            Sheet.addCell(t4);
            Sheet.addCell(t5);
            Sheet.addCell(t6);
            Sheet.addCell(t7);
            Sheet.addCell(t8);


            for(int i = 0; i< setup.length;i++){
                Label temp0 = new Label(0, 1+i,String.valueOf(num[i]));
                Label temp1 = new Label(1, 1+i,String.valueOf(setup[i]));
                Label temp2 = new Label(2, 1+i,String.valueOf(keyGen1[i]));
                Label temp3 = new Label(3, 1+i,String.valueOf(keyGen2[i]));
                Label temp4 = new Label(4, 1+i,String.valueOf(enc[i]));
                Label temp5 = new Label(5, 1+i,String.valueOf(rkG[i]));
                Label temp6 = new Label(6, 1+i,String.valueOf(reEnc[i]));
                Label temp7 = new Label(7, 1+i,String.valueOf(dec1[i]));
                Label temp8 = new Label(8, 1+i,String.valueOf(dec2[i]));

                Sheet.addCell(temp0);
                Sheet.addCell(temp1);
                Sheet.addCell(temp2);
                Sheet.addCell(temp3);
                Sheet.addCell(temp4);
                Sheet.addCell(temp5);
                Sheet.addCell(temp6);
                Sheet.addCell(temp7);
                Sheet.addCell(temp8);

            }
            // 4、打开流，开始写文件
            writeBook.write();
            // 5、关闭流
            writeBook.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public  static void write(String filename,String curves, int[] num,long[] setup,long[] keyGen1,long[] keyGen2,long[] enc,long[] rkG,long[] reEnc,long[] dec1,long[] dec2,int[] skl, int[] skl2, int[] ctl, int[] rkl,int[] rctl  ){
        try {
            // 1、创建工作簿(WritableWorkbook)对象，打开excel文件，若文件不存在，则创建文件
            WritableWorkbook writeBook = Workbook.createWorkbook(new File(filename));

            // 2、新建工作表(sheet)对象，并声明其属于第几页
            WritableSheet Sheet = writeBook.createSheet(curves, 1);// 第一个参数为工作簿的名称，第二个参数为页数

            // 3、创建单元格(Label)对象 第一个参数指定单元格的列数、第二个参数指定单元格的行数，第三个指定写的字符串内容

            // 写入标题
            Label t0 = new Label(0, 0,"num");
            Label t1 = new Label(1, 0,"setup");
            Label t2 = new Label(2, 0,"keyGen1");
            Label t3 = new Label(3, 0,"keyGen2");
            Label t4 = new Label(4, 0,"enc");
            Label t5 = new Label(5, 0,"rkGen");
            Label t6 = new Label(6, 0,"reEnc");
            Label t7 = new Label(7, 0,"dec1");
            Label t8 = new Label(8, 0,"dec2");

            Label t9 = new Label(9, 0,"skl");
            Label t10 = new Label(10, 0,"skl2");
            Label t11 = new Label(11, 0,"ctl");
            Label t12 = new Label(12, 0,"rkl");
            Label t13 = new Label(13, 0,"rctl");

            Sheet.addCell(t0);
            Sheet.addCell(t1);
            Sheet.addCell(t2);
            Sheet.addCell(t3);
            Sheet.addCell(t4);
            Sheet.addCell(t5);
            Sheet.addCell(t6);
            Sheet.addCell(t7);
            Sheet.addCell(t8);
            Sheet.addCell(t9);
            Sheet.addCell(t10);
            Sheet.addCell(t11);
            Sheet.addCell(t12);
            Sheet.addCell(t13);


            for(int i = 0; i< setup.length;i++){
                Label temp0 = new Label(0, 1+i,String.valueOf(num[i]));
                Label temp1 = new Label(1, 1+i,String.valueOf(setup[i]));
                Label temp2 = new Label(2, 1+i,String.valueOf(keyGen1[i]));
                Label temp3 = new Label(3, 1+i,String.valueOf(keyGen2[i]));
                Label temp4 = new Label(4, 1+i,String.valueOf(enc[i]));
                Label temp5 = new Label(5, 1+i,String.valueOf(rkG[i]));
                Label temp6 = new Label(6, 1+i,String.valueOf(reEnc[i]));
                Label temp7 = new Label(7, 1+i,String.valueOf(dec1[i]));
                Label temp8 = new Label(8, 1+i,String.valueOf(dec2[i]));

                Label temp9 = new Label(9, 1+i,String.valueOf(skl[i]));
                Label temp10 = new Label(10, 1+i,String.valueOf(skl2[i]));
                Label temp11 = new Label(11, 1+i,String.valueOf(ctl[i]));
                Label temp12 = new Label(12, 1+i,String.valueOf(rkl[i]));
                Label temp13 = new Label(13, 1+i,String.valueOf(rctl[i]));




                Sheet.addCell(temp0);
                Sheet.addCell(temp1);
                Sheet.addCell(temp2);
                Sheet.addCell(temp3);
                Sheet.addCell(temp4);
                Sheet.addCell(temp5);
                Sheet.addCell(temp6);
                Sheet.addCell(temp7);
                Sheet.addCell(temp8);
                Sheet.addCell(temp9);
                Sheet.addCell(temp10);
                Sheet.addCell(temp11);
                Sheet.addCell(temp12);
                Sheet.addCell(temp13);

            }
            // 4、打开流，开始写文件
            writeBook.write();
            // 5、关闭流
            writeBook.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }









}
