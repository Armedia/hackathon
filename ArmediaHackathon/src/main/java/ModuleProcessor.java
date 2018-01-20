package main.java;

import java.io.*;

public class ModuleProcessor {
    private static final String file = "/Users/jg/IdeaProjects/ArmediaHackathon/src/main/files/records";
    private static final double a = 3.1;
    private static final double b = 4.1;
    private static final double c = 6;
    private static final double pi = 3.14;
    private static final double psi = 6.48485;
    private static final double zeta = 3.2;
    //TODO: Calculate the result
    //Incomplete...Should've used map to map contents stopped at line 40.
    // Needed to move on to read the next lines and calculate the result to return.


    private static void fileProcessor (String fileName) {
        String line = null;
        String numDataGroups = "";
//        String
        try {

            //Read File
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            String[] a;
            if (br.ready()) {
                while ((line = br.readLine()) != null) {

                    a = line.split("");
                    if (line.contains("*")) {

                        //this is control group next X lines should contain elements
                        int numElements = (int) line.charAt(3);
                        if (numElements == 3) {

                            //process elements
                            //TODO: Get the next numElements in data
                            for (int i = 0; i < numElements; i++) {

                            }
//                            System.out.println(line);
                        } else {
                            System.out.println("This line does not contain exactly 3 elements");
                        }
                    }

                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static int numberOfLines(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }

    public static void main(String[] args) {
        fileProcessor(file);
    }
}
