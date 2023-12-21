/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoauthorization.business.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.ManagedException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmoauthorization.business.service.PreferenzeEnteService;
import it.csi.cosmo.cosmoauthorization.dto.rest.Preferenza;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.CosmoAuthorizationUnitTestInMemory;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoauthorization.testbed.model.ParentIntegrationTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoAuthorizationUnitTestInMemory.class})
@Transactional
public class PreferenzeEnteServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private PreferenzeEnteService preferenzeEnteService;
  private String valore =
      "{\"header\": \"Test cambio header\", "
          + "\"logo\": \"iVBORw0KGgoAAAANSUhEUgAAACAAAAAKCAYAAADVTVykAAAE4klEQVQ4T22Ra0xTdxjG/6ctp+1qCwVPnUJpkUqdApnXRagoVxdr2DRrcVObuErCFJ33kdCxsGmFBVMnC2I2axrYNBNRM9wwyuS2ZUVrmWPSpu3BtthTWmnX27pezjkL3Yd92fPhzfvhTZ7f+zzQ788sLw2/PQMUEgc5OdmgfPNGsKDvB4eB3+cnaHQmZRmy6PHJ40f3C4XCYhRFTUaj8UVtbW0JBEFJo9E4vXC/ZcuWYqfTOReNRnE6nU4bHR21KhQKyZMnT57hOM6Ix+OEzWZz1dTUvJZIJKgcDofl8XhCUNsFLan6TAPW1uwGwvwVIC9PCCCAgz9MKHCYjMBqGAEtJ98fs07rT9BozMKVK1ciOp2uX6FQvHP58mUdgiCLqqurK3Q63V2RSMRavnx5AUEQzKysLOrg4OCEVCqVzMzMPK+vr9+7a9eu4+Xl5QVisXj95OTkU4/H44bavtASre2XAH9FIXijUABamo8DPB4DjafOAIfbT3icNorqmGJsdKhfuX37zn0Oh8Pe1dXVr1arG8fHx0cJgmDn5OTwOjo6riqVyrJIJELh8/kiBEEyT58+/blSqXyPy+Um2Ww2k8vlLtZqtVc2bdq0A0GQNIPB8Cuk7b1Fdn59HSRjfwNJyQbQ1dEMAEmAt/Z8CByzLoLFTqfs3VkxeruvVymrq6t3zc5aWlpatBqN5qO2traL1dXVGyQSSVlDQ8OnnZ2dH+v1+kccDidr1apVuY2NjeeOHDmyj8lkxnp6egba29ubpqenTR6PJ46iqMPr9VoggiBIgiQBBEEASrW/MMnUhhMEQYEgytTU1Ehra+spFouVyWAwQDKZBAKBIN9oNBrC4bBfJBIt9BpNS0ujx2KxBJVKBRiGufl8/quhUCiGYZgLx3HYbDajpaWl6xAE4YVCoRAEQWGIJMl/3f5fBACAgqKoXi6X78jIyFg2NDQ0VVZWlo/jeCwjIwNxuVxYKBQKR6ML/mk4jUYjGAwGE8dxCoPBgOx2e9jn80UqKyvzI5FIHIbhhachFovFjkQiUejazR/Jq9/eSdlvWLManGluBIlEAnxw8ixwYV4ChmFKbc3GkaGBWydWv762IpmMB3Ec/9Pn8wG/3/9yfn7edvjw4aNSqfSYRqM56Ha7vU6n05tIJAgejwdXVVVtbmpq6i4uLhYIhcJlCIKkYxg2y2azuQaD4RHUduEKqTr7ZQpgW6UEDFy7CGKxOFhX8S6woU4CptMpn5zYN/LDne8OVlVt2/bihcOG43gCwzBQVFSEdHd39x84cKAuGAzOicViNkmSwr6+vqGxsbGn58+fPzg/P2/Ozs4uUqlUlwoKCngCgYA3PDxsVavVu2/cuHEXOqf5D+DNylIwcL0zBbC2fDewzTgJGKZTVEf3PJx89LAZUOlLrvX23lar1fvNZnNwbm5uLhAIzGRmZgrkcvlmm82G0mi0dJPJ9NxisaAymayip6fnpkwmqzOZTA673W4rLCzM1+v1FqlUuv7Bgwc/Qb9MTJLDPz9OJZAv5AP52zUgmcSB9pvbwOcPpCpYIUDGdVe/aggGg2n379+fOnToUHk4HP4LhmFmJBLxBwKBmMlk8pSUlBQ4nU43lUp9ZenSpekTExPTeXl5S+7du2cRi8WLORwOIzc3N9NqtWJbt25d4/V6Pf8A4sxmiG4jkaIAAAAASUVORK5CYII=\","
          + "\"logoFooter\": \"iVBORw0KGgoAAAANSUhEUgAAACAAAAAKCAYAAADVTVykAAAE4klEQVQ4T22Ra0xTdxjG/6ctp+1qCwVPnUJpkUqdApnXRagoVxdr2DRrcVObuErCFJ33kdCxsGmFBVMnC2I2axrYNBNRM9wwyuS2ZUVrmWPSpu3BtthTWmnX27pezjkL3Yd92fPhzfvhTZ7f+zzQ788sLw2/PQMUEgc5OdmgfPNGsKDvB4eB3+cnaHQmZRmy6PHJ40f3C4XCYhRFTUaj8UVtbW0JBEFJo9E4vXC/ZcuWYqfTOReNRnE6nU4bHR21KhQKyZMnT57hOM6Ix+OEzWZz1dTUvJZIJKgcDofl8XhCUNsFLan6TAPW1uwGwvwVIC9PCCCAgz9MKHCYjMBqGAEtJ98fs07rT9BozMKVK1ciOp2uX6FQvHP58mUdgiCLqqurK3Q63V2RSMRavnx5AUEQzKysLOrg4OCEVCqVzMzMPK+vr9+7a9eu4+Xl5QVisXj95OTkU4/H44bavtASre2XAH9FIXijUABamo8DPB4DjafOAIfbT3icNorqmGJsdKhfuX37zn0Oh8Pe1dXVr1arG8fHx0cJgmDn5OTwOjo6riqVyrJIJELh8/kiBEEyT58+/blSqXyPy+Um2Ww2k8vlLtZqtVc2bdq0A0GQNIPB8Cuk7b1Fdn59HSRjfwNJyQbQ1dEMAEmAt/Z8CByzLoLFTqfs3VkxeruvVymrq6t3zc5aWlpatBqN5qO2traL1dXVGyQSSVlDQ8OnnZ2dH+v1+kccDidr1apVuY2NjeeOHDmyj8lkxnp6egba29ubpqenTR6PJ46iqMPr9VoggiBIgiQBBEEASrW/MMnUhhMEQYEgytTU1Ehra+spFouVyWAwQDKZBAKBIN9oNBrC4bBfJBIt9BpNS0ujx2KxBJVKBRiGufl8/quhUCiGYZgLx3HYbDajpaWl6xAE4YVCoRAEQWGIJMl/3f5fBACAgqKoXi6X78jIyFg2NDQ0VVZWlo/jeCwjIwNxuVxYKBQKR6ML/mk4jUYjGAwGE8dxCoPBgOx2e9jn80UqKyvzI5FIHIbhhachFovFjkQiUejazR/Jq9/eSdlvWLManGluBIlEAnxw8ixwYV4ChmFKbc3GkaGBWydWv762IpmMB3Ec/9Pn8wG/3/9yfn7edvjw4aNSqfSYRqM56Ha7vU6n05tIJAgejwdXVVVtbmpq6i4uLhYIhcJlCIKkYxg2y2azuQaD4RHUduEKqTr7ZQpgW6UEDFy7CGKxOFhX8S6woU4CptMpn5zYN/LDne8OVlVt2/bihcOG43gCwzBQVFSEdHd39x84cKAuGAzOicViNkmSwr6+vqGxsbGn58+fPzg/P2/Ozs4uUqlUlwoKCngCgYA3PDxsVavVu2/cuHEXOqf5D+DNylIwcL0zBbC2fDewzTgJGKZTVEf3PJx89LAZUOlLrvX23lar1fvNZnNwbm5uLhAIzGRmZgrkcvlmm82G0mi0dJPJ9NxisaAymayip6fnpkwmqzOZTA673W4rLCzM1+v1FqlUuv7Bgwc/Qb9MTJLDPz9OJZAv5AP52zUgmcSB9pvbwOcPpCpYIUDGdVe/aggGg2n379+fOnToUHk4HP4LhmFmJBLxBwKBmMlk8pSUlBQ4nU43lUp9ZenSpekTExPTeXl5S+7du2cRi8WLORwOIzc3N9NqtWJbt25d4/V6Pf8A4sxmiG4jkaIAAAAASUVORK5CYII=\","
          + "\"logoFooterCentrale\": \"\", \"logoFooterDestra\": \"\","
          + "\"iconaFea\": \"iVBORw0KGgoAAAANSUhEUgAAACAAAAAKCAYAAADVTVykAAAE4klEQVQ4T22Ra0xTdxjG/6ctp+1qCwVPnUJpkUqdApnXRagoVxdr2DRrcVObuErCFJ33kdCxsGmFBVMnC2I2axrYNBNRM9wwyuS2ZUVrmWPSpu3BtthTWmnX27pezjkL3Yd92fPhzfvhTZ7f+zzQ788sLw2/PQMUEgc5OdmgfPNGsKDvB4eB3+cnaHQmZRmy6PHJ40f3C4XCYhRFTUaj8UVtbW0JBEFJo9E4vXC/ZcuWYqfTOReNRnE6nU4bHR21KhQKyZMnT57hOM6Ix+OEzWZz1dTUvJZIJKgcDofl8XhCUNsFLan6TAPW1uwGwvwVIC9PCCCAgz9MKHCYjMBqGAEtJ98fs07rT9BozMKVK1ciOp2uX6FQvHP58mUdgiCLqqurK3Q63V2RSMRavnx5AUEQzKysLOrg4OCEVCqVzMzMPK+vr9+7a9eu4+Xl5QVisXj95OTkU4/H44bavtASre2XAH9FIXijUABamo8DPB4DjafOAIfbT3icNorqmGJsdKhfuX37zn0Oh8Pe1dXVr1arG8fHx0cJgmDn5OTwOjo6riqVyrJIJELh8/kiBEEyT58+/blSqXyPy+Um2Ww2k8vlLtZqtVc2bdq0A0GQNIPB8Cuk7b1Fdn59HSRjfwNJyQbQ1dEMAEmAt/Z8CByzLoLFTqfs3VkxeruvVymrq6t3zc5aWlpatBqN5qO2traL1dXVGyQSSVlDQ8OnnZ2dH+v1+kccDidr1apVuY2NjeeOHDmyj8lkxnp6egba29ubpqenTR6PJ46iqMPr9VoggiBIgiQBBEEASrW/MMnUhhMEQYEgytTU1Ehra+spFouVyWAwQDKZBAKBIN9oNBrC4bBfJBIt9BpNS0ujx2KxBJVKBRiGufl8/quhUCiGYZgLx3HYbDajpaWl6xAE4YVCoRAEQWGIJMl/3f5fBACAgqKoXi6X78jIyFg2NDQ0VVZWlo/jeCwjIwNxuVxYKBQKR6ML/mk4jUYjGAwGE8dxCoPBgOx2e9jn80UqKyvzI5FIHIbhhachFovFjkQiUejazR/Jq9/eSdlvWLManGluBIlEAnxw8ixwYV4ChmFKbc3GkaGBWydWv762IpmMB3Ec/9Pn8wG/3/9yfn7edvjw4aNSqfSYRqM56Ha7vU6n05tIJAgejwdXVVVtbmpq6i4uLhYIhcJlCIKkYxg2y2azuQaD4RHUduEKqTr7ZQpgW6UEDFy7CGKxOFhX8S6woU4CptMpn5zYN/LDne8OVlVt2/bihcOG43gCwzBQVFSEdHd39x84cKAuGAzOicViNkmSwr6+vqGxsbGn58+fPzg/P2/Ozs4uUqlUlwoKCngCgYA3PDxsVavVu2/cuHEXOqf5D+DNylIwcL0zBbC2fDewzTgJGKZTVEf3PJx89LAZUOlLrvX23lar1fvNZnNwbm5uLhAIzGRmZgrkcvlmm82G0mi0dJPJ9NxisaAymayip6fnpkwmqzOZTA673W4rLCzM1+v1FqlUuv7Bgwc/Qb9MTJLDPz9OJZAv5AP52zUgmcSB9pvbwOcPpCpYIUDGdVe/aggGg2n379+fOnToUHk4HP4LhmFmJBLxBwKBmMlk8pSUlBQ4nU43lUp9ZenSpekTExPTeXl5S+7du2cRi8WLORwOIzc3N9NqtWJbt25d4/V6Pf8A4sxmiG4jkaIAAAAASUVORK5CYII=\" }";

  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  @Test
  public void getPreferenzeEnte() {
    Preferenza preferenzeEnte = preferenzeEnteService.getPreferenzeEnte(1, "1.0");
    assertNotNull(preferenzeEnte);
  }

  @Test
  public void createPreferenzeEnte() {

    Preferenza preferenzeEnteDaCreate = new Preferenza();
    preferenzeEnteDaCreate.setValore(valore);
    preferenzeEnteDaCreate.setVersione("2.0");

    Preferenza preferenzeEnteCreate =
        preferenzeEnteService.createPreferenzeEnte(1, preferenzeEnteDaCreate);

    assertNotNull(preferenzeEnteCreate);
  }

  @Test
  public void createPreferenzeEnteConVersioneGiaEsistente() {

    try {
      Preferenza preferenzeEnteDaCreare = new Preferenza();
      preferenzeEnteDaCreare.setValore(valore);
      preferenzeEnteDaCreare.setVersione("1.0");

      preferenzeEnteService.createPreferenzeEnte(1, preferenzeEnteDaCreare);

      fail("preferenzeEnteCreate non deve esistere");
    } catch (ManagedException e) {
      assertTrue(e.getMessage().equals("Preferenze con versione '1.0' gia' esistenti"));
    }

  }

  @Test
  public void updatePreferenzeEnte() {
    Preferenza preferenze = new Preferenza();
    preferenze.setVersione("1.0");
    preferenze.setValore(valore);
    assertNotNull(preferenzeEnteService.updatePreferenzeEnte(1, preferenze));
  }
  
  @Test(expected = NotFoundException.class)
  public void getPreferenzeEnteNotFound() {
    preferenzeEnteService.getPreferenzeEnte(111, "valore");
  }
  
  @Test(expected = NotFoundException.class)
  public void updatePreferenzeEnteNotFound() {
    Preferenza body = new Preferenza();
    preferenzeEnteService.updatePreferenzeEnte(111, body);
  }
  
  @Test(expected = NotFoundException.class)
  public void updatePreferenzeEntePreferenzeNotFound() {
    Preferenza body = new Preferenza();
    body.setVersione("prova");
    preferenzeEnteService.updatePreferenzeEnte(1, body);
  }
  
  @Test(expected = NotFoundException.class)
  public void createPreferenzeEnteNotFound() {
    Preferenza body = new Preferenza();
    preferenzeEnteService.createPreferenzeEnte(111, body);
  }
}
