USE [PoslovnaInformatika]
GO
/****** Object:  StoredProcedure [dbo].[u_ANALITIKA_IZVODA]    Script Date: 26.6.2015. 9:12:41 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
 
---------------------------------------------------------
--  UPDATE PROCEDURE FOR TABLE ANALITIKA_IZVODA
---------------------------------------------------------
create PROCEDURE [dbo].[updatePaymentStatus]
   @PR_PIB char(10),
   @BAR_RACUN varchar(18),
   @DSR_IZVOD numeric(3,0),
   @ASI_BROJSTAVKE numeric(8,0),
   @ASI_STATUS char(1)
AS
UPDATE [ANALITIKA_IZVODA]
SET
   ASI_STATUS = @ASI_STATUS
WHERE 
   PR_PIB =  @PR_PIB AND 
   BAR_RACUN =  @BAR_RACUN AND 
   DSR_IZVOD =  @DSR_IZVOD AND 
   ASI_BROJSTAVKE =  @ASI_BROJSTAVKE
