USE [PoslovnaInformatika]
GO
/****** Object:  StoredProcedure [dbo].[GET_STATEMENT_INFO_FOR_INSERT_ORDER_ACTION]    Script Date: 6/21/2015 3:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[GET_STATEMENT_INFO_FOR_INSERT_ORDER_ACTION]
	-- Add the parameters for the stored procedure here
	@accountNumber varchar(18),
	@orderDate DATETIME, -- datum za dnevno stanje racuna .. JBG
	@statementNumber  numeric(3, 0) OUTPUT,
    @statementSectionNumber NUMERIC(3,0) OUTPUT
	
AS
	DECLARE
	@currentStatementNumber numeric(3, 0),
	@currentSectionNumber numeric(3, 0)
BEGIN	
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	SELECT @currentStatementNumber= MAX(dsr.DSR_IZVOD) 
	FROM dbo.DNEVNO_STANJE_RACUNA dsr
	WHERE dsr.BAR_RACUN=@accountNumber and DATEDIFF(day,dsr.DSR_DATUM,@orderDate)=0
	--SELECT @currentStatementNumber= dsr.DSR_IZVOD
	--FROM dbo.DNEVNO_STANJE_RACUNA dsr
	--WHERE dsr.DSR_DATUM =@orderDate;

	IF @currentStatementNumber IS NOT NULL or @currentStatementNumber >0
		set @statementNumber = @currentStatementNumber
	ELSE 
		set @statementNumber = 0


	SELECT @currentSectionNumber = MAX(presek.BNP_PRESEK) 
	FROM dbo.PRENOS_IZVODA___PRESEK presek
	WHERE presek.BAR_RACUN = @accountNumber AND
		  presek.DSR_IZVOD = @statementNumber;

	IF @currentSectionNumber IS NOT NULL or @currentSectionNumber >0
		set @statementSectionNumber = @currentSectionNumber
	ELSE 
		set @statementSectionNumber = 0



	--PRINT(@currentSectionNumber);
	--PRINT(@currentStatementNumber);


	
END
