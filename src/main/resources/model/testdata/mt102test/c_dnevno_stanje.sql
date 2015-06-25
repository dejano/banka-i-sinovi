USE [Mt102Test]
GO

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
           (111
           ,161444444444444455
           ,0
           ,CAST(N'2015-06-19 02:02:14.127' AS DateTime)
           ,0
           ,0
           ,0
           ,0)
GO


