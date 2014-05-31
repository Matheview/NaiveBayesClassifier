/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package naivebayesclassifier;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Jarosław Szutkowski
 */
public class NaiveBayesClassifier
{

//    private float[][] tst = new float[][]{
//        {2,4,2,1,4},
//        {1,2,1,1,2},
//        {9,7,10,7,4},
//        {4,4,10,10,2}
//    };
//    
//    private float[][] trn = new float[][]{
//        {1,3,1,1,2},
//        {10,3,2,1,2},
//        {2,3,1,1,2},
//        {10,9,7,1,4},
//        {3,5,2,2,4},
//        {2,3,1,1,4},
//    };
    
    private float[][] tst;
    private float[][] trn;
    
    private ArrayList<Integer> ds = new ArrayList<>();
    private HashMap<Integer, Integer> decisionOccurances = new HashMap<>();
    private Random rand = new Random();
    
    private int correctlyClassified = 0; //ilosc obiektow poprawnie sklasyfikowanych
    
    private float globalAccuracy;
    private float balancedAccuracy;
    
    
    private int[] decisions;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        NaiveBayesClassifier nbc = new NaiveBayesClassifier();
        nbc.go();
        nbc.printDecisions();
        nbc.setGlobalAccuracy();
        nbc.setBalancedAccuracy(); 
        nbc.saveResult();
    }
    
    public NaiveBayesClassifier()
    {
        DataReader dr = new DataReader();
        try
        {
            tst = dr.readArray("australian_TST.txt");
            trn = dr.readArray("australian_TRN.txt");
        }
        catch(Exception e)
        {
            System.exit(0);
        }
        
        decisions = new int[tst.length];
        setPossibleDecisions();
        setDecisionOccurances();
        
    }
    
    /*
     * Ustawienie możliwych decyzji
     */
    private void setPossibleDecisions()
    {
        int trnLength = trn.length;
        for(int i = 0; i < trnLength; i++)
        {
            if(ds.contains(trn[i][trn[i].length - 1]) == false)
            {
                ds.add((int)trn[i][trn[i].length - 1]);
            }
        }
    }
    
    /*
     * Ustawienie ilosci wystapien danej decyzji w systemie treningowym
     */
    private void setDecisionOccurances()
    {
        for(int i = 0; i < ds.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < trn.length; j++)
            {
                if(trn[j][trn[j].length - 1] == ds.get(i))
                {
                    count++;
                }
            }
            
            decisionOccurances.put(ds.get(i), count);
        }
    }
    
    public void go()
    {
        for(int i = 0; i < tst.length; i++)
        {
            float rowDecision[] = new float[ds.size()];
            
            for(int j = 0; j < ds.size(); j++)
            {
                int decisionValue = ds.get(j);
                
                float factor = decisionOccurances.get(decisionValue) / trn.length; //ilosc wystapien danego wyniku do ilosci wynikow
                
                int occurances = decisionOccurances.get(decisionValue); //
                
                float sum = 0;
                
                
                for(int k = 0; k < tst[i].length; k++)
                {
                    int unitCount = 0;
                    for(int l = 0; l < trn.length; l++)
                    {
                        if(trn[l][trn[l].length - 1] == decisionValue && trn[l][k] == tst[i][k])
                        {
                            unitCount++;
                        }
                    }
                    sum += unitCount / occurances;
                }
                
                rowDecision[j] = factor * sum; 
            }
            
            decisions[i] = ds.get(getMax(rowDecision));
            
            if(decisions[i] == tst[i][tst[i].length - 1])
            {
                correctlyClassified++;
            }
        }
    }
    
    private int getMax(float[] values)
    {
        boolean same = true;
        
        int size = values.length;
        
        float first = values[0];
        
        for(int i = 1; i < size; i++)
        {
            if(values[i] != first)
            {
                same = false;
                break;
            }
        }
        
        if(same == true)
        {
            return rand.nextInt(size);
        }
        
        float max = values[0];
        int which = 0;
        
        for(int j = 0; j < size; j++)
        {
            if(values[j] >= max)
            {
                max = values[j];
                which = j;
            }
        }
        
        return which;
    }
    
    public void printDecisions()
    {
        for(int i = 0; i < decisions.length; i++)
        {
            System.out.println(decisions[i]);
        }
    }

    public float getGlobalAccuracy()
    {
        return globalAccuracy;
    }

    public void setGlobalAccuracy()
    {
        this.globalAccuracy = (float)correctlyClassified / trn.length;
    }

    public float getBalancedAccuracy()
    {
        return balancedAccuracy;
    }

    public void setBalancedAccuracy()
    {
        float sum = 0;
        for(int i = 0; i < ds.size(); i++)
        {
            int classCount = 0;
            int correctCount = 0;
            for(int j = 0; j < tst.length; j++)
            {
                if(tst[j][tst[j].length - 1] == ds.get(i))
                {
                    classCount++;
                    if(tst[j][tst[j].length - 1] == decisions[j])
                    {
                        correctCount++;
                    }
                }
            }
            sum += (float)correctCount / (float)classCount;
        }
        
        this.balancedAccuracy = sum / (float)ds.size();
    }

    private void saveResult()
    {
        try
        {
            FileWriter decWriter = new FileWriter("dec_bayes.txt");
            
            decWriter.write("DE -> DK");
            decWriter.write(System.lineSeparator());
            for(int i = 0; i < decisions.length; i++)
            {
                decWriter.write(decisions[i] + " -> " + (int)tst[i][tst[i].length - 1]);
                decWriter.write(System.lineSeparator());
            }
            decWriter.close();

            FileWriter accWriter = new FileWriter("acc_bayes.txt");
            
            accWriter.write("Global Accuracy: " + getGlobalAccuracy());
                accWriter.write(System.lineSeparator());
            accWriter.write("Balanced Accuracy: " + getBalancedAccuracy());
            accWriter.close();
        }
        catch(Exception e)
        {
            
        }
    }
}
