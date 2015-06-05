/*==============================================================*/
/* DBMS name:      Microsoft SQL Server 2008                    */
/* Created on:     6/3/2015 12:23:52                            */
/*==============================================================*/


if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('FILM') and o.name = 'FK_FILM_ASOCIJACI_ZANR')
alter table FILM
   drop constraint FK_FILM_ASOCIJACI_ZANR
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('FILM') and o.name = 'FK_FILM_FILMOVI_V_VIDEOTEK')
alter table FILM
   drop constraint FK_FILM_FILMOVI_V_VIDEOTEK
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('FILM') and o.name = 'FK_FILM_ZEMLJA_PO_ZEMLJA_P')
alter table FILM
   drop constraint FK_FILM_ZEMLJA_PO_ZEMLJA_P
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('KOPIJA') and o.name = 'FK_KOPIJA_KOPIJA_FI_FILM')
alter table KOPIJA
   drop constraint FK_KOPIJA_KOPIJA_FI_FILM
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('UCESNICI_FILMA') and o.name = 'FK_UCESNICI_UCESNICI__FILM')
alter table UCESNICI_FILMA
   drop constraint FK_UCESNICI_UCESNICI__FILM
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('UCESNICI_FILMA') and o.name = 'FK_UCESNICI_UCESNICI__UCESNIK_')
alter table UCESNICI_FILMA
   drop constraint FK_UCESNICI_UCESNICI__UCESNIK_
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('FILM')
            and   name  = 'FILMOVI_VIDEOTEKE_FK'
            and   indid > 0
            and   indid < 255)
   drop index FILM.FILMOVI_VIDEOTEKE_FK
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('FILM')
            and   name  = 'ASOCIJACIJA_FILM_ZANR_FK'
            and   indid > 0
            and   indid < 255)
   drop index FILM.ASOCIJACIJA_FILM_ZANR_FK
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('FILM')
            and   name  = 'ZEMLJA_POREKLA_FK'
            and   indid > 0
            and   indid < 255)
   drop index FILM.ZEMLJA_POREKLA_FK
go

if exists (select 1
            from  sysobjects
           where  id = object_id('FILM')
            and   type = 'U')
   drop table FILM
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('KOPIJA')
            and   name  = 'KOPIJA_FILMA_FK'
            and   indid > 0
            and   indid < 255)
   drop index KOPIJA.KOPIJA_FILMA_FK
go

if exists (select 1
            from  sysobjects
           where  id = object_id('KOPIJA')
            and   type = 'U')
   drop table KOPIJA
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('UCESNICI_FILMA')
            and   name  = 'UCESNICI_FILMA_FK2'
            and   indid > 0
            and   indid < 255)
   drop index UCESNICI_FILMA.UCESNICI_FILMA_FK2
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('UCESNICI_FILMA')
            and   name  = 'UCESNICI_FILMA_FK'
            and   indid > 0
            and   indid < 255)
   drop index UCESNICI_FILMA.UCESNICI_FILMA_FK
go

if exists (select 1
            from  sysobjects
           where  id = object_id('UCESNICI_FILMA')
            and   type = 'U')
   drop table UCESNICI_FILMA
go

if exists (select 1
            from  sysobjects
           where  id = object_id('UCESNIK_FILMA')
            and   type = 'U')
   drop table UCESNIK_FILMA
go

if exists (select 1
            from  sysobjects
           where  id = object_id('VIDEOTEKA')
            and   type = 'U')
   drop table VIDEOTEKA
go

if exists (select 1
            from  sysobjects
           where  id = object_id('ZANR')
            and   type = 'U')
   drop table ZANR
go

if exists (select 1
            from  sysobjects
           where  id = object_id('ZEMLJA_POREKLA')
            and   type = 'U')
   drop table ZEMLJA_POREKLA
go

/*==============================================================*/
/* Table: FILM                                                  */
/*==============================================================*/
create table FILM (
   SIFRA_VIDEOTEKE      numeric(1)           not null,
   SIFRA_FILMA          numeric(10)          not null,
   NAZIV_FILMA          varchar(200)         not null,
   ORIGINALNI_NAZIV     varchar(200)         null,
   SIFRA_ZEMLJE         numeric(3)           not null,
   SIFRA_ZANRA          numeric(2)           not null,
   KRATAK_OPIS          varchar(2000)        null,
   DUZINA_TRAJANJA      numeric(4)           not null,
   constraint PK_FILM primary key nonclustered (SIFRA_VIDEOTEKE, SIFRA_FILMA)
)
go

/*==============================================================*/
/* Index: ZEMLJA_POREKLA_FK                                     */
/*==============================================================*/
create index ZEMLJA_POREKLA_FK on FILM (
SIFRA_ZEMLJE ASC
)
go

/*==============================================================*/
/* Index: ASOCIJACIJA_FILM_ZANR_FK                              */
/*==============================================================*/
create index ASOCIJACIJA_FILM_ZANR_FK on FILM (
SIFRA_ZANRA ASC
)
go

/*==============================================================*/
/* Index: FILMOVI_VIDEOTEKE_FK                                  */
/*==============================================================*/
create index FILMOVI_VIDEOTEKE_FK on FILM (
SIFRA_VIDEOTEKE ASC
)
go

