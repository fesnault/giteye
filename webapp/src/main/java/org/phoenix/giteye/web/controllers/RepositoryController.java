package org.phoenix.giteye.web.controllers;

import org.phoenix.giteye.core.beans.RepositoryBean;
import org.phoenix.giteye.core.git.services.LogService;
import org.phoenix.giteye.core.git.services.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Git Repository controller.
 * @author phoenix
 */
@Controller
@SessionAttributes("repository")
public class RepositoryController {
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private LogService logService;

    @RequestMapping(value = "/repository.do")
    public void showRepository(Model model) {
        RepositoryBean bean = repositoryService.getRepositoryInformation("/Users/phoenix/data/sources/giteye");
        model.addAttribute("repository", bean);
        model.addAttribute("log", logService.getLog(bean));
    }
}
