package org.phoenix.giteye.web.controllers;

import org.phoenix.giteye.core.git.services.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Git Repository controller.
 * @author phoenix
 */
@Controller
public class RepositoryController {
    @Autowired
    private RepositoryService repositoryService;

    @RequestMapping(value = "/repository.do")
    public void showRepository(Model model) {
        repositoryService.getRepositoryInformation("/home/phoenix/data/sources/incubation/sandbox");
        model.addAttribute("repoPath", "/home/phoenix/data/sources/incubation/sandbox" );
    }
}
