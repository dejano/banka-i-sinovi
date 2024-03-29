USE [PoslovnaInformatika]
GO
/****** Object:  StoredProcedure [dbo].[c_KLIJENT]    Script Date: 19.6.2015. 1:11:59 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
 
---------------------------------------------------------
--   CREATE PROCEDURE FOR TABLE KLIJENT
---------------------------------------------------------
ALTER PROCEDURE [dbo].[c_KLIJENT]
   @PR_PIB char(10),
   @PRA_PR_PIB char(10),
   @JMBG numeric(13,0),
   @IME varchar(120),
   @PREZIME varchar(120),
   @PRAVNO_LICE_ bit
AS
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
