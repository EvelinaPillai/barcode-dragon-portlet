package life.qbic.portal.portlet.processes;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;


public class ProcessBuilderWrapper {
    private StringWriter infos;
    private StringWriter errors;
    private int status;
 
    public ProcessBuilderWrapper(File directory, List<String> command, String path) throws Exception {
    	infos = new StringWriter();
        errors = new StringWriter();
        ProcessBuilder pb = new ProcessBuilder(command);
        if(path != null) pb.environment().put("PATH", path);
        if(directory != null) pb.directory(directory);
        Process process = pb.start();
        StreamBoozer seInfo = new StreamBoozer(process.getInputStream(), new PrintWriter(infos, true));
        StreamBoozer seError = new StreamBoozer(process.getErrorStream(), new PrintWriter(errors, true));
        seInfo.start();
        seError.start();
        status = process.waitFor();
        seInfo.join();
        seError.join();
    }
 
    public ProcessBuilderWrapper(List<String> command) throws Exception {
        this(null, command, null);
    }
    
    public ProcessBuilderWrapper(List<String> command, String path) throws Exception {
    	this(null, command, path);
    }
 
    public String getErrors() {
        return errors.toString();
    }
 
    public String getInfos() {
        return infos.toString();
    }
 
    public int getStatus() {
        return status;
    }   
}
 
    class StreamBoozer extends Thread {
        private InputStream in;
        private PrintWriter pw;
 
        StreamBoozer(InputStream in, PrintWriter pw) {
            this.in = in;
            this.pw = pw;
        }
 
        @Override
        public void run() {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(in));
                String line = null;
                while ( (line = br.readLine()) != null) {
                    pw.println(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }