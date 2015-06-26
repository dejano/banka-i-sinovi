USE [PoslovnaInformatika]
GO
/****** Object:  Trigger [dbo].[AfterInsertRacunTrigger]    Script Date: 6/22/2015 12:25:05 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		Bandar Sayyed
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
alter TRIGGER [dbo].[AfterInsertRacunTrigger]
   ON	[dbo].[RACUNI_PRAVNIH_LICA]
   AFTER INSERT
AS 
BEGIN
	DECLARE
		@currentTimestamp datetime = (select CURRENT_TIMESTAMP),
		@accountNumber varchar(18),
		@pib char(10),
		@return_value int

	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

	SELECT @pib = racun.PR_PIB, @accountNumber = racun.BAR_RACUN
	FROM inserted racun;

    -- Insert statements for trigger here
	INSERT INTO [dbo].[DNEVNO_STANJE_RACUNA]
           ([PR_PIB]
           ,[BAR_RACUN]
           ,[DSR_IZVOD]
           ,[DSR_DATUM]
           ,[DSR_PRETHODNO]
           ,[DSR_UKORIST]
           ,[DSR_NATERET]
           ,[DSR_NOVOSTANJE])
     VALUES
           (@pib
           ,@accountNumber
           ,0
           ,@currentTimestamp
           ,0
           ,0
           ,0
           ,0);


END
