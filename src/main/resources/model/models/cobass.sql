/*==============================================================*/
/* DBMS name:      Microsoft SQL Server 2012                    */
/* Created on:     25.6.2015. 20:00:19                          */
/*==============================================================*/


if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('ANALITIKA_IZVODA') and o.name = 'FK_ANALITIK_ANALITIKA_DNEVNO_S')
alter table ANALITIKA_IZVODA
   drop constraint FK_ANALITIK_ANALITIKA_DNEVNO_S
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('ANALITIKA_IZVODA') and o.name = 'FK_ANALITIK_MESTO_PRI_NASELJEN')
alter table ANALITIKA_IZVODA
   drop constraint FK_ANALITIK_MESTO_PRI_NASELJEN
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('ANALITIKA_IZVODA') and o.name = 'FK_ANALITIK_VALUTA_PL_VALUTE')
alter table ANALITIKA_IZVODA
   drop constraint FK_ANALITIK_VALUTA_PL_VALUTE
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('ANALITIKA_IZVODA') and o.name = 'FK_ANALITIK_VRSTA_PLA_VRSTE_PL')
alter table ANALITIKA_IZVODA
   drop constraint FK_ANALITIK_VRSTA_PLA_VRSTE_PL
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('ANALITIKA_PRESEKA') and o.name = 'FK_ANALITIK_STAVKE_U__ANALITIK')
alter table ANALITIKA_PRESEKA
   drop constraint FK_ANALITIK_STAVKE_U__ANALITIK
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('ANALITIKA_PRESEKA') and o.name = 'FK_ANALITIK_STA_CINI__PRENOS_I')
alter table ANALITIKA_PRESEKA
   drop constraint FK_ANALITIK_STA_CINI__PRENOS_I
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('ANALITIKA_STAVKE') and o.name = 'FK_ANALITIK_STAVKA_MEDJUBAN')
alter table ANALITIKA_STAVKE
   drop constraint FK_ANALITIK_STAVKA_MEDJUBAN
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('ANALITIKA_STAVKE') and o.name = 'FK_ANALITIK_STAVKA_IZ_ANALITIK')
alter table ANALITIKA_STAVKE
   drop constraint FK_ANALITIK_STAVKA_IZ_ANALITIK
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('DNEVNO_STANJE_RACUNA') and o.name = 'FK_DNEVNO_S_DNEVNI_IZ_RACUNI_P')
alter table DNEVNO_STANJE_RACUNA
   drop constraint FK_DNEVNO_S_DNEVNI_IZ_RACUNI_P
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('KLIJENT') and o.name = 'FK_KLIJENT_PRAVNO_LI_PRAVNA_L')
alter table KLIJENT
   drop constraint FK_KLIJENT_PRAVNO_LI_PRAVNA_L
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('KLIJENT') and o.name = 'FK_KLIJENT_KLIJENT B_PRAVNA_L')
alter table KLIJENT
   drop constraint "FK_KLIJENT_KLIJENT B_PRAVNA_L"
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('KURSNA_LISTA') and o.name = 'FK_KURSNA_L_KURS_POSL_PRAVNA_L')
alter table KURSNA_LISTA
   drop constraint FK_KURSNA_L_KURS_POSL_PRAVNA_L
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('KURS_U_VALUTI') and o.name = 'FK_KURS_U_V_OSNOVNA_V_VALUTE')
alter table KURS_U_VALUTI
   drop constraint FK_KURS_U_V_OSNOVNA_V_VALUTE
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('KURS_U_VALUTI') and o.name = 'FK_KURS_U_V_PREMA_VAL_VALUTE')
alter table KURS_U_VALUTI
   drop constraint FK_KURS_U_V_PREMA_VAL_VALUTE
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('KURS_U_VALUTI') and o.name = 'FK_KURS_U_V_VALUTE_U__KURSNA_L')
alter table KURS_U_VALUTI
   drop constraint FK_KURS_U_V_VALUTE_U__KURSNA_L
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('MEDJUBANKARSKI_NALOG') and o.name = 'FK_MEDJUBAN_BANKA_DUZ_BANKA_PO')
alter table MEDJUBANKARSKI_NALOG
   drop constraint FK_MEDJUBAN_BANKA_DUZ_BANKA_PO
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('MEDJUBANKARSKI_NALOG') and o.name = 'FK_MEDJUBAN_BANKA_POV_BANKA_PO')
alter table MEDJUBANKARSKI_NALOG
   drop constraint FK_MEDJUBAN_BANKA_POV_BANKA_PO
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('NASELJENO_MESTO') and o.name = 'FK_NASELJEN_MESTA_U_D_DRZAVA')
alter table NASELJENO_MESTO
   drop constraint FK_NASELJEN_MESTA_U_D_DRZAVA
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('PRENOS_IZVODA___PRESEK') and o.name = 'FK_PRENOS_I_NALOZI_U__DNEVNO_S')
alter table PRENOS_IZVODA___PRESEK
   drop constraint FK_PRENOS_I_NALOZI_U__DNEVNO_S
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('RACUNI_PRAVNIH_LICA') and o.name = 'FK_RACUNI_P_POSLOVNA__PRAVNA_L')
alter table RACUNI_PRAVNIH_LICA
   drop constraint FK_RACUNI_P_POSLOVNA__PRAVNA_L
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('RACUNI_PRAVNIH_LICA') and o.name = 'FK_RACUNI_P_VALUTA_RA_VALUTE')
alter table RACUNI_PRAVNIH_LICA
   drop constraint FK_RACUNI_P_VALUTA_RA_VALUTE
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('RACUNI_PRAVNIH_LICA') and o.name = 'FK_RACUNI_P_VLASNIK_R_KLIJENT')
alter table RACUNI_PRAVNIH_LICA
   drop constraint FK_RACUNI_P_VLASNIK_R_KLIJENT
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('UKIDANJE') and o.name = 'FK_UKIDANJE_UKIDANJE__RACUNI_P')
alter table UKIDANJE
   drop constraint FK_UKIDANJE_UKIDANJE__RACUNI_P
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('VALUTE') and o.name = 'FK_VALUTE_DRZAVNA_V_DRZAVA')
alter table VALUTE
   drop constraint FK_VALUTE_DRZAVNA_V_DRZAVA
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('ANALITIKA_IZVODA')
            and   name  = 'VALUTA_PLA_ANJA_FK'
            and   indid > 0
            and   indid < 255)
   drop index ANALITIKA_IZVODA.VALUTA_PLA_ANJA_FK
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('ANALITIKA_IZVODA')
            and   name  = 'VRSTA_PLA_ANJA_FK'
            and   indid > 0
            and   indid < 255)
   drop index ANALITIKA_IZVODA.VRSTA_PLA_ANJA_FK
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('ANALITIKA_IZVODA')
            and   name  = 'MESTO_PRIJEMA_FK'
            and   indid > 0
            and   indid < 255)
   drop index ANALITIKA_IZVODA.MESTO_PRIJEMA_FK
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('ANALITIKA_IZVODA')
            and   name  = 'ANALITIKA_IZVODA_BANKE_FK'
            and   indid > 0
            and   indid < 255)
   drop index ANALITIKA_IZVODA.ANALITIKA_IZVODA_BANKE_FK
