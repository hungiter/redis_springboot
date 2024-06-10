package jpa.learning.neo4j.service;

import jpa.learning.neo4j.entity.Phone;
import jpa.learning.neo4j.entity.PhoneSystem;
import jpa.learning.neo4j.entity.PhoneType;
import jpa.learning.neo4j.repository.PhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class PhoneService {
    private final Random random = new Random();

    @Autowired
    PhoneRepository phoneRepository;

    public Phone createPhone() {
        // Get all phone types
        PhoneType[] phoneTypes = PhoneType.values();

        // Randomly select one phone type
        PhoneType randomPhoneType = phoneTypes[random.nextInt(phoneTypes.length)];

        // Generate random memory and price values (adjust ranges as needed)
        int randomInt = random.nextInt(5);
        int randomMemory = getRandomMemory(randomInt);
        int randomPrice = getRandomPrice(randomInt, randomPhoneType.getSystem());

        // Create a new Phone instance
        Phone newPhone = new Phone(randomPhoneType, randomMemory, randomPrice);
        phoneRepository.save(newPhone);
        return newPhone;
    }

    public List<Phone> getAll() {
        return phoneRepository.getAll();
    }

    public List<Phone> listByOS(String system) {
        String reprocessed_system = system.replaceAll("(?i)\\bios\\b", "iOS")
                .replaceAll("(?i)\\bkaios\\b", "KaiOS")
                .replaceAll("(?i)\\bandroid\\b", "Android");
        return phoneRepository.listByOS(reprocessed_system);
    }

    public List<Phone> listByPrice(int n1, int n2, int page) {
        return phoneRepository.listPrice(Math.min(n1, n2), Math.max(n1, n2), 10*(page+1));
    }


    // Helper method to generate random memory (in GB)
    private int getRandomMemory(int num) {
        // Example: random memory between 16 GB and 256 GB
        int[] possibleMemories = new int[]{16, 32, 64, 128, 256};
        return possibleMemories[num];
    }

    // Helper method to generate random price
    private int getRandomPrice(int num, PhoneSystem system) {
        // Example: random price between $100 and $1000 in increments of $50
        return 100 + 50 * num * (system.name().equals("iOS") ? 3 : (system.name().equals("KaiOS") ? 1 : 2)); // 100, 150, ..., 1000
    }
}