/*==============================================================*/
/* Table: KOPIJA                                                */
/*==============================================================*/
create table KOPIJA (
   BROJ_KOPIJE          numeric(10)          not null,
   SIFRA_VIDEOTEKE      numeric(1)           not null,
   SIFRA_FILMA          numeric(10)          not null,
   MEDIJUM              char(1)              not null,
   constraint PK_KOPIJA primary key nonclustered (SIFRA_VIDEOTEKE, BROJ_KOPIJE)
)
go

/*==============================================================*/
/* Index: KOPIJA_FILMA_FK                                       */
/*==============================================================*/
create index KOPIJA_FILMA_FK on KOPIJA (
SIFRA_VIDEOTEKE ASC,
SIFRA_FILMA ASC
)
go

/*==============================================================*/
/* Table: UCESNICI_FILMA                                        */
/*==============================================================*/
create table UCESNICI_FILMA (
   SIFRA_VIDEOTEKE      numeric(1)           not null,
   SIFRA_FILMA          numeric(10)          not null,
   SIFRA_UCESNIKA       numeric(4)           not null,
   constraint PK_UCESNICI_FILMA primary key nonclustered (SIFRA_VIDEOTEKE, SIFRA_FILMA, SIFRA_UCESNIKA)
)
go

/*==============================================================*/
/* Index: UCESNICI_FILMA_FK                                     */
/*==============================================================*/
create index UCESNICI_FILMA_FK on UCESNICI_FILMA (
SIFRA_VIDEOTEKE ASC,
SIFRA_FILMA ASC
)
go

/*==============================================================*/
/* Index: UCESNICI_FILMA_FK2                                    */
/*==============================================================*/
create index UCESNICI_FILMA_FK2 on UCESNICI_FILMA (
SIFRA_UCESNIKA ASC
)
go

/*==============================================================*/
/* Table: UCESNIK_FILMA                                         */
/*==============================================================*/
create table UCESNIK_FILMA (
   SIFRA_UCESNIKA       numeric(4)           not null,
   PREZIME              varchar(20)          not null,
   IME                  varchar(20)          not null,
   VRSTA_DELATNOSTI     char(1)              null
      constraint CKC_VRSTA_DELATNOSTI_UCESNIK_ check (VRSTA_DELATNOSTI is null or (VRSTA_DELATNOSTI in ('G','R','S','M'))),
   constraint PK_UCESNIK_FILMA primary key nonclustered (SIFRA_UCESNIKA)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'Vrsta delatnosti: glumac, reziser, scenarista, autor muzike',
   'user', @CurrentUser, 'table', 'UCESNIK_FILMA'
go

/*==============================================================*/
/* Table: VIDEOTEKA                                             */
/*==============================================================*/
create table VIDEOTEKA (
   SIFRA_VIDEOTEKE      numeric(1)           not null,
   NAZIV                varchar(30)          not null,
   ADRESA               varchar(80)          null,
   TELEFON              varchar(20)          null,
   ZIRO_RACUN           varchar(16)          not null,
   PORESK_IBROJ         varchar(13)          not null,
   BROJ_FILMOVA         numeric(10)          null default 0,
   BROJ_KOPIJA          numeric(10)          null default 0,
   constraint PK_VIDEOTEKA primary key nonclustered (SIFRA_VIDEOTEKE)
)
go

/*==============================================================*/
/* Table: ZANR                                                  */
/*==============================================================*/
create table ZANR (
   SIFRA_ZANRA          numeric(2)           not null,
   NAZIV                varchar(40)          not null,
   constraint PK_ZANR primary key nonclustered (SIFRA_ZANRA)
)
go

/*==============================================================*/
/* Table: ZEMLJA_POREKLA                                        */
/*==============================================================*/
create table ZEMLJA_POREKLA (
   SIFRA_ZEMLJE         numeric(3)           not null,
   NAZIV                varchar(30)          not null,
   constraint PK_ZEMLJA_POREKLA primary key nonclustered (SIFRA_ZEMLJE)
)
go

alter table FILM
   add constraint FK_FILM_ASOCIJACI_ZANR foreign key (SIFRA_ZANRA)
      references ZANR (SIFRA_ZANRA)
go

alter table FILM
   add constraint FK_FILM_FILMOVI_V_VIDEOTEK foreign key (SIFRA_VIDEOTEKE)
      references VIDEOTEKA (SIFRA_VIDEOTEKE)
go

alter table FILM
   add constraint FK_FILM_ZEMLJA_PO_ZEMLJA_P foreign key (SIFRA_ZEMLJE)
      references ZEMLJA_POREKLA (SIFRA_ZEMLJE)
go

alter table KOPIJA
   add constraint FK_KOPIJA_KOPIJA_FI_FILM foreign key (SIFRA_VIDEOTEKE, SIFRA_FILMA)
      references FILM (SIFRA_VIDEOTEKE, SIFRA_FILMA)
go

alter table UCESNICI_FILMA
   add constraint FK_UCESNICI_UCESNICI__FILM foreign key (SIFRA_VIDEOTEKE, SIFRA_FILMA)
      references FILM (SIFRA_VIDEOTEKE, SIFRA_FILMA)
go

alter table UCESNICI_FILMA
   add constraint FK_UCESNICI_UCESNICI__UCESNIK_ foreign key (SIFRA_UCESNIKA)
      references UCESNIK_FILMA (SIFRA_UCESNIKA)
go

