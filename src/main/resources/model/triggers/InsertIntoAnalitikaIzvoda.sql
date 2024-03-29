USE [PoslovnaInformatika]
GO
/****** Object:  Trigger [dbo].[PaymentInsteadOfInsertTrigger]    Script Date: 26.6.2015. 13:09:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- Batch submitted through debugger: SQLQuery5.sql|7|0|C:\Users\Bandjur\AppData\Local\Temp\~vs9CD2.sql
-- Batch submitted through debugger: SQLQuery22.sql|7|0|C:\Users\Bandjur\AppData\Local\Temp\~vs93D6.sql



ALTER TRIGGER [dbo].[PaymentInsteadOfInsertTrigger] ON [dbo].[ANALITIKA_IZVODA]
	INSTEAD OF INSERT
AS 
BEGIN
   DECLARE 
    @currentTimestamp datetime = (select CURRENT_TIMESTAMP),
	--@currentTimestamp datetime = '2015-07-25 14:22:50.310',
	@accountNumber varchar(18),
	@pib char(10),
	@orderDate datetime,
	@return_value int,
	@statementNumber numeric(3, 0),
	@amount decimal (15,2),
	@signedAmount decimal (15,2),
	@statementSectionNumber numeric(3, 0),
	@accountInDebt bit = 0,
	@debtorAccountNumber varchar(18),
	@creditorAccountNumber varchar(18),
	@payin decimal(15,2)=0, -- broj promena u korist
	@payout decimal(15,2)=0, -- broj promena na teret
	@totalPayin numeric(6,0)=0, -- ukupno u korist
	@totalPayout numeric(6,0)=0, -- ukupno na teret
	@statementItemIndex numeric (5,0) = 0,
	@stavkaPresekaUpdated bit = 0,
	@paymentId varchar(50)


	SELECT @accountNumber = izvod.BAR_RACUN, @orderDate = izvod.ASI_DATPRI,
			@amount = izvod.ASI_IZNOS, @debtorAccountNumber = izvod.ASI_RACDUZ,
			@creditorAccountNumber = izvod.ASI_RACPOV, @pib= izvod.PR_PIB,
			@paymentId = izvod.ID_NALOGA_PL
	FROM inserted izvod;

	EXEC	@return_value = [dbo].[GET_STATEMENT_INFO_FOR_INSERT_ORDER_ACTION]
		@accountNumber = @accountNumber,
		@orderDate = @currentTimestamp,
		@statementNumber = @statementNumber OUTPUT,
		@statementSectionNumber = @statementSectionNumber OUTPUT

		SELECT	@statementNumber as N'@statementNumber',
		@statementSectionNumber as N'@statementSectionNumber'

		if(@accountInDebt = 1)
			if (@statementNumber<>0) 
				begin
					set @totalPayout = (select dsr.DSR_NATERET from DNEVNO_STANJE_RACUNA dsr where dsr.DSR_IZVOD = @statementNumber AND dsr.BAR_RACUN=@accountNumber) +@amount;
					set @totalPayin = (select dsr.DSR_UKORIST from DNEVNO_STANJE_RACUNA dsr where dsr.DSR_IZVOD = @statementNumber AND dsr.BAR_RACUN=@accountNumber);
				end
			else 
				begin
					set @totalPayout = @amount;--(select dsr.DSR_NATERET from DNEVNO_STANJE_RACUNA dsr where dsr.BAR_RACUN=@accountNumber AND dsr.DSR_IZVOD = (select max(dr.DSR_IZVOD) from DNEVNO_STANJE_RACUNA dr where dr.BAR_RACUN=@accountNumber));
					set @totalPayin = 0;--(select dsr.DSR_UKORIST from DNEVNO_STANJE_RACUNA dsr where dsr.DSR_IZVOD = @statementNumber AND dsr.BAR_RACUN=@accountNumber);
				end
		else 
			if (@statementNumber<>0) 
				begin
					set @totalPayin = (select dsr.DSR_UKORIST from DNEVNO_STANJE_RACUNA dsr where dsr.DSR_IZVOD = (select max(dr.DSR_IZVOD) from DNEVNO_STANJE_RACUNA dr where dr.BAR_RACUN=@accountNumber) AND dsr.BAR_RACUN=@accountNumber) + @amount;
					set @totalPayout = (select dsr.DSR_NATERET from DNEVNO_STANJE_RACUNA dsr where dsr.DSR_IZVOD = (select max(dr.DSR_IZVOD) from DNEVNO_STANJE_RACUNA dr where dr.BAR_RACUN=@accountNumber) AND dsr.BAR_RACUN=@accountNumber);
				end
			else 
				begin
					set @totalPayin = @amount;--(select dsr.DSR_UKORIST from DNEVNO_STANJE_RACUNA dsr where dsr.DSR_IZVOD = @statementNumber) +1;
					set @totalPayout = 0;--(select dsr.DSR_NATERET from DNEVNO_STANJE_RACUNA dsr where dsr.DSR_IZVOD = @statementNumber);
				end


	if (@debtorAccountNumber = @accountNumber)
				begin
					set @accountInDebt =1;
					set @signedAmount = @amount * (-1);
				end
			else
				set @signedAmount = @amount;
		
		--DNEVNO STANJE RACUNA

		if( @statementNumber <> 0 ) 
			--update dnevno stanje racuna
			update DNEVNO_STANJE_RACUNA
			set DSR_PRETHODNO = (select dsr.DSR_NOVOSTANJE from DNEVNO_STANJE_RACUNA dsr where dsr.DSR_IZVOD = @statementNumber AND BAR_RACUN=@accountNumber),
				DSR_NOVOSTANJE = (select dsr.DSR_NOVOSTANJE from DNEVNO_STANJE_RACUNA dsr where dsr.DSR_IZVOD = @statementNumber AND BAR_RACUN=@accountNumber) + @signedAmount,
				DSR_UKORIST = @totalPayin,
				DSR_NATERET = @totalPayout
			where BAR_RACUN=@accountNumber AND DSR_IZVOD = @statementNumber;
		else
			--insert into dnevno stanje racuna
			begin
			set @statementNumber = (select max(dsr.DSR_IZVOD) from DNEVNO_STANJE_RACUNA dsr where dsr.BAR_RACUN=@accountNumber)+1;
			INSERT INTO [dbo].[DNEVNO_STANJE_RACUNA]
			   (PR_PIB
			   ,[BAR_RACUN]
			   ,DSR_IZVOD
			   ,[DSR_DATUM]
			   ,[DSR_PRETHODNO]
			   ,[DSR_UKORIST]
			   ,[DSR_NATERET]
			   ,[DSR_NOVOSTANJE])
			VALUES
			   (@pib
			   ,@accountNumber
			   ,@statementNumber
			   ,@currentTimestamp
			   ,(select coalesce(dsr.DSR_NOVOSTANJE, 0) from DNEVNO_STANJE_RACUNA dsr where dsr.DSR_IZVOD = (select max(dr.DSR_IZVOD) from DNEVNO_STANJE_RACUNA dr where BAR_RACUN='160111111111111155' ) AND BAR_RACUN='160111111111111155')
			   ,@totalPayin
			   ,@totalPayout
			   ,(select coalesce(dsr.DSR_NOVOSTANJE, 0) from DNEVNO_STANJE_RACUNA dsr where dsr.DSR_IZVOD = (select max(dr.DSR_IZVOD) from DNEVNO_STANJE_RACUNA dr where BAR_RACUN='160111111111111155' ) AND BAR_RACUN='160111111111111155')+@signedAmount);
			end
			
	if(SUBSTRING(@creditorAccountNumber,0,4) = SUBSTRING(@debtorAccountNumber,0,4))
		begin
			--provera da li su oba racuna u istoj banci, ako jesu onda treba sve ovo dole da se izvrsi
			-- u suprotnom nista
			

		-- INSERT INTO ANALITIKA_IZVODA
		--prvi nacin, updatuje izvod ali ne uradi insert (zato sto je 'instead of' trigger)
		--UPDATE ANALITIKA_IZVODA
		--SET DSR_IZVOD = @statementNumber
		--FROM ANALITIKA_IZVODA anal
		--INNER JOIN inserted i on i.ASI_BROJSTAVKE = anal.ASI_BROJSTAVKE; -- and i.BAR_RACUN = anal.BAR_RACUN 

		--drugi nacin - kreiraj temp tabelu
		SELECT * INTO #inserted FROM inserted;
		-- UPDATE #inserted SET DSR_IZVOD = @statementNumber;
		
		--SET IDENTITY_INSERT ANALITIKA_IZVODA ON;
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
           ,[ASI_STATUS]
		   ,[ID_NALOGA_PL])
		   VALUES
           ((select i.PR_PIB from inserted i)
		   ,(select i.BAR_RACUN from inserted i)
           ,@statementNumber
           ,(select i.VA_IFRA from inserted i)
           ,(select i.NM_SIFRA from inserted i)
           ,(select i.VPL_OZNAKA from inserted i)
           ,(select i.ASI_DUZNIK from inserted i)
           ,(select i.ASI_SVRHA from inserted i)
           ,(select i.ASI_POVERILAC from inserted i)
           ,(select i.ASI_DATPRI from inserted i)
           ,(select i.ASI_DATVAL from inserted i)
           ,(select i.ASI_RACDUZ from inserted i)
           ,(select i.ASI_MODZAD from inserted i)
           ,(select i.ASI_PBZAD from inserted i)
           ,(select i.ASI_RACPOV from inserted i)
           ,(select i.ASI_MODODOB from inserted i)
           ,(select i.ASI_PBODO from inserted i)
           ,(select i.ASI_HITNO from inserted i)
           ,(select i.ASI_IZNOS from inserted i)
           ,(select i.ASI_TIPGRESKE from inserted i)
           ,(select i.ASI_STATUS from inserted i)
		   ,@paymentId);


		--ALTER TABLE ANALITIKA_IZVODA DISABLE TRIGGER ALL;
		--INSERT INTO ANALITIKA_IZVODA SELECT * FROM #inserted;
		--ALTER TABLE ANALITIKA_IZVODA ENABLE TRIGGER ALL;

		-- PRENOS PRESEKA/IZVOD
		--reset countere za insert/update preseka
		set @payin=0; set @payout =0; set @totalPayout= 0; set @totalPayin = 0;

		set @statementItemIndex = (select count(*) from ANALITIKA_PRESEKA where BAR_RACUN= @accountNumber and DSR_IZVOD= @statementNumber and BNP_PRESEK = @statementSectionNumber and DATEDIFF(day,BNP_DATUM, @currentTimestamp)=0);

		if(@statementItemIndex%3 <> 0)
			begin
				--set @statementSectionNumber= @statementSectionNumber + CAST(1 AS Numeric(3, 0));
				-- update prenos,
				
				-- inicijalizija varijabli
				set @stavkaPresekaUpdated =1;
				if( @accountInDebt = 1)
				begin
					set @payout = (select p.BNP_BRNATERET from PRENOS_IZVODA___PRESEK p where p.BAR_RACUN= @accountNumber and p.DSR_IZVOD = @statementNumber and p.BNP_PRESEK = @statementSectionNumber and DATEDIFF(day, p.BNP_DATUM, @currentTimestamp)=0) +1;
					set @payin = (select p.BNP_BRUKORIST from PRENOS_IZVODA___PRESEK p where p.BAR_RACUN= @accountNumber and p.DSR_IZVOD = @statementNumber and p.BNP_PRESEK = @statementSectionNumber and DATEDIFF(day, p.BNP_DATUM, @currentTimestamp)=0);
					set @totalPayout = (select p.BNP_UKTERET from PRENOS_IZVODA___PRESEK p where p.BAR_RACUN= @accountNumber and p.DSR_IZVOD = @statementNumber and p.BNP_PRESEK = @statementSectionNumber and DATEDIFF(day, p.BNP_DATUM, @currentTimestamp)=0) + @amount;
					set @totalPayin = (select p.BNP_U_KORIST from PRENOS_IZVODA___PRESEK p where p.BAR_RACUN= @accountNumber and p.DSR_IZVOD = @statementNumber and p.BNP_PRESEK = @statementSectionNumber and DATEDIFF(day, p.BNP_DATUM, @currentTimestamp)=0);
				end
				else 
					begin
						set @payout = (select p.BNP_BRNATERET from PRENOS_IZVODA___PRESEK p where p.BAR_RACUN= @accountNumber and p.DSR_IZVOD = @statementNumber and p.BNP_PRESEK = @statementSectionNumber and DATEDIFF(day, p.BNP_DATUM, @currentTimestamp)=0);
						set @payin = (select p.BNP_BRUKORIST from PRENOS_IZVODA___PRESEK p where p.BAR_RACUN= @accountNumber and p.DSR_IZVOD = @statementNumber and p.BNP_PRESEK = @statementSectionNumber and DATEDIFF(day, p.BNP_DATUM, @currentTimestamp)=0)+1;
						set @totalPayout = (select p.BNP_UKTERET from PRENOS_IZVODA___PRESEK p where p.BAR_RACUN= @accountNumber and p.DSR_IZVOD = @statementNumber and p.BNP_PRESEK = @statementSectionNumber and DATEDIFF(day, p.BNP_DATUM, @currentTimestamp)=0);
						set @totalPayin = (select p.BNP_U_KORIST from PRENOS_IZVODA___PRESEK p where p.BAR_RACUN= @accountNumber and p.DSR_IZVOD = @statementNumber and p.BNP_PRESEK = @statementSectionNumber and DATEDIFF(day, p.BNP_DATUM, @currentTimestamp)=0) + @amount;
					end

				update PRENOS_IZVODA___PRESEK
				set BNP_U_KORIST = @totalPayin,
					BNP_BRUKORIST = @payin,
					BNP_BRNATERET = @payout,
					BNP_UKTERET = @totalPayout
				where BAR_RACUN= @accountNumber and DSR_IZVOD = @statementNumber and BNP_PRESEK = @statementSectionNumber and DATEDIFF(day, BNP_DATUM ,@currentTimestamp)=0;
			end
		else
			begin
				--set @StatementSectionNumber=1;
				--set @statementSectionNumber= @statementSectionNumber + CAST(1 AS Numeric(3, 0));
				--if ( @statementSectionNumber = 4) 
				set @statementSectionNumber = (select coalesce(max(p.BNP_PRESEK), 0) from PRENOS_IZVODA___PRESEK p where BAR_RACUN= @accountNumber and DSR_IZVOD = @statementNumber and BNP_PRESEK = @statementSectionNumber and DATEDIFF(day, BNP_DATUM ,@currentTimestamp)=0)+1;
				--set @statementNumber= @statementNumber +1 ;
				set @statementItemIndex = 1;

				if( @accountInDebt = 1)
					begin
						set @totalPayout = @amount; 
						set @payout=1;
					end
				else
					begin
						set @totalPayin = @amount;
						set @payin = 1;
					end
			
				--insert into prenos
				INSERT INTO [dbo].[PRENOS_IZVODA___PRESEK]
					   ([PR_PIB]
					   ,[BAR_RACUN]
					   ,[DSR_IZVOD]
					   ,[BNP_DATUM]
					   ,[BNP_PRESEK]
					   ,[BNP_BRUKORIST]
					   ,[BNP_U_KORIST]
					   ,[BNP_BRNATERET]
					   ,[BNP_UKTERET]
					   ,[BNP_BRPOGK]
					   ,[BNP_BRPOGT]
					   ,[BNP_STATUS])
				 VALUES
					   (@pib
					   ,@accountNumber
					   ,@statementNumber
					   ,@currentTimestamp
					   ,@statementSectionNumber
					   ,@payin
					   ,@totalPayin
					   ,@payout
					   ,@totalPayout
					   ,0 -- hardcoded
					   ,0 -- hardcoded
					   ,'P' );
			end

		--insert into analitika prenosa
		-- kopiraj iz sublime insert

		if(@stavkaPresekaUpdated=1) 
			set @currentTimestamp = (select coalesce(pren.BNP_DATUM,(0)) from PRENOS_IZVODA___PRESEK pren where pren.BAR_RACUN=@accountNumber and pren.BNP_PRESEK=@statementSectionNumber and pren.DSR_IZVOD = @statementNumber and DATEDIFF(day,BNP_DATUM, @currentTimestamp)=0);
		--(select coalesce( , 0) from ANALITIKA_PRESEKA where BAR_RACUN= @accountNumber and DSR_IZVOD= @statementNumber and BNP_PRESEK = @statementSectionNumber and DATEDIFF(day,BNP_DATUM, @currentTimestamp)=0);

		set @statementItemIndex = (select coalesce(count(*), 0) from ANALITIKA_PRESEKA where BAR_RACUN= @accountNumber and DSR_IZVOD= @statementNumber and BNP_PRESEK = @statementSectionNumber and DATEDIFF(day,BNP_DATUM, @currentTimestamp)=0)+1;

		--set @statementItemIndex = (select count(*) from ANALITIKA_PRESEKA where BAR_RACUN= @accountNumber and DSR_IZVOD= @statementNumber and BNP_PRESEK = @statementSectionNumber and DATEDIFF(day,BNP_DATUM, @currentTimestamp)=0);
		--if (@statementItemIndex < 3 ) 
			--set @statementItemIndex = @statementItemIndex +1;
		--else
			--set @statementItemIndex = 1;

		INSERT INTO [dbo].[ANALITIKA_PRESEKA]
					   ([PRE_PR_PIB]
					   ,[PRE_BAR_RACUN]
					   ,[DSR_IZVOD]
					   ,[BNP_DATUM]
					   ,[BNP_PRESEK]
					   ,[APR_RBR]
					   ,[PR_PIB]
					   ,[BAR_RACUN]
					   ,[ANA_DSR_IZVOD]
					   ,[ASI_BROJSTAVKE])
				 VALUES
					   (@pib
					   ,@accountNumber
					   ,@statementNumber -- !
					   ,@currentTimestamp --'2015-06-18 21:58:02.870' --!
					   ,@statementSectionNumber
					   ,@statementItemIndex
					   ,@pib--'111' -- hardcoded
					   ,@accountNumber--'160111111111111155' -- ponovljen jer imamo slucaj da su oba racuna u banci
					   ,@statementNumber --1 -- valjda je isti?
					   ,(select max(ASI_BROJSTAVKE) from ANALITIKA_IZVODA)); -- proveri da li treba '+ 1' da se doda 

			SELECT	@statementNumber as N'@statementNumber',
					@statementSectionNumber as N'@statementSectionNumber'

		end
		else
			begin
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
           ,[ASI_STATUS]
		   ,[ID_NALOGA_PL])
		   VALUES
           ((select i.PR_PIB from inserted i)
		   ,(select i.BAR_RACUN from inserted i)
           ,@statementNumber
           ,(select i.VA_IFRA from inserted i)
           ,(select i.NM_SIFRA from inserted i)
           ,(select i.VPL_OZNAKA from inserted i)
           ,(select i.ASI_DUZNIK from inserted i)
           ,(select i.ASI_SVRHA from inserted i)
           ,(select i.ASI_POVERILAC from inserted i)
           ,(select i.ASI_DATPRI from inserted i)
           ,(select i.ASI_DATVAL from inserted i)
           ,(select i.ASI_RACDUZ from inserted i)
           ,(select i.ASI_MODZAD from inserted i)
           ,(select i.ASI_PBZAD from inserted i)
           ,(select i.ASI_RACPOV from inserted i)
           ,(select i.ASI_MODODOB from inserted i)
           ,(select i.ASI_PBODO from inserted i)
           ,(select i.ASI_HITNO from inserted i)
           ,(select i.ASI_IZNOS from inserted i)
           ,(select i.ASI_TIPGRESKE from inserted i)
           ,(select i.ASI_STATUS from inserted i)
		   ,@paymentId);
			end
END
