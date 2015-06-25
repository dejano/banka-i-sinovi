USE [PoslovnaInformatika]
GO

INSERT INTO [dbo].[ANALITIKA_IZVODA]
           ([PR_PIB]
           ,[BAR_RACUN]
           ,[DSR_IZVOD]
           ,[VA_IFRA]
           ,[NM_SIFRA]
           ,[VPL_OZNAKA]
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
           ,[ASI_STATUS])
		   VALUES
           ('111'
		   ,'160111111111111155'
           ,0
           ,1
           ,1
           ,'A'
           ,'neko'
           ,'nesto'
           ,'nekome'
           ,'2015-06-17 22:50:50.047'
           ,'2015-06-17 18:50:50.047'
           ,'160444444444444455'
           ,97
           ,1111
           ,'160111111111111155'
           ,97
           ,8978987987
           ,0
           ,700.00
           ,1
           ,'P');
GO
