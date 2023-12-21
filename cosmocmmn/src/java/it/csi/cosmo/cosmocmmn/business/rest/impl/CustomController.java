/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business.rest.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.flowable.cmmn.engine.CmmnEngineConfiguration;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pratiche")
public class CustomController {

  @Autowired
  ProcessEngineConfiguration pec;

  @Autowired
  CmmnEngineConfiguration cmmnEngineConfiguration;

  @RequestMapping("/users/{id}/shared")
  public Map<String, Object> shared(@PathVariable String id) {

    Map<String, Object> m = new HashMap<>();

    List<Task> tasks = cmmnEngineConfiguration.getCmmnTaskService()
        .createTaskQuery()
        .active()
        .taskInvolvedUser(id)
        .orderByTaskDueDate()
        .orderByDueDateNullsLast()
        .asc()
        .list()
        .stream()
        .filter(t->!id.equals(t.getAssignee()))
        .collect(Collectors.toList());

    if (tasks.isEmpty()) {
      return m;
    }
    return getBusinessKeyFromTask(tasks);
  }


  @RequestMapping("/users/{id}/candidate")
  public Map<String, Object> candidate(@PathVariable String id,@RequestParam(value = "idGruppo", required = false, defaultValue = "") String idGruppo) {

    Map<String, Object> m = new HashMap<>();

    List<Task> tasksCandidateUser = cmmnEngineConfiguration.getCmmnTaskService()
        .createTaskQuery()
        .active()
        .taskCandidateUser(id)
        .orderByTaskDueDate()
        .orderByDueDateNullsLast()
        .asc()
        .list()
        .stream()
        .collect(Collectors.toList());

    List<Task> tasksCandidateGroup = new ArrayList();
    if(idGruppo != null && !idGruppo.equals("")) {
      tasksCandidateGroup = getTasksCandidateForGroups(idGruppo);
    }
    tasksCandidateUser.addAll(tasksCandidateGroup);
    if (tasksCandidateUser.isEmpty()) {
      return m;
    }
    return getBusinessKeyFromTask(tasksCandidateUser);
  }

  private List<Task> getTasksCandidateForGroups(String idGruppo) {
    List<String> ls = new ArrayList();
    Collections.addAll(ls, idGruppo.split(","));

    return cmmnEngineConfiguration.getCmmnTaskService()
        .createTaskQuery()
        .active()
        .taskCandidateGroupIn(ls)
        .orderByTaskDueDate()
        .orderByDueDateNullsLast()
        .asc()
        .list()
        .stream()
        .collect(Collectors.toList());
  }


  @RequestMapping("/users/{id}/sharedCase")
  public Set<String> sharedCase(@PathVariable String id) {

    Set<String> caseIds = cmmnEngineConfiguration.getCmmnTaskService().createTaskQuery().active().taskInvolvedUser(id)
        .includeIdentityLinks().list().stream().filter(t->!id.equals(t.getAssignee()))
        .map(t -> t.getScopeId()).collect(Collectors.toSet());

    if (caseIds.isEmpty()) {
      return Collections.emptySet();
    }


    return new HashSet<>(bksCase(caseIds).values());

  }

  @RequestMapping("/users/{id}/sharedProcess")
  public Set<String> sharedProcess(@PathVariable String id) {
    Set<String> procInstIds = pec.getTaskService().createTaskQuery().active().includeProcessVariables()
        .taskInvolvedUser(id).list().stream().filter(t -> !id.equals(t.getAssignee()))
        .map(t -> t.getProcessInstanceId()).collect(Collectors.toSet());

    if (procInstIds.isEmpty()) {
      return Collections.emptySet();
    }

    return new HashSet<>(bks(procInstIds).values());
  }

  @RequestMapping("/users/{id}/todo")
  public Map<String, Object> todo(@PathVariable String id) {
    List<Task> tasks = cmmnEngineConfiguration.getCmmnTaskService()
        .createTaskQuery()
        .active()
        .taskAssignee(id)
        .orderByTaskDueDate()
        .orderByDueDateNullsLast()
        .asc()
        .list();

    if (tasks.isEmpty()) {
      return new HashMap<>();
    }
    return getBusinessKeyFromTask(tasks);
  }

  private Map<String, Object> getBusinessKeyFromTask(List<Task> tasks) {
    Map<String, Object> m = new HashMap<>();
    Set<String> caseIds = tasks.stream().map(t -> t.getScopeId()).collect(Collectors.toSet());
    Map<String, String> entry = bksCase(caseIds);
    entry.forEach(
        (proc, bk) -> m.put(bk, tasks.stream().filter(t -> proc.equals(t.getScopeId())).map(t -> {
          HashMap<String, String> r = new HashMap<>();
          r.put("id", t.getId());
          r.put("nome", t.getName());
          r.put("descrizione", t.getDescription());
          return r;
        }).collect(Collectors.toList())));
    return m;
  }


  @RequestMapping("/users/{id}/todoProcess")
  public Map<String, Object> todoProcess(@PathVariable String id) {

    Map<String, Object> m = new HashMap<>();

    List<Task> tasks = pec.getTaskService().createTaskQuery().active().taskAssignee(id).orderByTaskDueDate()
        .orderByDueDateNullsLast().asc().list();

    if (tasks.isEmpty()) {
      return m;
    }
    Set<String> procInstIds = tasks.stream().map(t -> t.getProcessInstanceId()).collect(Collectors.toSet());

    Map<String, String> entry = bks(procInstIds);

    entry.forEach(
        (proc, bk) -> m.put(bk, tasks.stream().filter(t -> proc.equals(t.getProcessInstanceId())).map(t -> {
          HashMap<String, String> r = new HashMap<>();
          r.put("id", t.getId());
          r.put("nome", t.getName());
          r.put("descrizione", t.getDescription());
          return r;
        }).collect(Collectors.toList())));

    return m;
  }

  private Map<String, String> bks(Set<String> procInstIds) {
    return pec.getRuntimeService().createProcessInstanceQuery().active().processInstanceIds(procInstIds).list()
        .stream().collect(Collectors.toMap(p -> p.getId(), p -> p.getBusinessKey()));
  }

  private Map<String, String> bksCase(Set<String> scopeIds) {
    return cmmnEngineConfiguration.getCmmnRuntimeService().createCaseInstanceQuery().caseInstanceIds(scopeIds).list()
        .stream().collect(Collectors.toMap(p -> p.getId(), p -> p.getBusinessKey()));
  }

}
