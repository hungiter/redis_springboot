package fpt.study.exportDB.services;

import fpt.study.exportDB.models.SessionRepository;
import fpt.study.exportDB.models.Sessions;
import fpt.study.exportDB.utils.ExcelGenerator;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DatabaseServices implements DatabaseExecute, DatabaseToExcel {
    private List<Sessions> sessionsList = List.of(
            new Sessions("beadd857-fe35-11ee-a061-110f2c7", "sgfdn0401", "COMPLETED"),
            new Sessions("cdfdcfcf-fe3c-11ee-a061-c3bc7f5", "sgfdn0401", "COMPLETED"),
            new Sessions("a4268d52-03ad-11ef-a07f-2d9239f", "sgfdn0401", "COMPLETED"),
            new Sessions("cb08e648-03b0-11ef-a07f-b96730a", "sgfdn0401", "COMPLETED"),
            new Sessions("40f85e5f-03b4-11ef-a07f-61ba14d", "sgfdn0401", "COMPLETED"),
            new Sessions("a3a10809-fe37-11ee-a061-4f9d656", "sgfdn0401"),
            new Sessions("6d96829b-fe3b-11ee-a061-6fb6a0c", "sgfdn0401"),
            new Sessions("1bf1851d-fe3c-11ee-a061-17f5eaf", "sgfdn0401"),
            new Sessions("e80e95d4-03ad-11ef-a07f-21c3234", "sgfdn0401"),
            new Sessions("b700ceed-03b2-11ef-a07f-2bff6c7", "sgfdn0401"),
            new Sessions("f091fe91-03b8-11ef-a07f-513acba", "sgfdn0401")
    );

    private final String[] usernames = {"sgfdn0401", "nghxxbxnhmx", "hungiter", "pokabu1999", "mike_nguyen90it"};
    private final String[] statuses = {"OPEN", "COMPLETED"};


    @Autowired
    SessionRepository sessionRepository;

    @Override
    public String initDB() {
        this.sessionsList.forEach(this::addDatabase);
        generateSessions(1000).forEach(this::addDatabase);
        return "Database Added: 11 + 1000";
    }

    @Override
    public String statusChange(String id) {
        return "";
    }

    @Override
    public String deleteSession(String id) {
        return "";
    }

    @Override
    public long countByStatus(int status) {
        long total = sessionRepository.countByStatus(
                status == 0 ? "COMPLETED" : "OPEN"
        );


        return (total > 0) ? total / 100 : -1;
    }

    @Override
    public List<Sessions> findByStatus(int status, int page) {
        int size = 100;
        Pageable pageable = PageRequest.of(page, size);
        Page<Sessions> result = sessionRepository.findByStatus(
                status == 0 ? "COMPLETED" : "OPEN", pageable
        );

        return result.getContent();
    }

    @Override
    public void exportDatabase(HttpServletResponse response, List<Sessions> sessions) throws IOException {
        if (!sessions.isEmpty()) {
            response.setContentType("application/octet-stream");
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String currentDateTime = dateFormatter.format(new Date());
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=session_" +
                    sessions.get(0).getStatus().toLowerCase() + "_" + currentDateTime + ".xlsx";
            response.setHeader(headerKey, headerValue);
            ExcelGenerator generator = new ExcelGenerator(sessions);
            generator.generateExcelFile(response);
        }
    }

    // Function
    private void addDatabase(Sessions sessions) {
        Optional<Sessions> sessionsOptional = sessionRepository.findBySessionID(sessions.getSessionId());
        if (sessionsOptional.isEmpty()) {
            sessionRepository.save(sessions);
        }
    }

    public List<Sessions> generateSessions(int numberOfSessions) {
        List<Sessions> sessionsList = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numberOfSessions; i++) {
            String id = UUID.randomUUID().toString();
            String username = usernames[random.nextInt(usernames.length)];
            String status = statuses[random.nextInt(statuses.length)];
            Sessions session = new Sessions(id, username, status);
            sessionsList.add(session);
        }

        return sessionsList;
    }
}
