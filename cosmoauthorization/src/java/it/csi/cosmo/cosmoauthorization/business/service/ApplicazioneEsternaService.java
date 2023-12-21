/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service;

import java.util.List;
import it.csi.cosmo.cosmoauthorization.dto.rest.ApplicazioneEsterna;
import it.csi.cosmo.cosmoauthorization.dto.rest.ApplicazioneEsternaConValidita;

/**
 *
 */

public interface ApplicazioneEsternaService {

  /**
   * Metodo che elimina logicamente l'applicazione di cui si e' passato l'id, impostando la data di
   * cancellazione e l'utente di cancellazione (quello in sessione).
   *
   * Porre la fine della validita' alla relazione tra l'applicazione e l'ente.
   *
   * Se l'applicazione ha delle funzionalita' valide (quindi con data cancellazione ancora non
   * valorizzata, impostare la data di cancellazione e l'utente di cancellazione. valorizzata),
   * impostare la data di cancellazione e l'utente di cancellazione.
   *
   * Se le funzionalita' dell'applicazione sono associate a degli utenti, impostare la data di fine
   * validita' per tutte le associazioni. validita' per tutte le associazioni, se queste sono ancora
   * valide.
   *
   * @param id e' l'id dell'applicazione che si vuole eliminare
   */
  void eliminaApplicazione(String id);

  /**
   * Metodo che pone la fine della validita' alla relazione tra l'applicazione e l'ente.
   *
   * Se l'applicazione ha delle funzionalita' valide (quindi con data cancellazione ancora non
   * valorizzata, impostare la data di cancellazione e l'utente di cancellazione. valorizzata),
   * impostare la data di cancellazione e l'utente di cancellazione.
   *
   * Se le funzionalita' dell'applicazione sono associate a degli utenti, impostare la data di fine
   * validita' per tutte le associazioni. validita' per tutte le associazioni, se queste sono ancora
   * valide.
   *
   * @param id e' l'id dell'applicazione che si vuole eliminare
   */
  void eliminaApplicazioneAssociata(String id);

  /**
   * Restituisce tutte le applicazioni non cancellate
   *
   * @return una lista con tutte le applicazioni non cancellate
   */
  List<ApplicazioneEsternaConValidita> getTutteApplicazioni();

  /**
   * Metodo che restituisce tutte le applicazioni associate all'ente in sessione e in base al
   * paramentro configurata anche se associata o meno all'utente
   *
   * @param configurata, boolean che indica se restituire tutte le applicazioni associate all'ente
   *        quando e' valorizzata a false o tutte le applicazioni associate all'ente e anche
   *        all'utente quando e' valorizzata a true
   * @return una lista con tutte le applicazioni dell'ente in sessione
   */
  List<ApplicazioneEsterna> getApplicazioni(Boolean configurata);

  /**
   * Restituisce tutte le applicazioni associate o meno ad un ente
   *
   * @param enteAssociato indica se restituire tutte le applicazioni associate all'ente (true) o non
   *        associate all'ente (false)
   * @return una lista di applicazioni
   */
  List<ApplicazioneEsternaConValidita> getApplicazioniAssociateEnte(Boolean enteAssociato);

  /**
   * Metodo che restituisce l'applicazione di cui si e' passato l'id
   *
   * @param id e' l'id dell'applicazione che si vuole
   * @return un dto con l'applicazione richiesta
   */
  ApplicazioneEsterna getApplicazione(String id);

  /**
   * Metodo che restituisce l'applicazione di cui si e' passato l'id associata all'ente
   *
   * @param id e' l'id dell'applicazione che si vuole
   * @return un dto con l'applicazione richiesta
   */
  ApplicazioneEsternaConValidita getApplicazioneAssociataEnte(String id);

  /**
   * Salva l'applicazione che viene passata come input.
   *
   * @param body contiene le informazioni per creare un'applicazione
   * @return l'applicazione salvata
   */
  ApplicazioneEsterna salvaApplicazione(ApplicazioneEsterna body);

  /**
   * Metodo per aggiornare l'applicazione
   *
   * @param id e' l'id dell'applicazione da aggiornare
   * @param body contiene le informazioni da aggiornare nell'applicazione
   * @return
   */
  ApplicazioneEsterna aggiornaApplicazione(String id, ApplicazioneEsterna body);

  /**
   * Metodo per gestire le relazioni tra applicazione (quindi funzionalita' principale) e utente in
   * sessione
   *
   * Per ogni funzionalita' principale dell'applicazione:
   *
   * - Se la relazione non e' esistente, vuol dire che si vuole creare questa relazione e quindi si
   * crea una nuova relazione con la data di inizio validita' valorizzata con la data
   * dell'inserimento.
   *
   * - Se la relazione e' gia' esistente ed ha una data di fine validita' non valorizzata, significa
   * che si vuole rimuovere tale relazione e quindi si valorizza la data di fine validita'.
   *
   * - Sel la relazione e' gia' esistente e ha una data di fine validita' valorizzita, significa che
   * si vuole ricreare la relazione e quindi si imposta ad una nuova data la data di inizio
   * validita' e si imposta a null la data di fine validita'
   *
   * @param applicazioni e' la lista delle applicazioni' da associare
   * @return le applicazioni associate aggiornate
   */
  List<ApplicazioneEsterna> associaDissociaAppUtente(List<ApplicazioneEsterna> applicazioni);

  /**
   * Metodo che crea l'associazione app ed ente in sessione, se questa non e' ancora presente.
   *
   * Inoltre, alla prima creazione, crea anche la funzionalita' principale dell'app per l'ente in
   * sessione.
   *
   * Se invece l'associazione e' gia' esistente, cambia solo i parametri passati in input
   *
   * @param ha le informazioni per creare e aggiornare l'associazione ente applicazione
   * @return l'applicazione aggiornata
   */
  ApplicazioneEsternaConValidita associaModificaAssociazioneAppEnte(
      ApplicazioneEsternaConValidita applicazione);

}
