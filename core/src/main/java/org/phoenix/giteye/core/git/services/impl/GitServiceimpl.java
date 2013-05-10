package org.phoenix.giteye.core.git.services.impl;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revplot.PlotCommit;
import org.eclipse.jgit.revplot.PlotCommitList;
import org.eclipse.jgit.revplot.PlotLane;
import org.eclipse.jgit.revplot.PlotWalk;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.phoenix.giteye.core.beans.BranchBean;
import org.phoenix.giteye.core.beans.CommitBean;
import org.phoenix.giteye.core.beans.RepositoryBean;
import org.phoenix.giteye.core.exceptions.json.NotInitializedRepositoryException;
import org.phoenix.giteye.core.git.services.GitService;
import org.phoenix.giteye.core.git.services.dao.GitDAO;
import org.phoenix.giteye.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.transform.impl.AddDelegateTransformer;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 2/27/13
 * Time: 11:32 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class GitServiceimpl implements GitService {
    private final static Logger logger = LoggerFactory.getLogger(GitServiceimpl.class);
    private final static int DEFAULT_MAX_COMMITS = 300;

    @Autowired
    private GitDAO gitDAO;

    @Override
    public List<BranchBean> getBranches(RepositoryBean repository) {

        List<BranchBean> branches= new ArrayList<BranchBean>();
        try {

            Repository repo = gitDAO.getRepository(repository.getPath());
            Iterable<Ref> iterable = gitDAO.getBranches(repo);

            for (Ref ref : iterable) {
                BranchBean branch = new BranchBean();
                String name = ref.getName();
                if (name.startsWith("refs/heads/")) {
                    name = name.replace("refs/heads/","");
                    branch.setName(name);
                    branch.setRemote(false);
                } else {
                    name = name.replace("refs/","");
                    branch.setName(name);
                    branch.setRemote(true);
                }
                branches.add(branch);
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoHeadException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (GitAPIException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return branches;
    }

    @Override
    public JsonCommitDetails getCommitDetails(RepositoryBean repository, String commitId) {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repo = null;

        JsonCommitDetails details = new JsonCommitDetails();
        try {
            repo = builder.setGitDir(new File(repository.getPath()))
                    .readEnvironment() // scan environment GIT_* variables
                    .findGitDir() // scan up the file system tree
                    .build();

            RevWalk revWalk = new RevWalk(repo);

            RevCommit commit = revWalk.parseCommit(repo.resolve(commitId));
            RevCommit parent = null;
            if (commit == null) {
                return null;
            }
            if (commit.getParentCount() == 0) {
                return null;
            }

            List<JsonDiff> differences = new ArrayList<JsonDiff>();
            for (int i=0; i<commit.getParentCount(); i++) {
                parent = revWalk.parseCommit(commit.getParent(i).getId());
                JsonDiffs diffSet = new JsonDiffs();
                diffSet.setParentCommitId(parent.getId().name());
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                DiffFormatter df = new DiffFormatter(out);
                df.setRepository(repo);
                df.setDiffComparator(RawTextComparator.DEFAULT);
                df.setDetectRenames(true);

                details.setId(commit.getId().name());
                details.setMessage(commit.getFullMessage());
                details.setCommitDate(new Date(commit.getCommitTime() * 1000L));
                details.setAuthorName(commit.getAuthorIdent().getName());
                details.setAuthorEmail(commit.getAuthorIdent().getEmailAddress());

                List<DiffEntry> diffs = df.scan(parent.getTree(), commit.getTree());
                for (DiffEntry diff : diffs) {
                    df.format(diff);
                    JsonDiff jdiff = new JsonDiff();
                    jdiff.setChangeName(diff.getChangeType().name());
                    jdiff.setNewMode(diff.getNewMode().getBits());
                    jdiff.setNewPath(diff.getNewPath());
                    jdiff.setOldMode(diff.getOldMode().getBits());
                    jdiff.setOldPath(diff.getOldPath());
                    //jdiff.setDiff();
                    String lines = out.toString("UTF-8");
                    StringTokenizer tokenizer = new StringTokenizer(lines,"\n");
                    JsonDiffChunk chunk = null;
                    int oldLineNumber = 0;
                    int newLineNumber = 0;
                    while (tokenizer.hasMoreTokens()) {
                        String line = tokenizer.nextToken();
                        if (line.startsWith("@@")) {
                            if (chunk != null) {
                                jdiff.addChunk(chunk);
                            }
                            chunk = new JsonDiffChunk();
                            line = line.replace("@@","").trim();
                            StringTokenizer spaceTokenizer = new StringTokenizer(line, " ");
                            while (spaceTokenizer.hasMoreTokens()) {
                                String token = spaceTokenizer.nextToken();
                                if (token.startsWith("-")) {
                                    token = token.substring(1);
                                    // old file diff part
                                    String[] elements = token.trim().split(",");
                                    chunk.setOldLineStart(Integer.parseInt(elements[0]));
                                    chunk.setOldLineRange(Integer.parseInt(elements[1]));
                                    oldLineNumber = chunk.getOldLineStart()-1;
                                } else {
                                    // new file diff part
                                    token = token.substring(1);
                                    String[] elements = token.trim().split(",");
                                    chunk.setNewLineStart(Integer.parseInt(elements[0]));
                                    chunk.setNewLineRange(Integer.parseInt(elements[1]));
                                    newLineNumber = chunk.getNewLineStart()-1;
                                }
                            }
                        } else if (chunk != null) {
                            JsonChunkLine chunkLine = new JsonChunkLine();
                            if (line.startsWith("-")) {
                                oldLineNumber++;
                                chunkLine.setOldLineNumber(oldLineNumber);
                                chunkLine.setNewLineNumber(0);
                                if (line.length() == 1) {
                                    line = " ";
                                } else {
                                    line = " "+line.substring(1);
                                }
                                chunkLine.setType(ChunkLineType.OLD);
                            } else if (line.startsWith("+")) {
                                newLineNumber++;
                                chunkLine.setNewLineNumber(newLineNumber);
                                chunkLine.setOldLineNumber(0);
                                if (line.length() == 1) {
                                    line = " ";
                                } else {
                                    line = " "+line.substring(1);
                                }
                                chunkLine.setType(ChunkLineType.NEW);
                            } else {
                                newLineNumber++;
                                oldLineNumber++;
                                chunkLine.setOldLineNumber(oldLineNumber);
                                chunkLine.setNewLineNumber(newLineNumber);
                                chunkLine.setType(ChunkLineType.COMMON);
                            }
                            chunkLine.setLine(line);
                            chunk.addLine(chunkLine);
                        }
                    }
                    if (chunk != null) {
                        jdiff.addChunk(chunk);
                    }
                    out.reset();
                    diffSet.addDifference(jdiff);
                }
                details.addDifferences(diffSet);
            }
        } catch (IOException ioe) {

        }
        return details;
    }

    private void addRepositoryRefs(JsonRepository jrep, Repository repo, String currentBranch) throws IOException{
        Map<String, Ref> refs = gitDAO.getAllRefs(repo);
        for (Map.Entry<String, Ref> refEntry : refs.entrySet()) {
            String refName = refEntry.getKey();
            Ref ref = refEntry.getValue();
            String name = refName;
            if (name.contains("/") && !name.contains("remotes") && !name.contains("tags")) {
                name = name.substring(name.lastIndexOf("/")+1);
            } else if (name.contains("tags")) {
                name = name.substring(name.lastIndexOf("/")+1);
                JsonTag tag = new JsonTag();
                tag.setName(name);
                tag.setTarget(ref.getTarget().getObjectId().name());
                tag.setRef(ref.getObjectId().name());
                jrep.addTag(tag);
                continue;
            } else if (name.contains("/")) {
                name = name.replace("refs/", "");
                name = name.replace("remotes/", "");
            }
            JsonBranch branch = new JsonBranch(name);
            branch.setRef(ref.getName());
            branch.setTarget(ref.getObjectId().name());
            if (currentBranch.equals(name)) {
                branch.setCurrent(true);
            }
            if (ref instanceof SymbolicRef) {
                branch.setSymbolic(true);
            }
            jrep.addBranch(branch);
        }

    }

    @Override
    public JsonRepository getLogAsJson(RepositoryBean repository, int pageSize, int page) throws NotInitializedRepositoryException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repo = null;
        int position = 0;
        int max = page*pageSize;
        int firstCommitPosition = (page-1)*pageSize;

        try {
            repo = gitDAO.getRepository(repository.getPath());

            JsonRepository jrep = new JsonRepository(repository.getDisplayName());

            // Retrieve refs
            addRepositoryRefs(jrep, repo, repository.getBranch());
            // Retrieve objects
            PlotWalk revWalk = new PlotWalk(repo);
            // First create a list of heads targets for all non symbolic refs
            List<RevCommit> heads = new ArrayList<RevCommit>();
            for (JsonBranch branch : jrep.getBranches()) {
                if (branch.isSymbolic()) {
                    continue;
                }
                String headId = branch.getTarget();
                RevCommit head = revWalk.lookupCommit(repo.resolve(headId));
                if (head == null) {
                    continue;
                }
                heads.add(head);
            }
            revWalk.markStart(heads);
            PlotCommitList<PlotLane> commits = new PlotCommitList<PlotLane>();
            commits.source(revWalk);
            commits.fillTo(Integer.MAX_VALUE);
            Iterator<PlotCommit<PlotLane>> iterator = commits.iterator();
            while (iterator.hasNext()) {
                PlotCommit revc = iterator.next();
                JsonCommit commit = new JsonCommit(revc.getId().name());
                commit.setShortMessage(revc.getShortMessage());
                commit.setMessage(revc.getFullMessage());
                commit.setDate(new Date(revc.getCommitTime() * 1000L));
                commit.setAuthorName(revc.getAuthorIdent().getName());
                commit.setAuthorEmail(revc.getAuthorIdent().getEmailAddress());
                commit.setCommitterName(revc.getCommitterIdent().getName());
                commit.setCommitterEmail(revc.getCommitterIdent().getEmailAddress());
                commit.setPosition(position);
                if (jrep.getCommitCount() >= max || jrep.getCommitCount() < firstCommitPosition) {
                    commit.setExtra(true);
                    commit.setDisposable(true);
                }
                if (revc.getLane() == null) {
                    commit.setLane(0);
                } else {
                    commit.setLane(revc.getLane().getPosition());
                }
                position++;
                jrep.addCommit(commit);
                if (revc.getParents() == null || revc.getParents().length == 0) {
                    continue;
                }
                for (RevCommit parent : revc.getParents()) {
                    commit.addParent(parent.getId().name());
                }
            }
            // set children
            for (JsonCommit commit : jrep.getCommits()) {
                if (!CollectionUtils.isEmpty(commit.getParents())) {
                    for (String parent : commit.getParents()) {
                        JsonCommit parentCommit = jrep.getCommit(parent);
                        if (parentCommit.isDisposable() && (!commit.isExtra())) {
                            parentCommit.setDisposable(false);
                            logger.warn("Keeping extra commit "+parentCommit.getId());
                        } else if (commit.isDisposable() && (!parentCommit.isExtra())) {
                            commit.setDisposable(false);
                        }
                        if (parentCommit == null) {
                            logger.error("Could not get JsonCommit with id "+parent);
                        } else {
                            JsonCommitChild child = new JsonCommitChild(commit.getId());
                            child.setLane(commit.getLane());
                            child.setPosition(commit.getPosition());
                            parentCommit.addChild(child);
                        }
                    }
                }
                commit.resetParents();
            }
            revWalk.release();
            int nbPages = (int) Math.ceil(jrep.getCommitCount() / pageSize);
            jrep.setCurrentPage(page);
            jrep.setMaxPage(nbPages);
            logger.info("Before cleaning : Json Repository contains " + jrep.getCommitCount() + " commits ("+nbPages+").");
            jrep.tidy();
            logger.info("After cleaning : Json Repository contains "+jrep.getCommitCount()+" commits.");
            return jrep;

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }
}
