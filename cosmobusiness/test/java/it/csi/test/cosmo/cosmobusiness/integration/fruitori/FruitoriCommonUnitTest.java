/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.integration.fruitori;

import static org.junit.Assert.assertTrue;
import java.security.InvalidParameterException;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.cosmobusiness.business.service.impl.PracticeServiceImpl;
import it.csi.test.cosmo.cosmobusiness.integration.fruitori.FruitoriCommonUnitTest.TestConfig;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;
import it.csi.test.cosmo.cosmobusiness.testbed.model.ParentUnitTest;
import it.csi.test.cosmo.cosmobusiness.testbed.service.EnteTestbedService;
import it.csi.test.cosmo.cosmobusiness.testbed.service.PraticaTestbedService;

/**
 *
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoBusinessUnitTestInMemory.class, TestConfig.class})
@Transactional()
public class FruitoriCommonUnitTest extends ParentUnitTest {

  @Configuration
  public static class TestConfig {
  }

  private TestClassBridge instance;

  @Autowired
  private AutowireCapableBeanFactory beanFactory;

  @Autowired
  private PraticaTestbedService praticaTestbedService;

  @Autowired
  private EnteTestbedService enteTestbedService;

  private CosmoTEnte ente;

  @Before
  public void prepare() {
    instance = new TestClassBridge();

    ente = enteTestbedService.creaEnte();

    beanFactory.autowireBean(instance);
    beanFactory.initializeBean(instance, "TestClassBridge");
  }

  @Test
  public void recuperaBusinessKeyShouldFailWithoutPraticaOrWithoutId() {
    expect(() -> instance.recuperaBusinessKey(null), InvalidParameterException.class);

    CosmoTPratica p = new CosmoTPratica();
    p.setId(null);
    expect(() -> instance.recuperaBusinessKey(p), InvalidParameterException.class);
  }

  @Test
  public void recuperaProcessDefinitionKeyShouldFailWithoutPratica() {
    expect(() -> instance.recuperaProcessDefinitionKey(null), InvalidParameterException.class);
  }

  @Test
  public void recuperaProcessDefinitionKeyShouldFailWithoutTipoPratica() {

    CosmoTPratica pratica = praticaTestbedService.creaPratica(ente, null, null);

    expect(() -> instance.recuperaProcessDefinitionKey(pratica), InternalServerException.class,
        e -> Arrays.asList("pratica", "non", "associata", "tipologia").forEach(
            token -> assertTrue(e.getMessage().toUpperCase().contains(token.toUpperCase()))));
  }

  private static class TestClassBridge extends PracticeServiceImpl {

    @Override
    public String recuperaProcessDefinitionKey(CosmoTPratica pratica) {
      return super.recuperaProcessDefinitionKey(pratica);
    }

    @Override
    public String recuperaBusinessKey(CosmoTPratica pratica) {
      return super.recuperaBusinessKey(pratica);
    }
  }
}