go

if exists (select 1
            from  sysobjects
           where  id = object_id('ANALITIKA_IZVODA')
            and   type = 'U')
   drop table ANALITIKA_IZVODA
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('ANALITIKA_PRESEKA')
            and   name  = 'STAVKE_U_IZVODU_FK'
            and   indid > 0
            and   indid < 255)
   drop index ANALITIKA_PRESEKA.STAVKE_U_IZVODU_FK
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('ANALITIKA_PRESEKA')
            and   name  = 'STA_CINI_PRESEK_FK'
            and   indid > 0
            and   indid < 255)
   drop index ANALITIKA_PRESEKA.STA_CINI_PRESEK_FK
go

if exists (select 1
            from  sysobjects
           where  id = object_id('ANALITIKA_PRESEKA')
            and   type = 'U')
   drop table ANALITIKA_PRESEKA
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('ANALITIKA_STAVKE')
            and   name  = 'STAVKA_IZ_IZVODA_FK'
            and   indid > 0
            and   indid < 255)
   drop index ANALITIKA_STAVKE.STAVKA_IZ_IZVODA_FK
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('ANALITIKA_STAVKE')
            and   name  = 'STAVKA_FK'
            and   indid > 0
            and   indid < 255)
   drop index ANALITIKA_STAVKE.STAVKA_FK
go

if exists (select 1
            from  sysobjects
           where  id = object_id('ANALITIKA_STAVKE')
            and   type = 'U')
   drop table ANALITIKA_STAVKE
go

if exists (select 1
            from  sysobjects
           where  id = object_id('BANKA_POSLOVNOG_PARTNERA')
            and   type = 'U')
   drop table BANKA_POSLOVNOG_PARTNERA
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('DNEVNO_STANJE_RACUNA')
            and   name  = 'DNEVNI_IZVODI_BANKE_FK'
            and   indid > 0
            and   indid < 255)
   drop index DNEVNO_STANJE_RACUNA.DNEVNI_IZVODI_BANKE_FK
go

if exists (select 1
            from  sysobjects
           where  id = object_id('DNEVNO_STANJE_RACUNA')
            and   type = 'U')
   drop table DNEVNO_STANJE_RACUNA
go

if exists (select 1
            from  sysobjects
           where  id = object_id('DRZAVA')
            and   type = 'U')
   drop table DRZAVA
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('KLIJENT')
            and   name  = 'klijent banke_FK'
            and   indid > 0
            and   indid < 255)
   drop index KLIJENT."klijent banke_FK"
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('KLIJENT')
            and   name  = 'PRAVNO_LICE_FK'
            and   indid > 0
            and   indid < 255)
   drop index KLIJENT.PRAVNO_LICE_FK
