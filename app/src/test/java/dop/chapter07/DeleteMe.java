package dop.chapter07;

import dop.chapter07.Finalizing.RawData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteMe {

//    public static List<Record> dedupe(List<RawData> records) {
//        Map<String, List<Record>> grouped = new HashMap<String, List<Record>>();
//
//        for (RawData r : records) {
//            if (!grouped.containsKey(r.id())) {
//                grouped.put(r.id(), new ArrayList<Record>());
//            }
//            grouped.get(r.id()).add(r);  // Do not apply defaults here
//        }
//
//        List<Record> finalRecords = new ArrayList<Record>();
//
//        for (Map.Entry<String, List<Record>> entry : grouped.entrySet()) {
//            List<Record> group = entry.getValue();
//            Record best = group.get(0);
//
//            for (int i = 1; i < group.size(); i++) {
//                Record current = group.get(i);
//
////                CustomerImpact bestImpact = best.overallImpact();
////                CustomerImpact currImpact = current.overallImpact();
////
////                if (currImpact == CustomerImpact.FAVORS && bestImpact == CustomerImpact.HARMS) {
////                    best = current;
////                } else if (currImpact == bestImpact) {
////                    if (current.toString().compareTo(best.toString()) < 0) {
////                        best = current;
////                    }
////                }
////            }
////
////            finalRecords.add(applyDefaults(best));
//        }
//
//        return finalRecords;
//    }
}
