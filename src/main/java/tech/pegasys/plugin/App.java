/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package tech.pegasys.plugin;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class App {

  public static void main(String[] args) {
    try {
      HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
      Map<Long, String> map = hazelcastInstance.getMap("data");
      final AtomicLong counter = new AtomicLong(0L);
      Executors.newSingleThreadScheduledExecutor()
          .scheduleAtFixedRate(
              () -> {
                // System.out.println("Writing new message");
                map.put(counter.get(), String.format("%d", counter.getAndIncrement()));
              },
              0,
              2,
              TimeUnit.SECONDS);

      ClientConfig config = new ClientConfig();
      HazelcastInstance hazelcastInstanceClient = HazelcastClient.newHazelcastClient(config);
      IMap<Long, String> clientMap = hazelcastInstanceClient.getMap("data");
      Executors.newSingleThreadScheduledExecutor()
          .scheduleAtFixedRate(
              () -> {
                clientMap.entrySet().forEach(entry -> System.out.printf("%s\t", entry.getValue()));
                System.out.println();
              },
              0,
              2,
              TimeUnit.SECONDS);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}
