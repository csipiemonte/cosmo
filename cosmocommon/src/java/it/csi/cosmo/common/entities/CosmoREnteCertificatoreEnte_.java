package it.csi.cosmo.common.entities;

import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-12-11T10:21:01.797+0100")
@StaticMetamodel(CosmoREnteCertificatoreEnte.class)
public class CosmoREnteCertificatoreEnte_ extends CosmoREntity_ {
	public static volatile SingularAttribute<CosmoREnteCertificatoreEnte, CosmoREnteCertificatoreEntePK> id;
	public static volatile SingularAttribute<CosmoREnteCertificatoreEnte, Long> anno;
	public static volatile SingularAttribute<CosmoREnteCertificatoreEnte, Long> numeroMarcheTemporali;
	public static volatile SingularAttribute<CosmoREnteCertificatoreEnte, CosmoDEnteCertificatore> cosmoDEnteCertificatore;
	public static volatile SingularAttribute<CosmoREnteCertificatoreEnte, CosmoTEnte> cosmoTEnte;
}
