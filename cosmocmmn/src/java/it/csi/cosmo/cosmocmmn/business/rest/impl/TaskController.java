/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business.rest.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.flowable.cmmn.engine.CmmnEngineConfiguration;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.identitylink.api.IdentityLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.JsonNode;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmocmmn.dto.rest.AssegnaTaskRequest;
import it.csi.cosmo.cosmocmmn.dto.rest.AssegnaTaskRequestAssegnazione;
import it.csi.cosmo.cosmocmmn.dto.rest.AssegnaTaskResponse;
import it.csi.cosmo.cosmocmmn.util.ComplexListComparator;
import it.csi.cosmo.cosmocmmn.util.ComplexListComparator.DifferentListsDifference;

@RestController
@RequestMapping("/cosmo/task")
public class TaskController {

  @Autowired
  ProcessEngineConfiguration pec;

  @Autowired
  CmmnEngineConfiguration cmmnEngineConfiguration;

  @Transactional(readOnly = false)
  @RequestMapping("/{taskId}/assegna")
  public AssegnaTaskResponse riassegna(@PathVariable String taskId,
      @RequestBody AssegnaTaskRequest body) {

    ValidationUtils.require(taskId, "taskId");
    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);

    if (body.getAssegnazioni() == null) {
      body.setAssegnazioni(Collections.emptyList());
    }
    
    AssegnaTaskResponse output = new AssegnaTaskResponse();

    TaskService taskService = pec.getTaskService();

    List<IdentityLink> currentIdentityLinks = taskService.getIdentityLinksForTask(taskId);

    DifferentListsDifference<IdentityLink, AssegnaTaskRequestAssegnazione> compareResult =
        ComplexListComparator.compareLists(currentIdentityLinks, body.getAssegnazioni(),
            (il, ass) -> {
              return Objects.equals(il.getUserId(), ass.getCodiceFiscaleUtente())
                  && Objects.equals(il.getGroupId(), ass.getCodiceGruppo())
                  && Objects.equals(il.getType(), ass.getTipologia());
            });

    if (Boolean.TRUE.equals(body.isEsclusivo())) {
      compareResult.onElementsInFirstNotInSecond(toRemove -> {
        if (toRemove.getUserId() != null) {
          taskService.deleteUserIdentityLink(taskId, toRemove.getUserId(), toRemove.getType());
        }
        if (toRemove.getGroupId() != null) {
          taskService.deleteGroupIdentityLink(taskId, toRemove.getGroupId(), toRemove.getType());
        }
      });
    }

    compareResult.onElementsInSecondNotInFirst(toInsert -> {
      if (toInsert.getCodiceFiscaleUtente() != null) {
        taskService.addUserIdentityLink(taskId, toInsert.getCodiceFiscaleUtente(),
            toInsert.getTipologia());
      }
      if (toInsert.getCodiceGruppo() != null) {
        taskService.addGroupIdentityLink(taskId, toInsert.getCodiceGruppo(),
            toInsert.getTipologia());
      }
    });

    return output;
  }

  @RequestMapping("/{executionId}/test-9125")
  public Object testJson(@PathVariable String executionId) {

    Map<String, Object> map = new HashMap<>();
    map.put("k1", "v1");


    Map<String, Object> map2 = new HashMap<>();
    map2.put("k2", "v2");

    map.put("nested", map2);

    RuntimeService rs = pec.getRuntimeService();

    var toInsert = ObjectUtils.getDataMapper().convertValue(map, JsonNode.class);

    rs.setVariable(executionId, "test_raw", map);
    rs.setVariable(executionId, "test_as_node", toInsert);

    return map;
  }

}
