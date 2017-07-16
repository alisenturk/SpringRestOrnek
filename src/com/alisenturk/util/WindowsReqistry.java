package com.alisenturk.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class WindowsReqistry {
	/**
	 * @usage WindowsReqistry.readRegistry("HKLM\\SOFTWARE\\Ghisler\\Total Commander", "InstallDir");
	 * @param location
	 * @param key
	 * @return
	 */
    public static final String readRegistry(String location, String key){
        Process         process = null;
        StreamReader    reader = null;
        try {
            process = Runtime.getRuntime().exec("reg query " +'"'+ location + "\" /v " + key);

            reader = new StreamReader(process.getInputStream());
            reader.start();
            process.waitFor();
            reader.join();

            String result = reader.getResult().trim();
            String[] parsed = result.split("\\s+");
            if (parsed.length > 1) {
                return parsed[parsed.length-1];
            }
        } catch (Exception e) {

        }finally {
            process = null;
            reader = null;
        }
        return null;
    }

    static class StreamReader extends Thread {
        private InputStream is;
        private StringWriter sw= new StringWriter();

        public StreamReader(InputStream is) {
            this.is = is;
        }

        public void run() {
            try {
                int c;
                while ((c = is.read()) != -1)
                    sw.write(c);
            } catch (IOException e) {
            }
        }

        public String getResult() {
            return sw.toString();
        }
    }
    
}
