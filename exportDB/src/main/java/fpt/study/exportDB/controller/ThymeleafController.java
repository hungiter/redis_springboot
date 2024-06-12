package fpt.study.exportDB.controller;

import fpt.study.exportDB.services.DatabaseServices;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

// RESTCONTROLLER cannot use template
@Controller
@RequestMapping("/database/export")
public class ThymeleafController {
    @Autowired
    DatabaseServices databaseServices;

    @GetMapping("/{status}")
    public String getExportPage(@PathVariable String status, Model model) {
        try {
            // Page start at 0
            int statusInt = Integer.parseInt(status);
            if (statusInt != 0 & statusInt != 1) {
                // Set error message
                model.addAttribute("errorMessage", "An error occurred! /database/export/{0,1}");

                // Set redirect URL
                model.addAttribute("redirectUrl", "/database/export/0"); // Example redirecting to the homepage

                return "error";
            }

            // Export database data
            long totalPage = databaseServices.countByStatus(statusInt) + 1;
            // Add totalPage to the model
            model.addAttribute("totalPage", totalPage);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return "download";
    }

    @PostMapping("/download")
    public void export(HttpServletResponse response,
                       @RequestParam("status") String status,
                       @RequestParam("page") String page) throws IOException {
        try {
            // Page start at 0
            int statusInt = Integer.parseInt(status);
            int pageInt = Integer.parseInt(page);

            // Export database data
            if (statusInt == 0 || statusInt == 1) {
                databaseServices.exportDatabase(response, databaseServices.findByStatus(statusInt, pageInt));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