go

if exists (select 1
            from  sysobjects
           where  id = object_id('KLIJENT')
            and   type = 'U')
   drop table KLIJENT
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('KURSNA_LISTA')
            and   name  = 'KURS_POSLOVNE_BANKE_FK'
            and   indid > 0
            and   indid < 255)
   drop index KURSNA_LISTA.KURS_POSLOVNE_BANKE_FK
go

if exists (select 1
            from  sysobjects
           where  id = object_id('KURSNA_LISTA')
            and   type = 'U')
   drop table KURSNA_LISTA
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('KURS_U_VALUTI')
            and   name  = 'VALUTE_U_LISTI_FK'
            and   indid > 0
            and   indid < 255)
   drop index KURS_U_VALUTI.VALUTE_U_LISTI_FK
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('KURS_U_VALUTI')
            and   name  = 'PREMA_VALUTI_FK'
            and   indid > 0
            and   indid < 255)
   drop index KURS_U_VALUTI.PREMA_VALUTI_FK
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('KURS_U_VALUTI')
            and   name  = 'OSNOVNA_VALUTA_FK'
            and   indid > 0
            and   indid < 255)
   drop index KURS_U_VALUTI.OSNOVNA_VALUTA_FK
go

if exists (select 1
            from  sysobjects
           where  id = object_id('KURS_U_VALUTI')
            and   type = 'U')
   drop table KURS_U_VALUTI
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('MEDJUBANKARSKI_NALOG')
            and   name  = 'BANKA_POVERIOCA_FK'
            and   indid > 0
            and   indid < 255)
   drop index MEDJUBANKARSKI_NALOG.BANKA_POVERIOCA_FK
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('MEDJUBANKARSKI_NALOG')
            and   name  = 'BANKA_DUZNIKA_FK'
            and   indid > 0
            and   indid < 255)
   drop index MEDJUBANKARSKI_NALOG.BANKA_DUZNIKA_FK
go

if exists (select 1
            from  sysobjects
           where  id = object_id('MEDJUBANKARSKI_NALOG')
            and   type = 'U')
   drop table MEDJUBANKARSKI_NALOG
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('NASELJENO_MESTO')
            and   name  = 'MESTA_U_DRZAVI_FK'
            and   indid > 0
            and   indid < 255)
   drop index NASELJENO_MESTO.MESTA_U_DRZAVI_FK
go

if exists (select 1
            from  sysobjects
           where  id = object_id('NASELJENO_MESTO')
            and   type = 'U')
   drop table NASELJENO_MESTO
go

if exists (select 1
            from  sysobjects
           where  id = object_id('PRAVNA_LICA')
            and   type = 'U')
   drop table PRAVNA_LICA
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('PRENOS_IZVODA___PRESEK')
            and   name  = 'NALOZI_U_IZVODU_FK'
            and   indid > 0
            and   indid < 255)
   drop index PRENOS_IZVODA___PRESEK.NALOZI_U_IZVODU_FK
go

if exists (select 1
            from  sysobjects
           where  id = object_id('PRENOS_IZVODA___PRESEK')
            and   type = 'U')
   drop table PRENOS_IZVODA___PRESEK
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('RACUNI_PRAVNIH_LICA')
            and   name  = 'VLASNIK_RACUNA_FK'
            and   indid > 0
            and   indid < 255)
   drop index RACUNI_PRAVNIH_LICA.VLASNIK_RACUNA_FK
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('RACUNI_PRAVNIH_LICA')
            and   name  = 'VALUTA_RACUNA_FK'
            and   indid > 0
            and   indid < 255)
   drop index RACUNI_PRAVNIH_LICA.VALUTA_RACUNA_FK
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('RACUNI_PRAVNIH_LICA')
            and   name  = 'POSLOVNA_BANKA_FK'
            and   indid > 0
            and   indid < 255)
   drop index RACUNI_PRAVNIH_LICA.POSLOVNA_BANKA_FK
go

if exists (select 1
            from  sysobjects
           where  id = object_id('RACUNI_PRAVNIH_LICA')
            and   type = 'U')
   drop table RACUNI_PRAVNIH_LICA
go

if exists (select 1
            from  sysobjects
           where  id = object_id('UKIDANJE')
            and   type = 'U')
   drop table UKIDANJE
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('VALUTE')
            and   name  = 'DRZAVNA_VALUTA_FK'
            and   indid > 0
            and   indid < 255)
   drop index VALUTE.DRZAVNA_VALUTA_FK
go

if exists (select 1
            from  sysobjects
           where  id = object_id('VALUTE')
            and   type = 'U')
   drop table VALUTE
go

if exists (select 1
            from  sysobjects
           where  id = object_id('VRSTE_PLACANJA')
            and   type = 'U')
   drop table VRSTE_PLACANJA
go

