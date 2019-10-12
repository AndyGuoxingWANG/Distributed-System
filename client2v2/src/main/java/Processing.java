import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Represent Processing with their details-- . *
 *
 * @author Yiwen Zhang
 */
public class Processing {
  public static void processData(ConcurrentLinkedDeque resultQueue, long wallTime, int numberOfThreads) throws IOException {
    long successful = 0;
    long unsuccessful = 0;
    long sum = 0;
    FileWriter csvWriter = new FileWriter("src/main/resources/" +numberOfThreads+".csv");
    csvWriter.append("start time");
    csvWriter.append(",");
    csvWriter.append("request type");
    csvWriter.append(",");
    csvWriter.append("latency");
    csvWriter.append(",");
    csvWriter.append("response code");
    csvWriter.append("\n");
    ArrayList<Long> latencyList = new ArrayList<>();
    for (Iterator it = resultQueue.iterator(); it.hasNext(); ) {
      Result result = (Result) it.next();
      csvWriter.append(String.valueOf(result.getStartTime()));
      csvWriter.append(",");
      csvWriter.append(result.getRequestType());
      csvWriter.append(",");
      csvWriter.append(String.valueOf(result.getLatency()));
      csvWriter.append(",");
      csvWriter.append(String.valueOf(result.getResponseCode()));
      csvWriter.append("\n");
      if (result.getResponseCode() == 201){
        successful += 1;
      }else{
        unsuccessful += 1;
      }
      latencyList.add(result.getLatency());
      sum += result.getLatency();
    }
    csvWriter.flush();
    csvWriter.close();
    if(latencyList.size() == 0){
      System.out.println("No results");
      return;
    }
    Collections.sort(latencyList);
    long median = 0;
    if (latencyList.size()%2==0){
      median = latencyList.get(latencyList.size()/2);
    }
    else{
      median = (latencyList.get(latencyList.size()/2)+latencyList.get(latencyList.size()/2+1))/2;
    }
    double throughput = (double) (successful+unsuccessful)/wallTime;
    long maxResponseTime = latencyList.get(latencyList.size()-1);
    long mean = sum/latencyList.size();
    long p99 = latencyList.get((int)(latencyList.size()*0.99-1));
    System.out.println("total time spent: " + wallTime);
    System.out.println("number of threads: " + numberOfThreads);
    System.out.println("number of successful requests: " + successful);
    System.out.println("number of unsuccessful requests: " + unsuccessful);
    System.out.println("mean response time: " + mean);
    System.out.println("median response time: " + median);
    System.out.println("throughput: " + throughput);
    System.out.println("p99: " + p99);
    System.out.println("max response time: " + maxResponseTime);
  }
}
