SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
alter PROCEDURE getUnprocessedPayments
	@pib char(10)
AS
BEGIN
	SET NOCOUNT ON;

	return
	SELECT [PR_PIB]
      ,[BAR_RACUN]
      ,[DSR_IZVOD]
      ,[ASI_BROJSTAVKE]
      ,[VA_IFRA]
      ,[NM_SIFRA]
      ,[VPL_OZNAKA]
      ,[ID_NALOGA_PL]
      ,[ASI_DUZNIK]
      ,[ASI_SVRHA]
      ,[ASI_POVERILAC]
      ,[ASI_DATPRI]
      ,[ASI_DATVAL]
      ,[ASI_RACDUZ]
      ,[ASI_MODZAD]
      ,[ASI_PBZAD]
      ,[ASI_RACPOV]
      ,[ASI_MODODOB]
      ,[ASI_PBODO]
      ,[ASI_HITNO]
      ,[ASI_IZNOS]
      ,[ASI_TIPGRESKE]
      ,[ASI_STATUS]
	  from dbo.ANALITIKA_IZVODA
	  where PR_PIB = @pib
END
GO