/*==============================================================*/
/* Table: ANALITIKA_IZVODA                                      */
/*==============================================================*/
create table ANALITIKA_IZVODA (
   PR_PIB               char(10)             not null,
   BAR_RACUN            varchar(18)          not null,
   DSR_IZVOD            numeric(3)           not null,
   ASI_BROJSTAVKE       numeric(8)           identity,
   VA_IFRA              char(3)              null,
   NM_SIFRA             numeric(3)           null,
   VPL_OZNAKA           char(3)              null,
   ID_NALOGA_PL         varchar(50)          null,
      constraint UN_NALOG UNIQUE(ID_NALOGA_PL),
   ASI_DUZNIK           varchar(256)         not null,
   ASI_SVRHA            varchar(256)         not null,
   ASI_POVERILAC        varchar(256)         not null,
   ASI_DATPRI           datetime             not null,
   ASI_DATVAL           datetime             not null,
   ASI_RACDUZ           varchar(18)          not null,
   ASI_MODZAD           numeric(2)           null,
   ASI_PBZAD            varchar(20)          null,
   ASI_RACPOV           varchar(18)          not null,
   ASI_MODODOB          numeric(2)           null,
   ASI_PBODO            varchar(20)          null,
   ASI_HITNO            bit                  not null default 0
      constraint CKC_ASI_HITNO_ANALITIK check (ASI_HITNO between 0 and 1),
   ASI_IZNOS            decimal(15,2)        not null default 0,
   ASI_TIPGRESKE        numeric(1)           not null default 1
      constraint CKC_ASI_TIPGRESKE_ANALITIK check (ASI_TIPGRESKE in (1,2,3,8,9)),
   ASI_STATUS           char(1)              null
      constraint CKC_ASI_STATUS_ANALITIK check (ASI_STATUS is null or (ASI_STATUS in ('E','P','I'))),
   constraint PK_ANALITIKA_IZVODA primary key nonclustered (PR_PIB, BAR_RACUN, DSR_IZVOD, ASI_BROJSTAVKE)
)
go

/*==============================================================*/
/* Index: ANALITIKA_IZVODA_BANKE_FK                             */
/*==============================================================*/
create index ANALITIKA_IZVODA_BANKE_FK on ANALITIKA_IZVODA (
PR_PIB ASC,
BAR_RACUN ASC,
DSR_IZVOD ASC
)
go

/*==============================================================*/
/* Index: MESTO_PRIJEMA_FK                                      */
/*==============================================================*/
create index MESTO_PRIJEMA_FK on ANALITIKA_IZVODA (
NM_SIFRA ASC
)
go

/*==============================================================*/
/* Index: VRSTA_PLA_ANJA_FK                                     */
/*==============================================================*/
create index VRSTA_PLA_ANJA_FK on ANALITIKA_IZVODA (
VPL_OZNAKA ASC
)
go

/*==============================================================*/
/* Index: VALUTA_PLA_ANJA_FK                                    */
/*==============================================================*/
create index VALUTA_PLA_ANJA_FK on ANALITIKA_IZVODA (
VA_IFRA ASC
)
go

/*==============================================================*/
/* Table: ANALITIKA_PRESEKA                                     */
/*==============================================================*/
create table ANALITIKA_PRESEKA (
   PRE_PR_PIB           char(10)             not null,
   PRE_BAR_RACUN        varchar(18)          not null,
   DSR_IZVOD            numeric(3)           not null,
   BNP_DATUM            datetime             not null,
   BNP_PRESEK           numeric(2)           not null,
   APR_RBR              numeric(5)           not null,
   PR_PIB               char(10)             null,
   BAR_RACUN            varchar(18)          null,
   ANA_DSR_IZVOD        numeric(3)           null,
   ASI_BROJSTAVKE       numeric(8)           null,
   constraint PK_ANALITIKA_PRESEKA primary key nonclustered (PRE_PR_PIB, PRE_BAR_RACUN, DSR_IZVOD, BNP_DATUM, BNP_PRESEK, APR_RBR)
)
go

/*==============================================================*/
/* Index: STA_CINI_PRESEK_FK                                    */
/*==============================================================*/
create index STA_CINI_PRESEK_FK on ANALITIKA_PRESEKA (
PRE_PR_PIB ASC,
PRE_BAR_RACUN ASC,
DSR_IZVOD ASC,
BNP_DATUM ASC,
BNP_PRESEK ASC
)
go

/*==============================================================*/
/* Index: STAVKE_U_IZVODU_FK                                    */
/*==============================================================*/
create index STAVKE_U_IZVODU_FK on ANALITIKA_PRESEKA (
PR_PIB ASC,
BAR_RACUN ASC,
ANA_DSR_IZVOD ASC,
ASI_BROJSTAVKE ASC
)
go

