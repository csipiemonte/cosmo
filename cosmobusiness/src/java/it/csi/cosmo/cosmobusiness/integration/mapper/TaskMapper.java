/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.mapper;

import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.flowable.rest.service.api.runtime.task.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.cosmobusiness.dto.rest.Processo;
import it.csi.cosmo.cosmobusiness.dto.rest.Task;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring",
    uses = {DateFormatsMapper.class})
public interface TaskMapper {

  @Mapping(target = "action", ignore = true)
  @Mapping(target = "execution", ignore = true)
  @Mapping(target = "processDefinition", ignore = true)
  @Mapping(target = "processInstance", ignore = true)
  @Mapping(target = "cancellationDate", ignore = true)
  @Mapping(target = "subtasks", ignore = true)
  Task toTask(TaskResponse t);

  Processo toProcesso(ProcessInstanceResponse processInstanceResponse);
}
