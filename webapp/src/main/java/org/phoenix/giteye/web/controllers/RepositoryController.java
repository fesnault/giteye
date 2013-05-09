package org.phoenix.giteye.web.controllers;

import org.phoenix.giteye.core.beans.RepositoryBean;
import org.phoenix.giteye.core.beans.RepositoryConfig;
import org.phoenix.giteye.core.exceptions.RepositoryPersistenceException;
import org.phoenix.giteye.core.git.services.GitService;
import org.phoenix.giteye.core.git.services.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Git Repository controller.
 * @author phoenix
 */
@Controller
@RequestMapping(value = "/repositories")
public class RepositoryController {
    private final static Logger logger = LoggerFactory.getLogger(RepositoryController.class);
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private GitService logService;

    @RequestMapping(value = "/add.do", method = RequestMethod.GET)
    public String showForm(@ModelAttribute RepositoryConfig repositoryConfig) {
        return "repositories/add";
    }

    @RequestMapping(value = "/add.do", method = RequestMethod.POST)
    public String addRepository(@ModelAttribute RepositoryConfig repositoryConfig, Model model, RedirectAttributes redirectAttributes) {
        try {
            repositoryService.saveRepository(repositoryConfig);
        } catch (RepositoryPersistenceException rpe) {
            logger.error("Could not save repository ", rpe);
            redirectAttributes.addFlashAttribute("error", rpe.getMessage());
            redirectAttributes.addFlashAttribute(repositoryConfig);
            return "redirect:/repositories/add.do";
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/list.do")
    public void listRepositories(Model model) {
        List<RepositoryConfig> repositories = repositoryService.getAllRepositories();
        model.addAttribute("repositories", repositories);
    }

    @RequestMapping(value = "/select.do")
    public String selectRepository(@ModelAttribute RepositoryConfig repository, HttpSession session) {
        logger.info("Switched to repository "+repository.getName());
        session.setAttribute("repository", repository);
        return "redirect:/";
    }
}
