USE [PI]
GO
/****** Object:  StoredProcedure [dbo].[c_PRAVNO_LICE]    Script Date: 21.6.2015. 14:23:30 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[u_PRAVNO_LICE]
   @old_JMBG numeric(13,0),
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
UPDATE [KLIJENT]
SET
   PR_PIB = @PR_PIB,
   PRA_PR_PIB = @PRA_PR_PIB,
   JMBG = @JMBG,
   IME = @IME,
   PREZIME = @PREZIME,
   PRAVNO_LICE_ = @PRAVNO_LICE_
WHERE 
   JMBG =  @old_JMBG

UPDATE [PRAVNA_LICA]
SET
   PR_PIB = @PR_PIB,
   PR_NAZIV = @PR_NAZIV,
   PR_ADRESA = @PR_ADRESA,
   PR_EMAIL = @PR_EMAIL,
   PR_WEB = @PR_WEB,
   PR_TELEFON = @PR_TELEFON,
   PR_FAX = @PR_FAX,
   PR_BANKA = 0
WHERE 
   PR_PIB =  @PR_PIB
