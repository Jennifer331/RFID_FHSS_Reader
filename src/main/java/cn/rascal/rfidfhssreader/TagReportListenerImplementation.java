/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.rascal.rfidfhssreader;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Xiaoyue Lei
 */
public class TagReportListenerImplementation implements TagReportListener{

    @Override
    public void onTagReported(ImpinjReader reader, TagReport tr) {
        List<Tag> tags = tr.getTags();
        List<Record> records = new LinkedList<>();
        for (Tag t : tags) {
            String epc = t.getEpc().toString();
            double channel = t.getChannelInMhz();
            double phase = t.getPhaseAngleInRadians();
            double rssi = t.getPeakRssiInDbm();
            Record r = new Record(epc, channel, phase, rssi);
            records.add(r);
            System.out.print(r.toString());
        }
        
        StorageManager.getIntance().save(records);
    }

}
