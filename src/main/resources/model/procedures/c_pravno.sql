USE [PI]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[c_PRAVNO_LICE]
   @PR_PIB char(10),
   @PR_NAZIV varchar(120),
   @PR_ADRESA varchar(120),
   @PR_EMAIL varchar(128),
   @PR_WEB varchar(128),
   @PR_TELEFON varchar(20),
   @PR_FAX varchar(20),
   @PRA_PR_PIB char(10),
   @JMBG numeric(13,0),
   @IME varchar(120),
   @PREZIME varchar(120),
   @PRAVNO_LICE_ bit
AS
INSERT INTO [PRAVNA_LICA] (
   PR_PIB,
   PR_NAZIV,
   PR_ADRESA,
   PR_EMAIL,
   PR_WEB,
   PR_TELEFON,
   PR_FAX,
   PR_BANKA)
 VALUES ( 
   @PR_PIB,
   @PR_NAZIV,
   @PR_ADRESA,
   @PR_EMAIL,
   @PR_WEB,
   @PR_TELEFON,
   @PR_FAX,
   0);
INSERT INTO [KLIJENT] (
   PR_PIB,
   PRA_PR_PIB,
   JMBG,
   IME,
   PREZIME,
   PRAVNO_LICE_)
 VALUES ( 
   @PR_PIB,
   @PRA_PR_PIB,
   @JMBG,
   @IME,
   @PREZIME,
   @PRAVNO_LICE_);
