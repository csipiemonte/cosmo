/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.client;

import it.csi.cosmo.cosmobusiness.dto.rest.*;

import it.csi.cosmo.cosmobusiness.dto.rest.RegaxAllegati;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxAllegatiListResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxAllegatiResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxAllegatiass;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxAllegatiassListResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxAllegatiassResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxAssociazione;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxAssociazioneListResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxAssociazioneResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxComunicazioni;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxComunicazioniListResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxComunicazioniResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxContestodelibera;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxContestodeliberaListResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxContestodeliberaResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxDeliberaass;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxDeliberaassListResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxDeliberaassResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxDelibere;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxDelibereListResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxDelibereResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxEmptyResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxLivelloassociazione;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxLivelloassociazioneListResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxLivelloassociazioneResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxNaturagiuridica;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxNaturagiuridicaListResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxNaturagiuridicaResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxPersone;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxPersoneListResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxPersoneResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxPersoneass;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxPersoneassListResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxPersoneassResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxProtocollo;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxProtocolloListResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxProtocolloResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxRelazioneass;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxRelazioneassListResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxRelazioneassResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxRelazioni;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxRelazioniListResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxRelazioniResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxSedelegaleassociazione;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxSedelegaleassociazioneListResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxSedelegaleassociazioneResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxSedeoperativa;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxSedeoperativaListResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxSedeoperativaResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxSedime;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxSedimeListResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxSedimeResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxSettint;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxSettintListResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxSettintResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxSettoreinterventoass;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxSettoreinterventoassListResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxSettoreinterventoassResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxSezionelocaleassociazione;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxSezionelocaleassociazioneListResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxSezionelocaleassociazioneResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxStatoassociazione;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxStatoassociazioneListResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxStatoassociazioneResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxTiporuolo;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxTiporuoloListResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxTiporuoloResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxUffici;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxUfficiListResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxUfficiResponse;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/regax")  
public interface RegaxApi  {
   
    @DELETE @Path("/api/delete/Allegati/{key}")  @Produces({ "application/json" })
    public RegaxEmptyResponse regaxAllegatiDelete( @PathParam("key") String key);

    @GET @Path("/api/view/Allegati/{key}")  @Produces({ "application/json" })
    public RegaxAllegatiResponse regaxAllegatiGet( @PathParam("key") String key);

    @GET @Path("/api/list/Allegati")  @Produces({ "application/json" })
    public RegaxAllegatiListResponse regaxAllegatiList();

    @POST @Path("/api/add/Allegati") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxAllegatiResponse regaxAllegatiPost( @Valid RegaxAllegati body);

