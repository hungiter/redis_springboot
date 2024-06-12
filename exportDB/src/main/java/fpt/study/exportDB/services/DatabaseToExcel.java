package fpt.study.exportDB.services;

import fpt.study.exportDB.models.Sessions;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public interface DatabaseToExcel {
    void exportDatabase(HttpServletResponse response, List<Sessions> sessions) throws IOException;
}
