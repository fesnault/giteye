package org.phoenix.giteye.web.controllers;

import org.phoenix.giteye.core.beans.GitLogRequest;
import org.phoenix.giteye.core.beans.RepositoryBean;
import org.phoenix.giteye.core.beans.RepositoryConfig;
import org.phoenix.giteye.core.exceptions.json.NotInitializedRepositoryException;
import org.phoenix.giteye.core.git.services.GitService;
import org.phoenix.giteye.core.git.services.RepositoryService;
import org.phoenix.giteye.json.JsonCommitDetails;
import org.phoenix.giteye.json.JsonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Git Commands controller.
 * @author phoenix
 */
@Controller
@RequestMapping(value = "/git")
public class GitController {
    private final static Logger logger = LoggerFactory.getLogger(GitController.class);
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private GitService gitService;

    @RequestMapping(value = "/branches.do")
    public String showBranches(HttpSession session, Model model) {
        RepositoryConfig selectedRepository = (RepositoryConfig)session.getAttribute("repository");
        if (selectedRepository == null) {
            return "redirect:/repositories/list.do";
        }
        RepositoryBean bean = repositoryService.getRepositoryInformation(selectedRepository.getLocation());
        model.addAttribute("branches", gitService.getBranches(bean));
        model.addAttribute("gitRepository", bean);
        return "git/branches";
    }

    @RequestMapping(value = "/json/graph/{max}/{page}/log.do", produces = "application/json")
    public @ResponseBody JsonRepository getLogAsJson(HttpSession session, @PathVariable int max, @PathVariable int page) {
            RepositoryConfig selectedRepository = (RepositoryConfig)session.getAttribute("repository");
        if (selectedRepository == null) {
            return null;
        }
        RepositoryBean bean = repositoryService.getRepositoryInformation(selectedRepository.getLocation());
        JsonRepository jrep = null;
        try {
            jrep = gitService.getLogAsJson(bean, max, page);
        } catch (NotInitializedRepositoryException notInitializedRepositoryException) {
            logger.error("Error while retrieving json repository", notInitializedRepositoryException);
        }
        return jrep;
    }

    @RequestMapping(value = "/json/graph/log2.do", produces = "application/json", consumes = "application/json")
    public @ResponseBody JsonRepository getLogAsJson(HttpSession session, @RequestBody GitLogRequest logRequest) {
        RepositoryConfig selectedRepository = (RepositoryConfig)session.getAttribute("repository");
        if (selectedRepository == null) {
            return null;
        }
        RepositoryBean bean = repositoryService.getRepositoryInformation(selectedRepository.getLocation());
        JsonRepository jrep = null;
        try {
            jrep = gitService.getLogAsJson(bean, logRequest);
        } catch (NotInitializedRepositoryException notInitializedRepositoryException) {
            logger.error("Error while retrieving json repository", notInitializedRepositoryException);
        }
        return jrep;
    }

    @RequestMapping(value = "/json/commit/{commitId}/details.do")
    public @ResponseBody JsonCommitDetails getCommitDetails(HttpSession session, @PathVariable String commitId) {
        RepositoryConfig selectedRepository = (RepositoryConfig)session.getAttribute("repository");
        if (selectedRepository == null) {
            return null;
        }
        RepositoryBean bean = repositoryService.getRepositoryInformation(selectedRepository.getLocation());
        JsonCommitDetails details = null;
        try {
            details = gitService.getCommitDetails(bean, commitId);
        } catch (NotInitializedRepositoryException nire) {

        }
        return details;
    }



}
