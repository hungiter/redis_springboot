package fpt.com.eureka_cloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.system.DiskSpaceHealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Component("diskSpace")
public class CustomDiskSpaceHealthIndicator extends DiskSpaceHealthIndicator {

    private static Logger logger = LoggerFactory.getLogger(CustomDiskSpaceHealthIndicator.class);

    private File path;
    private DataSize threshold;

    public CustomDiskSpaceHealthIndicator() {
        // new File("/") - represents root Path
        // DataSize.ofMegabytes(8000) - Kept My threshold to 1 Gb 
        super(new File("/"), DataSize.ofMegabytes(8000));
        this.path = new File("/");
        this.threshold = DataSize.ofMegabytes(8000);
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        long diskFreeInBytes = this.path.getUsableSpace();
        if (diskFreeInBytes >= this.threshold.toBytes()) {
            builder.up();
        } else {
            logger.warn("Free disk space below threshold. Available: {} bytes (threshold: {})", diskFreeInBytes, this.threshold);
            builder.down();
        }
        // Skip These if you don't  need response, In My case I needed 
        // response  details in percentages 

        long total = this.path.getTotalSpace();

        Map<String, Object> customDiskDetails = getStringObjectMap(diskFreeInBytes, total);
        builder.withDetails(customDiskDetails);
    }

    private Map<String, Object> getStringObjectMap(long diskFreeInBytes, long total) {
        double freePrecentage = ((double) diskFreeInBytes / total) * 100;
        double subtract = total - diskFreeInBytes;
        double usedPercentage = ((subtract / total) * 100);

        Map<String, Object> customDiskDetails = new HashMap<>();
        customDiskDetails.put("total", "100%");
        customDiskDetails.put("free", Math.round(freePrecentage) + "%");
        customDiskDetails.put("used", Math.round(usedPercentage) + "%");
        customDiskDetails.put("exists", this.path.exists());
        customDiskDetails.put("threshold", this.threshold.toBytes());
        return customDiskDetails;
    }
}