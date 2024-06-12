package fpt.study.exportDB.services;


import fpt.study.exportDB.models.Sessions;

import java.util.List;

public interface DatabaseExecute {
    String initDB();

    String statusChange(String id);
    String deleteSession(String id);

    long countByStatus(int status);
    List<Sessions> findByStatus(int status, int page);
}