/*==============================================================*/
/* Table: ANALITIKA_STAVKE                                      */
/*==============================================================*/
create table ANALITIKA_STAVKE (
   REDNI_BROJ_STAVKE    numeric(8)           not null,
   ID_NALOGA            varchar(50)          not null,
   PR_PIB               char(10)             null,
   BAR_RACUN            varchar(18)          null,
   DSR_IZVOD            numeric(3)           null,
   ASI_BROJSTAVKE       numeric(8)           null,
   constraint PK_ANALITIKA_STAVKE primary key nonclustered (REDNI_BROJ_STAVKE, ID_NALOGA)
)
go

/*==============================================================*/
/* Index: STAVKA_FK                                             */
/*==============================================================*/
create index STAVKA_FK on ANALITIKA_STAVKE (
ID_NALOGA ASC
)
go

/*==============================================================*/
/* Index: STAVKA_IZ_IZVODA_FK                                   */
/*==============================================================*/
create index STAVKA_IZ_IZVODA_FK on ANALITIKA_STAVKE (
PR_PIB ASC,
BAR_RACUN ASC,
DSR_IZVOD ASC,
ASI_BROJSTAVKE ASC
)
go

/*==============================================================*/
/* Table: BANKA_POSLOVNOG_PARTNERA                              */
/*==============================================================*/
create table BANKA_POSLOVNOG_PARTNERA (
   SIFRA_BANKE          numeric(3)           not null,
   SWIFT_KOD            char(8)              not null,
   OBRACUNSKI_RACUN     numeric(18)          not null,
   constraint PK_BANKA_POSLOVNOG_PARTNERA primary key nonclustered (SIFRA_BANKE)
)
go

/*==============================================================*/
/* Table: DNEVNO_STANJE_RACUNA                                  */
/*==============================================================*/
create table DNEVNO_STANJE_RACUNA (
   PR_PIB               char(10)             not null,
   BAR_RACUN            varchar(18)          not null,
   DSR_IZVOD            numeric(3)           not null,
   DSR_DATUM            datetime             not null,
   DSR_PRETHODNO        decimal(15,2)        not null default 0,
   DSR_UKORIST          decimal(15,2)        not null default 0,
   DSR_NATERET          decimal(15,2)        not null default 0,
   DSR_NOVOSTANJE       decimal(15,2)        not null default 0,
   constraint PK_DNEVNO_STANJE_RACUNA primary key nonclustered (PR_PIB, BAR_RACUN, DSR_IZVOD)
)
go

/*==============================================================*/
/* Index: DNEVNI_IZVODI_BANKE_FK                                */
/*==============================================================*/
create index DNEVNI_IZVODI_BANKE_FK on DNEVNO_STANJE_RACUNA (
PR_PIB ASC,
BAR_RACUN ASC
)
go

/*==============================================================*/
/* Table: DRZAVA                                                */
/*==============================================================*/
create table DRZAVA (
   DR_SIFRA             char(2)              not null default 'DR'
      constraint CKC_DR_SIFRA_DRZAVA check (DR_SIFRA in ('DR','RE','PR','PO')),
   DR_NAZIV             varchar(40)          not null,
   constraint PK_DRZAVA primary key nonclustered (DR_SIFRA)
)
go

if exists (select 1 from  sys.extended_properties
           where major_id = object_id('DRZAVA') and minor_id = 0)
begin 
   declare @CurrentUser sysname 
select @CurrentUser = user_name() 
execute sp_dropextendedproperty 'MS_Description',  
   'user', @CurrentUser, 'table', 'DRZAVA' 
 
end 


select @CurrentUser = user_name() 
execute sp_addextendedproperty 'MS_Description',  
   'Matièni katalog drava.', 
   'user', @CurrentUser, 'table', 'DRZAVA'
go

/*==============================================================*/
/* Table: KLIJENT                                               */
/*==============================================================*/
create table KLIJENT (
   JMBG                 numeric(13)          not null,
   PR_PIB               char(10)             null,
   PRA_PR_PIB           char(10)             not null,
   IME                  varchar(120)         null,
   PREZIME              varchar(120)         null,
   PRAVNO_LICE_         bit                  null,
   constraint PK_KLIJENT primary key nonclustered (JMBG)
)
go

/*==============================================================*/
/* Index: PRAVNO_LICE_FK                                        */
/*==============================================================*/
create index PRAVNO_LICE_FK on KLIJENT (
PR_PIB ASC
)
go

/*==============================================================*/
/* Index: "klijent banke_FK"                                    */
/*==============================================================*/
create index "klijent banke_FK" on KLIJENT (
PRA_PR_PIB ASC
)
go

/*==============================================================*/
/* Table: KURSNA_LISTA                                          */
/*==============================================================*/
create table KURSNA_LISTA (
   PR_PIB               char(10)             not null,
   KL_DATUM             datetime             not null,
   KL_BROJ              numeric(3)           not null,
   KL_DATPR             datetime             not null,
   constraint PK_KURSNA_LISTA primary key nonclustered (PR_PIB, KL_DATUM)
)
go

/*==============================================================*/
/* Index: KURS_POSLOVNE_BANKE_FK                                */
/*==============================================================*/
create index KURS_POSLOVNE_BANKE_FK on KURSNA_LISTA (
PR_PIB ASC
)
go

