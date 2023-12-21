package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:02.859+0100")
@StaticMetamodel(CosmoTAttivita.class)
public class CosmoTAttivita_ extends CosmoTEntity_ {
	public static volatile SingularAttribute<CosmoTAttivita, Long> id;
	public static volatile SingularAttribute<CosmoTAttivita, String> descrizione;
	public static volatile SingularAttribute<CosmoTAttivita, String> linkAttivita;
	public static volatile SingularAttribute<CosmoTAttivita, String> linkAttivitaEsterna;
	public static volatile SingularAttribute<CosmoTAttivita, String> nome;
	public static volatile ListAttribute<CosmoTAttivita, CosmoRAttivitaAssegnazione> cosmoRAttivitaAssegnaziones;
	public static volatile ListAttribute<CosmoTAttivita, CosmoTCommento> commenti;
	public static volatile SingularAttribute<CosmoTAttivita, CosmoTPratica> cosmoTPratica;
	public static volatile SingularAttribute<CosmoTAttivita, CosmoTAttivita> parent;
	public static volatile ListAttribute<CosmoTAttivita, CosmoTAttivita> subtasks;
	public static volatile SingularAttribute<CosmoTAttivita, CosmoTFormLogico> formLogico;
	public static volatile SingularAttribute<CosmoTAttivita, String> formKey;
}
