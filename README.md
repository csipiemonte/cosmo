# Prodotto
Cosmo - Common workspace

# Descrizione del prodotto

Cosmo è un applicativo il cui scopo è quello di fare da scrivania digitale, quindi digitalizzare azioni come gli iter amministrativi, la gestione documentale, la gestione delle pratiche amministrative, la comunicazione tra fruitori diversi.

Per garantire un livello sufficiente di separazione e implementare dei meccanismi di fault tolerance, Cosmo è diviso in più componenti, organizzati per funzionalità. Tutti i servizi sono orchestrati da due componenti installate separatamente, che sono [Cosmo](/cosmo) e [Cosmobe](/cosmobe), rispettivamente per i servizi per il frontend e per i fruitori esterni.

Cosmo prevede anche l'utilizzo di un motore di processi di business Flowable.

### Backend
Le componenti di backend sono:

 - [CosmoAuthorization](/cosmoauthorization), la cui funzionalità è quella della gestione delle autorizzazioni, degli utenti e tutte le proprietà legate agli utilizzatori dell'applicativo.
 - [CosmoEcm](/cosmoecm), la cui funzionalità è quella di gestire la parte documentale.
 - [CosmoNotifications](/cosmonotifications), si occupa della gestione di notifiche da e verso l'applicativo.
 - [CosmoPratiche](/cosmopratiche), la cui funzionalità è gestire le pratiche che vengono create su Cosmo e tutte le azioni che vengono svolte sulle pratiche stesse
 - [CosmoBusiness](/cosmobusiness), si occupa della gestione e comunicazione verso cosmocmmn.
 - [CosmoCmmn](/cosmocmmn), per la gestione di flowable e dei flussi di processo di business che vengono creati con flowable.
 - [CosmoSoap](/cosmosoap), per effettuare tutte le chiamate verso servizi SOAP.

Ogni servizio rappresenta un’unità di installazione che verrà dispiegata all’interno di uno o più middleware WildFly17. I servizi che necessitano di colloquiare con il database lo fanno attraverso jdbc (Spring Data JPA).
Ogni componente esporrà dei servizi REST che verranno fruiti dagli orchestratori.

### Frontend
Per la parte di frontend esiste un unico componente [CosmoWcl](/cosmowcl). Questa componente comunica unicamente con [Cosmo](/cosmo). CosmoWcl usa il software development kit pubblicato da AgID attraverso “Designers Italia” (Designers Italia 2020).

# Configurazioni iniziali
Aggiungere al classpath tutte le librerie che si trovano nel path /lib, /docs/lib_ext e docs/test.
Aggiungere al classpath delle configurazioni di Ant, le librerie ant-contrib-1.0b3.jar e ivy-2.5.0.jar. Si trovano sotto la cartella [lib-build](/cosmocommon/docs/lib_build).
Lanciare il file build.xml per buildare ciascuna componente.
Una volta che la build è terminata, nella cartella build del componente è presente un file .ear con il nome della componente, deployabile su un application server.

Per quanto riguarda invece il frontend, dopo aver installato npm e l'angular cli, da command line bisogna portarsi nella cartella di CosmoWcl e da li installare le dipendenze e le devDependencies e avviare il server che punta a localhost:4200.

```sh
npm install 
ng serve
```

# Prerequisiti di sistema
Java version: 11

Ant version: 1.7

Postgresql version: 9.7

Angular version: 10.2

Wildfly version: 17.0

Npm

# Versioning
2.0.0

# Authors
Per le persone persone che hanno partecipato alla realizzazione del software si fa riferimento a quanto riportato nel file [AUTHORS.txt](/AUTHORS.txt).

# Copyrights
© Copyright CSI-Piemonte – 2023

# License
SPDX-License-Identifier: [GPL-3.0-or-later](https://spdx.org/licenses/GPL-3.0-or-later.html)

Vedere il file [license.md](/license.md) per i dettagli.

# Community site
Per l'uso di Flowable far riferimento al [sito ufficiale](https://www.flowable.com/open-source/docs/)

Per l'uso di Form.io per la creazione di form customizzabili, far riferimento al [sito ufficiale](https://form.io/open-source)