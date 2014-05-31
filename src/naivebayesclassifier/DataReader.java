/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package naivebayesclassifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Jarek
 */
public class DataReader
{

    public float[][] tst;
    public float[][] trn;

    public float[][] readArray(String filename) throws Exception
    {
        float[][] tmp;
        ArrayList<float[]> list = new ArrayList<>();

        FileReader fileReader = new FileReader(new File(filename));
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line = bufferedReader.readLine();

        ArrayList<Float> rowList;

        while (line != null)
        {
            
            rowList = new ArrayList<>();
            
            String[] split = line.split(" ");

            for(String s : split)
            {
                rowList.add(new Float(s));
            }

            float[] row = new float[rowList.size()];
            for (int k = 0; k < rowList.size(); k++)
            {
                row[k] = rowList.get(k);
            }
            list.add(row);
            line = bufferedReader.readLine();
        }

        tmp = new float[list.size()][];

        for (int i = 0; i < list.size(); i++)
        {
            tmp[i] = new float[list.get(i).length];
            for (int j = 0; j < list.get(i).length; j++)
            {
                tmp[i][j] = list.get(i)[j];
//                    System.out.print(tmp[i][j] + " ");
            }
//                System.out.println("");
        }
//        System.out.println("");
//        System.out.println("");
//        System.out.println("");
        return tmp;
    }
}