/*==============================================================*/
/* Table: KURS_U_VALUTI                                         */
/*==============================================================*/
create table KURS_U_VALUTI (
   PR_PIB               char(10)             not null,
   KL_DATUM             datetime             not null,
   KLS_RBR              numeric(2)           not null,
   VA_IFRA              char(3)              not null,
   VAL_VA_IFRA          char(3)              not null,
   KLS_KUPOVNI          decimal(9,4)         not null default 0,
   KLS_SREDNJI          decimal(9,4)         not null default 0,
   KLS_PRODAJNI         decimal(9,4)         not null default 0,
   constraint PK_KURS_U_VALUTI primary key nonclustered (PR_PIB, KL_DATUM, KLS_RBR)
)
go

/*==============================================================*/
/* Index: OSNOVNA_VALUTA_FK                                     */
/*==============================================================*/
create index OSNOVNA_VALUTA_FK on KURS_U_VALUTI (
VAL_VA_IFRA ASC
)
go

/*==============================================================*/
/* Index: PREMA_VALUTI_FK                                       */
/*==============================================================*/
create index PREMA_VALUTI_FK on KURS_U_VALUTI (
VA_IFRA ASC
)
go

/*==============================================================*/
/* Index: VALUTE_U_LISTI_FK                                     */
/*==============================================================*/
create index VALUTE_U_LISTI_FK on KURS_U_VALUTI (
PR_PIB ASC,
KL_DATUM ASC
)
go

/*==============================================================*/
/* Table: MEDJUBANKARSKI_NALOG                                  */
/*==============================================================*/
create table MEDJUBANKARSKI_NALOG (
   ID_NALOGA            varchar(50)          not null,
   SIFRA_BANKE          numeric(3)           null,
   BAN_SIFRA_BANKE      numeric(3)           null,
   UKUPAN_IZNOS         decimal(15,2)        not null,
   DATUM                datetime             not null,
   RTGS_                bit                  not null,
   STATUS               numeric(1)           not null,
   constraint PK_MEDJUBANKARSKI_NALOG primary key nonclustered (ID_NALOGA)
)
go

/*==============================================================*/
/* Index: BANKA_DUZNIKA_FK                                      */
/*==============================================================*/
create index BANKA_DUZNIKA_FK on MEDJUBANKARSKI_NALOG (
SIFRA_BANKE ASC
)
go

/*==============================================================*/
/* Index: BANKA_POVERIOCA_FK                                    */
/*==============================================================*/
create index BANKA_POVERIOCA_FK on MEDJUBANKARSKI_NALOG (
BAN_SIFRA_BANKE ASC
)
go

/*==============================================================*/
/* Table: NASELJENO_MESTO                                       */
/*==============================================================*/
create table NASELJENO_MESTO (
   NM_SIFRA             numeric(3)           not null,
   DR_SIFRA             char(2)              not null default 'DR'
      constraint CKC_DR_SIFRA_NASELJEN check (DR_SIFRA in ('DR','RE','PR','PO')),
   NM_NAZIV             varchar(60)          not null,
   NM_PTTOZNAKA         varchar(12)          not null,
   constraint PK_NASELJENO_MESTO primary key nonclustered (NM_SIFRA)
)
go

/*==============================================================*/
/* Index: MESTA_U_DRZAVI_FK                                     */
/*==============================================================*/
create index MESTA_U_DRZAVI_FK on NASELJENO_MESTO (
DR_SIFRA ASC
)
go

/*==============================================================*/
/* Table: PRAVNA_LICA                                           */
/*==============================================================*/
create table PRAVNA_LICA (
   PR_PIB               char(10)             not null,
   PR_NAZIV             varchar(120)         not null,
   PR_ADRESA            varchar(120)         not null,
   PR_EMAIL             varchar(128)         null,
   PR_WEB               varchar(128)         null,
   PR_TELEFON           varchar(20)          null,
   PR_FAX               varchar(20)          null,
   PR_BANKA             bit                  not null,
   constraint PK_PRAVNA_LICA primary key nonclustered (PR_PIB)
)
go

/*==============================================================*/
/* Table: PRENOS_IZVODA___PRESEK                                */
/*==============================================================*/
create table PRENOS_IZVODA___PRESEK (
   PR_PIB               char(10)             not null,
   BAR_RACUN            varchar(18)          not null,
   DSR_IZVOD            numeric(3)           not null,
   BNP_DATUM            datetime             not null,
   BNP_PRESEK           numeric(2)           not null,
   BNP_BRUKORIST        numeric(6)           not null default 0,
   BNP_U_KORIST         decimal(15,2)        not null default 0,
   BNP_BRNATERET        numeric(6)           not null default 0,
   BNP_UKTERET          decimal(15,2)        not null default 0,
   BNP_BRPOGK           numeric(6)           not null default 0,
   BNP_BRPOGT           numeric(6)           not null default 0,
   BNP_STATUS           char(1)              not null default 'F'
      constraint CKC_BNP_STATUS_PRENOS_I check (BNP_STATUS in ('F','P')),
   constraint PK_PRENOS_IZVODA___PRESEK primary key nonclustered (PR_PIB, BAR_RACUN, DSR_IZVOD, BNP_DATUM, BNP_PRESEK)
)
go

