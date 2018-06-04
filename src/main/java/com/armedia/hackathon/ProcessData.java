package com.armedia.hackathon;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.math.*;

public class ProcessData {

    //main class to process data given in a file.
    //Usage: filename given via command line.
    public static void main(String[] args) throws FileNotFoundException{
        Scanner sc = new Scanner(new File(args[1]));

        if(sc.hasNext()){

            int nGroups = sc.nextInt();

            for (int i=0; i < nGroups; i++){
                String control = sc.next();
                if (control.charAt(0) == '*'){
                    int nRec = Character.getNumericValue(control.charAt(1));
                    int nElm = Character.getNumericValue(control.charAt(2));

                    if(nElm == 3){
                        try{
                            String record = sc.next();
                            int v1 = Character.getNumericValue(record.charAt(0));
                            int v2 = Character.getNumericValue(record.charAt(1));
                            char v3 = record.charAt(2);

                            //translations for v3 here.

                            //add the last part of zeta to the expression.
                            double result = ((3.14)*(v1))+((6.48485)*(Math.pow(v2,3)));

                        }catch (Exception e){
                            System.out.println(e);
                        }
                    }

                }else{
                    System.out.println(String.format("Error in control record : %d",i));
                }
            }

        }else{
            System.out.println("File invalid : 1st line is empty !!");
        }
    }
}
