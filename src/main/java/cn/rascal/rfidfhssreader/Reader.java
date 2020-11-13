package cn.rascal.rfidfhssreader;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.OctaneSdkException;
import com.impinj.octane.ReportConfig;
import com.impinj.octane.Settings;

import java.util.Scanner;

public class Reader {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("RFID FHSS Reader is starting...");
        initReader();
    }
    
    private static void initReader() {
        String hostname = System.getProperty(SampleProperties.hostname);
        try {
            if (null == hostname) {
                throw new Exception("Must specify the '"
                        + SampleProperties.hostname + "' property");
            }

            ImpinjReader reader = new ImpinjReader();

            System.out.println("Connecting...");
            reader.connect(hostname);

            reader.setTagReportListener(new TagReportListenerImplementation());
            
            System.out.println("Applying Settings");
            Settings settings = reader.queryDefaultSettings();
            ReportConfig r = settings.getReport();
            r.setIncludeChannel(true);
            r.setIncludePeakRssi(true);
            r.setIncludePhaseAngle(true);
            reader.applySettings(settings);

            System.out.println("Starting...");
            reader.start();

            System.out.println("Press Enter to exit.");
            Scanner in = new Scanner(System.in);
            in.nextLine();

            reader.stop();
            reader.disconnect();
            
            System.out.println("Enter file name to save:");
            String name = in.next();
            StorageManager.getIntance().saveToFile(name);
            
            System.out.println("See ya!");
        } catch (OctaneSdkException ex) {
            System.err.println(ex);
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
}