/*==============================================================*/
/* Index: NALOZI_U_IZVODU_FK                                    */
/*==============================================================*/
create index NALOZI_U_IZVODU_FK on PRENOS_IZVODA___PRESEK (
PR_PIB ASC,
BAR_RACUN ASC,
DSR_IZVOD ASC
)
go

/*==============================================================*/
/* Table: RACUNI_PRAVNIH_LICA                                   */
/*==============================================================*/
create table RACUNI_PRAVNIH_LICA (
   PR_PIB               char(10)             not null,
   BAR_RACUN            varchar(18)          not null,
   JMBG                 numeric(13)          not null,
   VA_IFRA              char(3)              not null,
   BAR_DATOTV           datetime             not null,
   BAR_VAZI             bit                  not null default 1
      constraint CKC_BAR_VAZI_RACUNI_P check (BAR_VAZI between 0 and 1),
   constraint PK_RACUNI_PRAVNIH_LICA primary key nonclustered (PR_PIB, BAR_RACUN)
)
go

/*==============================================================*/
/* Index: POSLOVNA_BANKA_FK                                     */
/*==============================================================*/
create index POSLOVNA_BANKA_FK on RACUNI_PRAVNIH_LICA (
PR_PIB ASC
)
go

/*==============================================================*/
/* Index: VALUTA_RACUNA_FK                                      */
/*==============================================================*/
create index VALUTA_RACUNA_FK on RACUNI_PRAVNIH_LICA (
VA_IFRA ASC
)
go

/*==============================================================*/
/* Index: VLASNIK_RACUNA_FK                                     */
/*==============================================================*/
create index VLASNIK_RACUNA_FK on RACUNI_PRAVNIH_LICA (
JMBG ASC
)
go

/*==============================================================*/
/* Table: UKIDANJE                                              */
/*==============================================================*/
create table UKIDANJE (
   PR_PIB               char(10)             not null,
   BAR_RACUN            varchar(18)          not null,
   UK_DATUKIDANJA       datetime             not null,
   UK_NARACUN           varchar(20)          not null,
   constraint PK_UKIDANJE primary key (PR_PIB, BAR_RACUN)
)
go

/*==============================================================*/
/* Table: VALUTE                                                */
/*==============================================================*/
create table VALUTE (
   VA_IFRA              char(3)              not null,
   DR_SIFRA             char(2)              not null default 'DR'
      constraint CKC_DR_SIFRA_VALUTE check (DR_SIFRA in ('DR','RE','PR','PO')),
   VA_NAZIV             varchar(30)          not null,
   VA_DOMICILNA         bit                  not null default 0
      constraint CKC_VA_DOMICILNA_VALUTE check (VA_DOMICILNA between 0 and 1),
   constraint PK_VALUTE primary key nonclustered (VA_IFRA)
)
go

/*==============================================================*/
/* Index: DRZAVNA_VALUTA_FK                                     */
/*==============================================================*/
create index DRZAVNA_VALUTA_FK on VALUTE (
DR_SIFRA ASC
)
go

/*==============================================================*/
/* Table: VRSTE_PLACANJA                                        */
/*==============================================================*/
create table VRSTE_PLACANJA (
   VPL_OZNAKA           char(3)              not null,
   VPL_NAZIV            varchar(120)         not null,
   constraint PK_VRSTE_PLACANJA primary key nonclustered (VPL_OZNAKA)
)
go

alter table ANALITIKA_IZVODA
   add constraint FK_ANALITIK_ANALITIKA_DNEVNO_S foreign key (PR_PIB, BAR_RACUN, DSR_IZVOD)
      references DNEVNO_STANJE_RACUNA (PR_PIB, BAR_RACUN, DSR_IZVOD)
go

alter table ANALITIKA_IZVODA
   add constraint FK_ANALITIK_MESTO_PRI_NASELJEN foreign key (NM_SIFRA)
      references NASELJENO_MESTO (NM_SIFRA)
go

alter table ANALITIKA_IZVODA
   add constraint FK_ANALITIK_VALUTA_PL_VALUTE foreign key (VA_IFRA)
      references VALUTE (VA_IFRA)
go

alter table ANALITIKA_IZVODA
   add constraint FK_ANALITIK_VRSTA_PLA_VRSTE_PL foreign key (VPL_OZNAKA)
      references VRSTE_PLACANJA (VPL_OZNAKA)
go

alter table ANALITIKA_PRESEKA
   add constraint FK_ANALITIK_STAVKE_U__ANALITIK foreign key (PR_PIB, BAR_RACUN, ANA_DSR_IZVOD, ASI_BROJSTAVKE)
      references ANALITIKA_IZVODA (PR_PIB, BAR_RACUN, DSR_IZVOD, ASI_BROJSTAVKE)