    @PUT @Path("/api/edit/Allegati/{key}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxAllegatiResponse regaxAllegatiPut( @PathParam("key") String key,  @Valid RegaxAllegati body);

    @DELETE @Path("/api/delete/Allegatiass/{key}")  @Produces({ "application/json" })
    public RegaxEmptyResponse regaxAllegatiassDelete( @PathParam("key") String key);

    @GET @Path("/api/view/Allegatiass/{key}")  @Produces({ "application/json" })
    public RegaxAllegatiassResponse regaxAllegatiassGet( @PathParam("key") String key);

    @GET @Path("/api/list/Allegatiass")  @Produces({ "application/json" })
    public RegaxAllegatiassListResponse regaxAllegatiassList();

    @POST @Path("/api/add/Allegatiass") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxAllegatiassResponse regaxAllegatiassPost( @Valid RegaxAllegatiass body);

    @PUT @Path("/api/edit/Allegatiass/{key}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxAllegatiassResponse regaxAllegatiassPut( @PathParam("key") String key,  @Valid RegaxAllegatiass body);

    @DELETE @Path("/api/delete/Associazione/{key}")  @Produces({ "application/json" })
    public RegaxEmptyResponse regaxAssociazioneDelete( @PathParam("key") String key);

    @GET @Path("/api/view/Associazione/{key}")  @Produces({ "application/json" })
    public RegaxAssociazioneResponse regaxAssociazioneGet( @PathParam("key") String key);

    @GET @Path("/api/list/Associazione")  @Produces({ "application/json" })
    public RegaxAssociazioneListResponse regaxAssociazioneList();

    @POST @Path("/api/add/Associazione") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxAssociazioneResponse regaxAssociazionePost( @Valid RegaxAssociazione body);

    @PUT @Path("/api/edit/Associazione/{key}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxAssociazioneResponse regaxAssociazionePut( @PathParam("key") String key,  @Valid RegaxAssociazione body);

    @DELETE @Path("/api/delete/Comunicazioni/{key}")  @Produces({ "application/json" })
    public RegaxEmptyResponse regaxComunicazioniDelete( @PathParam("key") String key);

    @GET @Path("/api/view/Comunicazioni/{key}")  @Produces({ "application/json" })
    public RegaxComunicazioniResponse regaxComunicazioniGet( @PathParam("key") String key);

    @GET @Path("/api/list/Comunicazioni")  @Produces({ "application/json" })
    public RegaxComunicazioniListResponse regaxComunicazioniList();

    @POST @Path("/api/add/Comunicazioni") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxComunicazioniResponse regaxComunicazioniPost( @Valid RegaxComunicazioni body);

    @PUT @Path("/api/edit/Comunicazioni/{key}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxComunicazioniResponse regaxComunicazioniPut( @PathParam("key") String key,  @Valid RegaxComunicazioni body);

    @DELETE @Path("/api/delete/Contestodelibera/{key}")  @Produces({ "application/json" })
    public RegaxEmptyResponse regaxContestodeliberaDelete( @PathParam("key") String key);

    @GET @Path("/api/view/Contestodelibera/{key}")  @Produces({ "application/json" })
    public RegaxContestodeliberaResponse regaxContestodeliberaGet( @PathParam("key") String key);

    @GET @Path("/api/list/Contestodelibera")  @Produces({ "application/json" })
    public RegaxContestodeliberaListResponse regaxContestodeliberaList();

    @POST @Path("/api/add/Contestodelibera") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxContestodeliberaResponse regaxContestodeliberaPost( @Valid RegaxContestodelibera body);

    @PUT @Path("/api/edit/Contestodelibera/{key}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxContestodeliberaResponse regaxContestodeliberaPut( @PathParam("key") String key,  @Valid RegaxContestodelibera body);

    @DELETE @Path("/api/delete/Deliberaass/{key}")  @Produces({ "application/json" })
    public RegaxEmptyResponse regaxDeliberaassDelete( @PathParam("key") String key);

    @GET @Path("/api/view/Deliberaass/{key}")  @Produces({ "application/json" })
    public RegaxDeliberaassResponse regaxDeliberaassGet( @PathParam("key") String key);

    @GET @Path("/api/list/Deliberaass")  @Produces({ "application/json" })
    public RegaxDeliberaassListResponse regaxDeliberaassList();

    @POST @Path("/api/add/Deliberaass") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxDeliberaassResponse regaxDeliberaassPost( @Valid RegaxDeliberaass body);

    @PUT @Path("/api/edit/Deliberaass/{key}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxDeliberaassResponse regaxDeliberaassPut( @PathParam("key") String key,  @Valid RegaxDeliberaass body);

    @DELETE @Path("/api/delete/Delibere/{key}")  @Produces({ "application/json" })
    public RegaxEmptyResponse regaxDelibereDelete( @PathParam("key") String key);

    @GET @Path("/api/view/Delibere/{key}")  @Produces({ "application/json" })
    public RegaxDelibereResponse regaxDelibereGet( @PathParam("key") String key);

    @GET @Path("/api/list/Delibere")  @Produces({ "application/json" })
    public RegaxDelibereListResponse regaxDelibereList();

    @POST @Path("/api/add/Delibere") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxDelibereResponse regaxDeliberePost( @Valid RegaxDelibere body);

    @PUT @Path("/api/edit/Delibere/{key}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxDelibereResponse regaxDeliberePut( @PathParam("key") String key,  @Valid RegaxDelibere body);

    @DELETE @Path("/api/delete/Livelloassociazione/{key}")  @Produces({ "application/json" })
    public RegaxEmptyResponse regaxLivelloassociazioneDelete( @PathParam("key") String key);

    @GET @Path("/api/view/Livelloassociazione/{key}")  @Produces({ "application/json" })
    public RegaxLivelloassociazioneResponse regaxLivelloassociazioneGet( @PathParam("key") String key);

    @GET @Path("/api/list/Livelloassociazione")  @Produces({ "application/json" })
    public RegaxLivelloassociazioneListResponse regaxLivelloassociazioneList();

    @POST @Path("/api/add/Livelloassociazione") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxLivelloassociazioneResponse regaxLivelloassociazionePost( @Valid RegaxLivelloassociazione body);

    @PUT @Path("/api/edit/Livelloassociazione/{key}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxLivelloassociazioneResponse regaxLivelloassociazionePut( @PathParam("key") String key,  @Valid RegaxLivelloassociazione body);

    @DELETE @Path("/api/delete/Naturagiuridica/{key}")  @Produces({ "application/json" })
    public RegaxEmptyResponse regaxNaturagiuridicaDelete( @PathParam("key") String key);

    @GET @Path("/api/view/Naturagiuridica/{key}")  @Produces({ "application/json" })
    public RegaxNaturagiuridicaResponse regaxNaturagiuridicaGet( @PathParam("key") String key);

    @GET @Path("/api/list/Naturagiuridica")  @Produces({ "application/json" })
    public RegaxNaturagiuridicaListResponse regaxNaturagiuridicaList();

    @POST @Path("/api/add/Naturagiuridica") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxNaturagiuridicaResponse regaxNaturagiuridicaPost( @Valid RegaxNaturagiuridica body);

    @PUT @Path("/api/edit/Naturagiuridica/{key}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxNaturagiuridicaResponse regaxNaturagiuridicaPut( @PathParam("key") String key,  @Valid RegaxNaturagiuridica body);

    @DELETE @Path("/api/delete/Persone/{key}")  @Produces({ "application/json" })
    public RegaxEmptyResponse regaxPersoneDelete( @PathParam("key") String key);

    @GET @Path("/api/view/Persone/{key}")  @Produces({ "application/json" })
    public RegaxPersoneResponse regaxPersoneGet( @PathParam("key") String key);

    @GET @Path("/api/list/Persone")  @Produces({ "application/json" })
    public RegaxPersoneListResponse regaxPersoneList();

    @POST @Path("/api/add/Persone") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxPersoneResponse regaxPersonePost( @Valid RegaxPersone body);

    @PUT @Path("/api/edit/Persone/{key}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxPersoneResponse regaxPersonePut( @PathParam("key") String key,  @Valid RegaxPersone body);

    @DELETE @Path("/api/delete/Personeass/{key}")  @Produces({ "application/json" })
    public RegaxEmptyResponse regaxPersoneassDelete( @PathParam("key") String key);

    @GET @Path("/api/view/Personeass/{key}")  @Produces({ "application/json" })
    public RegaxPersoneassResponse regaxPersoneassGet( @PathParam("key") String key);

    @GET @Path("/api/list/Personeass")  @Produces({ "application/json" })
    public RegaxPersoneassListResponse regaxPersoneassList();

    @POST @Path("/api/add/Personeass") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxPersoneassResponse regaxPersoneassPost( @Valid RegaxPersoneass body);

    @PUT @Path("/api/edit/Personeass/{key}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxPersoneassResponse regaxPersoneassPut( @PathParam("key") String key,  @Valid RegaxPersoneass body);

    @DELETE @Path("/api/delete/Protocollo/{key}")  @Produces({ "application/json" })
    public RegaxEmptyResponse regaxProtocolloDelete( @PathParam("key") String key);

    @GET @Path("/api/view/Protocollo/{key}")  @Produces({ "application/json" })
    public RegaxProtocolloResponse regaxProtocolloGet( @PathParam("key") String key);

    @GET @Path("/api/list/Protocollo")  @Produces({ "application/json" })
    public RegaxProtocolloListResponse regaxProtocolloList();

    @POST @Path("/api/add/Protocollo") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxProtocolloResponse regaxProtocolloPost( @Valid RegaxProtocollo body);

    @PUT @Path("/api/edit/Protocollo/{key}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxProtocolloResponse regaxProtocolloPut( @PathParam("key") String key,  @Valid RegaxProtocollo body);

    @DELETE @Path("/api/delete/Relazioneass/{key}")  @Produces({ "application/json" })
    public RegaxEmptyResponse regaxRelazioneassDelete( @PathParam("key") String key);

    @GET @Path("/api/view/Relazioneass/{key}")  @Produces({ "application/json" })
    public RegaxRelazioneassResponse regaxRelazioneassGet( @PathParam("key") String key);

    @GET @Path("/api/list/Relazioneass")  @Produces({ "application/json" })
    public RegaxRelazioneassListResponse regaxRelazioneassList();

    @POST @Path("/api/add/Relazioneass") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxRelazioneassResponse regaxRelazioneassPost( @Valid RegaxRelazioneass body);

    @PUT @Path("/api/edit/Relazioneass/{key}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxRelazioneassResponse regaxRelazioneassPut( @PathParam("key") String key,  @Valid RegaxRelazioneass body);

    @DELETE @Path("/api/delete/Relazioni/{key}")  @Produces({ "application/json" })
    public RegaxEmptyResponse regaxRelazioniDelete( @PathParam("key") String key);

    @GET @Path("/api/view/Relazioni/{key}")  @Produces({ "application/json" })
    public RegaxRelazioniResponse regaxRelazioniGet( @PathParam("key") String key);

    @GET @Path("/api/list/Relazioni")  @Produces({ "application/json" })
    public RegaxRelazioniListResponse regaxRelazioniList();

    @POST @Path("/api/add/Relazioni") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxRelazioniResponse regaxRelazioniPost( @Valid RegaxRelazioni body);

    @PUT @Path("/api/edit/Relazioni/{key}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxRelazioniResponse regaxRelazioniPut( @PathParam("key") String key,  @Valid RegaxRelazioni body);

    @DELETE @Path("/api/delete/Sedelegaleassociazione/{key}")  @Produces({ "application/json" })
    public RegaxEmptyResponse regaxSedelegaleassociazioneDelete( @PathParam("key") String key);

    @GET @Path("/api/view/Sedelegaleassociazione/{key}")  @Produces({ "application/json" })
    public RegaxSedelegaleassociazioneResponse regaxSedelegaleassociazioneGet( @PathParam("key") String key);

    @GET @Path("/api/list/Sedelegaleassociazione")  @Produces({ "application/json" })
    public RegaxSedelegaleassociazioneListResponse regaxSedelegaleassociazioneList();

    @POST @Path("/api/add/Sedelegaleassociazione") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxSedelegaleassociazioneResponse regaxSedelegaleassociazionePost( @Valid RegaxSedelegaleassociazione body);

    @PUT @Path("/api/edit/Sedelegaleassociazione/{key}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxSedelegaleassociazioneResponse regaxSedelegaleassociazionePut( @PathParam("key") String key,  @Valid RegaxSedelegaleassociazione body);

    @DELETE @Path("/api/delete/Sedeoperativa/{key}")  @Produces({ "application/json" })
    public RegaxEmptyResponse regaxSedeoperativaDelete( @PathParam("key") String key);

    @GET @Path("/api/view/Sedeoperativa/{key}")  @Produces({ "application/json" })
    public RegaxSedeoperativaResponse regaxSedeoperativaGet( @PathParam("key") String key);

    @GET @Path("/api/list/Sedeoperativa")  @Produces({ "application/json" })
    public RegaxSedeoperativaListResponse regaxSedeoperativaList();

    @POST @Path("/api/add/Sedeoperativa") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxSedeoperativaResponse regaxSedeoperativaPost( @Valid RegaxSedeoperativa body);

    @PUT @Path("/api/edit/Sedeoperativa/{key}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxSedeoperativaResponse regaxSedeoperativaPut( @PathParam("key") String key,  @Valid RegaxSedeoperativa body);

    @DELETE @Path("/api/delete/Sedime/{key}")  @Produces({ "application/json" })
    public RegaxEmptyResponse regaxSedimeDelete( @PathParam("key") String key);

    @GET @Path("/api/view/Sedime/{key}")  @Produces({ "application/json" })
    public RegaxSedimeResponse regaxSedimeGet( @PathParam("key") String key);

    @GET @Path("/api/list/Sedime")  @Produces({ "application/json" })
    public RegaxSedimeListResponse regaxSedimeList();

    @POST @Path("/api/add/Sedime") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxSedimeResponse regaxSedimePost( @Valid RegaxSedime body);

    @PUT @Path("/api/edit/Sedime/{key}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxSedimeResponse regaxSedimePut( @PathParam("key") String key,  @Valid RegaxSedime body);

    @DELETE @Path("/api/delete/Settint/{key}")  @Produces({ "application/json" })
    public RegaxEmptyResponse regaxSettintDelete( @PathParam("key") String key);

    @GET @Path("/api/view/Settint/{key}")  @Produces({ "application/json" })
    public RegaxSettintResponse regaxSettintGet( @PathParam("key") String key);

    @GET @Path("/api/list/Settint")  @Produces({ "application/json" })
    public RegaxSettintListResponse regaxSettintList();

    @POST @Path("/api/add/Settint") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxSettintResponse regaxSettintPost( @Valid RegaxSettint body);

    @PUT @Path("/api/edit/Settint/{key}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxSettintResponse regaxSettintPut( @PathParam("key") String key,  @Valid RegaxSettint body);

    @DELETE @Path("/api/delete/Settoreinterventoass/{key}")  @Produces({ "application/json" })
    public RegaxEmptyResponse regaxSettoreinterventoassDelete( @PathParam("key") String key);

    @GET @Path("/api/view/Settoreinterventoass/{key}")  @Produces({ "application/json" })
    public RegaxSettoreinterventoassResponse regaxSettoreinterventoassGet( @PathParam("key") String key);

    @GET @Path("/api/list/Settoreinterventoass")  @Produces({ "application/json" })
    public RegaxSettoreinterventoassListResponse regaxSettoreinterventoassList();

    @POST @Path("/api/add/Settoreinterventoass") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxSettoreinterventoassResponse regaxSettoreinterventoassPost( @Valid RegaxSettoreinterventoass body);

    @PUT @Path("/api/edit/Settoreinterventoass/{key}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxSettoreinterventoassResponse regaxSettoreinterventoassPut( @PathParam("key") String key,  @Valid RegaxSettoreinterventoass body);

    @DELETE @Path("/api/delete/Sezionelocaleassociazione/{key}")  @Produces({ "application/json" })
    public RegaxEmptyResponse regaxSezionelocaleassociazioneDelete( @PathParam("key") String key);

    @GET @Path("/api/view/Sezionelocaleassociazione/{key}")  @Produces({ "application/json" })
    public RegaxSezionelocaleassociazioneResponse regaxSezionelocaleassociazioneGet( @PathParam("key") String key);

    @GET @Path("/api/list/Sezionelocaleassociazione")  @Produces({ "application/json" })
    public RegaxSezionelocaleassociazioneListResponse regaxSezionelocaleassociazioneList();

    @POST @Path("/api/add/Sezionelocaleassociazione") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxSezionelocaleassociazioneResponse regaxSezionelocaleassociazionePost( @Valid RegaxSezionelocaleassociazione body);

    @PUT @Path("/api/edit/Sezionelocaleassociazione/{key}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxSezionelocaleassociazioneResponse regaxSezionelocaleassociazionePut( @PathParam("key") String key,  @Valid RegaxSezionelocaleassociazione body);

    @DELETE @Path("/api/delete/Statoassociazione/{key}")  @Produces({ "application/json" })
    public RegaxEmptyResponse regaxStatoassociazioneDelete( @PathParam("key") String key);

    @GET @Path("/api/view/Statoassociazione/{key}")  @Produces({ "application/json" })
    public RegaxStatoassociazioneResponse regaxStatoassociazioneGet( @PathParam("key") String key);

    @GET @Path("/api/list/Statoassociazione")  @Produces({ "application/json" })
    public RegaxStatoassociazioneListResponse regaxStatoassociazioneList();

    @POST @Path("/api/add/Statoassociazione") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxStatoassociazioneResponse regaxStatoassociazionePost( @Valid RegaxStatoassociazione body);

    @PUT @Path("/api/edit/Statoassociazione/{key}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxStatoassociazioneResponse regaxStatoassociazionePut( @PathParam("key") String key,  @Valid RegaxStatoassociazione body);

    @DELETE @Path("/api/delete/Tiporuolo/{key}")  @Produces({ "application/json" })
    public RegaxEmptyResponse regaxTiporuoloDelete( @PathParam("key") String key);

    @GET @Path("/api/view/Tiporuolo/{key}")  @Produces({ "application/json" })
    public RegaxTiporuoloResponse regaxTiporuoloGet( @PathParam("key") String key);

    @GET @Path("/api/list/Tiporuolo")  @Produces({ "application/json" })
    public RegaxTiporuoloListResponse regaxTiporuoloList();

    @POST @Path("/api/add/Tiporuolo") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxTiporuoloResponse regaxTiporuoloPost( @Valid RegaxTiporuolo body);

    @PUT @Path("/api/edit/Tiporuolo/{key}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxTiporuoloResponse regaxTiporuoloPut( @PathParam("key") String key,  @Valid RegaxTiporuolo body);

    @DELETE @Path("/api/delete/Uffici/{key}")  @Produces({ "application/json" })
    public RegaxEmptyResponse regaxUfficiDelete( @PathParam("key") String key);

    @GET @Path("/api/view/Uffici/{key}")  @Produces({ "application/json" })
    public RegaxUfficiResponse regaxUfficiGet( @PathParam("key") String key);

    @GET @Path("/api/list/Uffici")  @Produces({ "application/json" })
    public RegaxUfficiListResponse regaxUfficiList();

    @POST @Path("/api/add/Uffici") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxUfficiResponse regaxUfficiPost( @Valid RegaxUffici body);

    @PUT @Path("/api/edit/Uffici/{key}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RegaxUfficiResponse regaxUfficiPut( @PathParam("key") String key,  @Valid RegaxUffici body);

}
