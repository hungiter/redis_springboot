package fpt.com.eureka_cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EurekaClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientApplication.class, args);
    }
//    @Override
//    public Info info() {
//        Info.Builder builder = new Info.Builder();
//        Map<String, Object> details = new HashMap<String, Object>() {{
//            put("Application's name", "INF's Eureka");
//            put("Description", "Eureka's Reached Microservices CLient");
//            put("Version", "1.0.0");
//			put("Author's name", "Zion Nguyễn");
//			put("Build's time", LocalDate.now().toString());
//            // Add more key-value pairs as needed
//        }};
//        return builder.withDetails(details).build();
//    }

//    @Override
//    public List<String> serviceInstancesByApplicationName() {
//        return this.discoveryClient.getInstances("eureka_cloud_client1")
//                .stream()
//                .map(si -> si.getServiceId() + " (" + si.getHost() + ":" + si.getPort() + ")")
//                .collect(Collectors.toList());
//    }
}
