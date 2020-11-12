package cn.rascal.rfidfhssreader;

import com.impinj.octane.AntennaConfigGroup;
import com.impinj.octane.ImpinjReader;
import com.impinj.octane.OctaneSdkException;
import com.impinj.octane.ReaderMode;
import com.impinj.octane.ReportConfig;
import com.impinj.octane.ReportMode;
import com.impinj.octane.Settings;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class Reader {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("RFID FHSS Reader is starting...");
        initReader();
        try {
            Map<String, String> values = new CSVReaderHeaderAware(
                    new FileReader("d:\\Atom\\Dependency\\test.csv")).readMap();
            for (String key : values.keySet())
                System.out.println(key + ": " + values.get(key));
        } catch (IOException|CsvValidationException e) {
            System.err.print(e);
        }
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
            
            Settings settings = reader.queryDefaultSettings();

            ReportConfig report = settings.getReport();
            report.setIncludeAntennaPortNumber(true);
            report.setMode(ReportMode.Individual);

            // The reader can be set into various modes in which reader
            // dynamics are optimized for specific regions and environments.
            // The following mode, AutoSetDenseReader, monitors RF noise and interference and then automatically
            // and continuously optimizes the reader's configuration
            settings.setReaderMode(ReaderMode.AutoSetDenseReader);

            // set some special settings for antenna 1
            AntennaConfigGroup antennas = settings.getAntennas();
            antennas.disableAll();
            antennas.enableById(new short[]{1});
            antennas.getAntenna((short) 1).setIsMaxRxSensitivity(false);
            antennas.getAntenna((short) 1).setIsMaxTxPower(false);
            antennas.getAntenna((short) 1).setTxPowerinDbm(20.0);
            antennas.getAntenna((short) 1).setRxSensitivityinDbm(-70);

            System.out.println("Applying Settings");
            reader.applySettings(settings);
            System.out.println("Starting...");
            reader.start();

            System.out.println("Press Enter to exit.");
            Scanner in = new Scanner(System.in);
            in.nextLine();

            reader.stop();
            reader.disconnect();
        } catch (OctaneSdkException ex) {
            System.err.println(ex);
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
}