go

alter table ANALITIKA_PRESEKA
   add constraint FK_ANALITIK_STA_CINI__PRENOS_I foreign key (PRE_PR_PIB, PRE_BAR_RACUN, DSR_IZVOD, BNP_DATUM, BNP_PRESEK)
      references PRENOS_IZVODA___PRESEK (PR_PIB, BAR_RACUN, DSR_IZVOD, BNP_DATUM, BNP_PRESEK)
go

alter table ANALITIKA_STAVKE
   add constraint FK_ANALITIK_STAVKA_MEDJUBAN foreign key (ID_NALOGA)
      references MEDJUBANKARSKI_NALOG (ID_NALOGA)
go

alter table ANALITIKA_STAVKE
   add constraint FK_ANALITIK_STAVKA_IZ_ANALITIK foreign key (PR_PIB, BAR_RACUN, DSR_IZVOD, ASI_BROJSTAVKE)
      references ANALITIKA_IZVODA (PR_PIB, BAR_RACUN, DSR_IZVOD, ASI_BROJSTAVKE)
go

alter table DNEVNO_STANJE_RACUNA
   add constraint FK_DNEVNO_S_DNEVNI_IZ_RACUNI_P foreign key (PR_PIB, BAR_RACUN)
      references RACUNI_PRAVNIH_LICA (PR_PIB, BAR_RACUN)
go

alter table KLIJENT
   add constraint FK_KLIJENT_PRAVNO_LI_PRAVNA_L foreign key (PR_PIB)
      references PRAVNA_LICA (PR_PIB)
go

alter table KLIJENT
   add constraint "FK_KLIJENT_KLIJENT B_PRAVNA_L" foreign key (PRA_PR_PIB)
      references PRAVNA_LICA (PR_PIB)
go

alter table KURSNA_LISTA
   add constraint FK_KURSNA_L_KURS_POSL_PRAVNA_L foreign key (PR_PIB)
      references PRAVNA_LICA (PR_PIB)
go

alter table KURS_U_VALUTI
   add constraint FK_KURS_U_V_OSNOVNA_V_VALUTE foreign key (VAL_VA_IFRA)
      references VALUTE (VA_IFRA)
go

alter table KURS_U_VALUTI
   add constraint FK_KURS_U_V_PREMA_VAL_VALUTE foreign key (VA_IFRA)
      references VALUTE (VA_IFRA)
go

alter table KURS_U_VALUTI
   add constraint FK_KURS_U_V_VALUTE_U__KURSNA_L foreign key (PR_PIB, KL_DATUM)
      references KURSNA_LISTA (PR_PIB, KL_DATUM)
go

alter table MEDJUBANKARSKI_NALOG
   add constraint FK_MEDJUBAN_BANKA_DUZ_BANKA_PO foreign key (SIFRA_BANKE)
      references BANKA_POSLOVNOG_PARTNERA (SIFRA_BANKE)
go

alter table MEDJUBANKARSKI_NALOG
   add constraint FK_MEDJUBAN_BANKA_POV_BANKA_PO foreign key (BAN_SIFRA_BANKE)
      references BANKA_POSLOVNOG_PARTNERA (SIFRA_BANKE)
go

alter table NASELJENO_MESTO
   add constraint FK_NASELJEN_MESTA_U_D_DRZAVA foreign key (DR_SIFRA)
      references DRZAVA (DR_SIFRA)
go

alter table PRENOS_IZVODA___PRESEK
   add constraint FK_PRENOS_I_NALOZI_U__DNEVNO_S foreign key (PR_PIB, BAR_RACUN, DSR_IZVOD)
      references DNEVNO_STANJE_RACUNA (PR_PIB, BAR_RACUN, DSR_IZVOD)
go

alter table RACUNI_PRAVNIH_LICA
   add constraint FK_RACUNI_P_POSLOVNA__PRAVNA_L foreign key (PR_PIB)
      references PRAVNA_LICA (PR_PIB)
go

alter table RACUNI_PRAVNIH_LICA
   add constraint FK_RACUNI_P_VALUTA_RA_VALUTE foreign key (VA_IFRA)
      references VALUTE (VA_IFRA)
go

alter table RACUNI_PRAVNIH_LICA
   add constraint FK_RACUNI_P_VLASNIK_R_KLIJENT foreign key (JMBG)
      references KLIJENT (JMBG)
go

alter table UKIDANJE
   add constraint FK_UKIDANJE_UKIDANJE__RACUNI_P foreign key (PR_PIB, BAR_RACUN)
      references RACUNI_PRAVNIH_LICA (PR_PIB, BAR_RACUN)
go

alter table VALUTE
   add constraint FK_VALUTE_DRZAVNA_V_DRZAVA foreign key (DR_SIFRA)
      references DRZAVA (DR_SIFRA)
go

