USE [PoslovnaInformatika]
GO
/****** Object:  StoredProcedure [dbo].[d_KLIJENT]    Script Date: 21.6.2015. 14:27:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
 
---------------------------------------------------------
--   DELETE PROCEDURE FOR TABLE KLIJENT
---------------------------------------------------------
ALTER PROCEDURE [dbo].[d_KLIJENT]
   @JMBG numeric(13,0)
AS

DELETE [KLIJENT] WHERE 
   JMBG =  @JMBG

DECLARE @PRAVNO_LICE_ID int

SELECT @PRAVNO_LICE_ID = k.PR_PIB 
FROM KLIJENT k
WHERE k.JMBG = @JMBG;

IF @PRAVNO_LICE_ID IS NOT NULL
BEGIN
	DELETE [PRAVNA_LICA] WHERE
	PR_PIB = @PRAVNO_LICE_ID
END