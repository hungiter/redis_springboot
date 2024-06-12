package fpt.study.exportDB.controller;

import fpt.study.exportDB.services.DatabaseServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/database/import")
public class ApiController {
    @Autowired
    DatabaseServices databaseServices;

    @GetMapping("/")
    public String init() {
        return databaseServices.initDB();
    }
}
