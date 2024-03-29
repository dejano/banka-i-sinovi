USE [PoslovnaInformatika]
GO
/****** Object:  StoredProcedure [dbo].[getStatement]    Script Date: 6/21/2015 3:02:43 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
create PROCEDURE [dbo].[getStatement]
	-- Add the parameters for the stored procedure here
	@accountNumber nvarchar(50),
	@orderDate datetime,
	@presekNumber numeric(2, 0)
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	SELECT 	prenos.BAR_RACUN,
	prenos.BNP_DATUM,
	prenos.BNP_PRESEK,
	prenos.BNP_BRUKORIST,
	prenos.BNP_U_KORIST,
	prenos.BNP_BRNATERET,
	prenos.BNP_UKTERET,
	prenos.BNP_BRPOGK,
	prenos.BNP_BRPOGT,
	p.ASI_DUZNIK,
	p.ASI_SVRHA,
	p.ASI_POVERILAC,
	p.ASI_DATVAL,
	p.ASI_RACDUZ,
	p.ASI_MODZAD,
	p.ASI_PBZAD,
	p.ASI_RACPOV,
	p.ASI_MODODOB,
	p.ASI_PBODO,
	p.ASI_IZNOS
FROM PRENOS_IZVODA___PRESEK prenos 
INNER JOIN ANALITIKA_PRESEKA ap
ON
	  ap.BNP_PRESEK = prenos.BNP_PRESEK
AND   ap.BNP_DATUM = prenos.BNP_DATUM
INNER JOIN ANALITIKA_IZVODA p
ON
      ap.ASI_BROJSTAVKE = p.ASI_BROJSTAVKE
AND   ap.ASI_BROJSTAVKE = p.ASI_BROJSTAVKE
WHERE prenos.BAR_RACUN = @accountNumber 
AND   prenos.BNP_PRESEK = @presekNumber
AND   prenos.BNP_DATUM= @orderDate

END
