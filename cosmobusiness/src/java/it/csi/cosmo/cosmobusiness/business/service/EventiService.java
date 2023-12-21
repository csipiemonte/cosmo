/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service;

import it.csi.cosmo.cosmobusiness.dto.rest.Evento;
import it.csi.cosmo.cosmobusiness.dto.rest.PaginaEventi;

public interface EventiService {
  public PaginaEventi getEventi(String nome, String utente, String dtCreazionePrima, String dtCreazioneDopo, String dtScadenzaPrima, String dtScadenzaDopo, Boolean sospeso, String ente, Integer inizio, Integer dimensionePagina);

  public Evento getEventiId(String id);

  public Evento postEvento(Evento body);

  public Evento putEvento(String id, Evento body);

  public void deleteEvento(String id);

}
