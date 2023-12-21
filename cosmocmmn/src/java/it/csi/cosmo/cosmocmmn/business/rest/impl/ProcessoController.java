/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business.rest.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;
import org.flowable.cmmn.engine.CmmnEngineConfiguration;
import org.flowable.engine.ProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import it.csi.cosmo.common.util.ValidationUtils;

@RestController
@RequestMapping("/cosmo/processi")
public class ProcessoController {

  @Autowired
  ProcessEngineConfiguration pec;

  @Autowired
  CmmnEngineConfiguration cmmnEngineConfiguration;

  @Transactional(readOnly = false)
  @PostMapping("/deploy")
  public Object deployProcesso(@RequestParam("tenantId") String tenantId,
      @RequestParam("processDefinitionKey") String processDefinitionKey,
      @RequestParam("file") MultipartFile file) throws IOException {

    ValidationUtils.require(tenantId, "tenantId");
    ValidationUtils.require(processDefinitionKey, "processDefinitionKey");
    ValidationUtils.require(file, "file");

    ZipInputStream inputStream = new ZipInputStream(new ByteArrayInputStream(file.getBytes()));

    var res = pec.getRepositoryService().createDeployment().name(processDefinitionKey + ".bar")
        .tenantId(tenantId).key(processDefinitionKey).addZipInputStream(inputStream).deploy();

    return res;
  }

}
