-- =============================================
-- Author:    Bandar Sayyed
-- Create date: <Create Date,,>
-- Description: <Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[cancelBankAccount]
  -- Add the parameters for the stored procedure here
  @pib char(10),
  @accountNumberToCancel varchar(18),
  @accountNumberModelToCancel numeric(2,0),
  @accountNumberReferenceNumberToCancel varchar(20),
  @accountNumberForTransfer varchar(18),
  @accountNumberModelForTransfer numeric(2,0),
  @accountNumberReferenceNumberForCancel varchar(20)
  
AS
BEGIN 
  DECLARE 
  @accountNumberForTransferExists bit,
  @debtorName varchar(255),
  @creditorName varchar(255),
  @valueId char(3),
  @townId bigint,
  @countryId char(2),
  @transferAmount decimal (15,2)
  --@paymentType char(3) -- vrsta placanja

  -- SET NOCOUNT ON added to prevent extra result sets from
  -- interfering with SELECT statements.
  SET NOCOUNT ON;

  -- PROVERA DA LI RACUN UOPSTE POSTOJI
  -- PROVERA DA LI JE VEC UKINUT


  SELECT @accountNumberForTransferExists = count(*) FROM RACUNI_PRAVNIH_LICA rac WHERE rac.BAR_RACUN= @accountNumberForTransfer AND rac.BAR_VAZI=1;

  if( @accountNumberForTransferExists <> 1) 
    begin
    --RAISEERROR('The account number for transfer doesnt exist or it isnt active',16,1);
    RAISERROR('The account number for transfer doesnt exist or it isnt active',16,1)
    RETURN;
    end


  update RACUNI_PRAVNIH_LICA
  set BAR_VAZI = 0
  WHERE BAR_RACUN = @accountNumberToCancel;


    --INSERT INTO UKIDANJE
  INSERT INTO [dbo].[UKIDANJE]
           ([PR_PIB]
           ,[BAR_RACUN]
           ,[UK_DATUKIDANJA]
           ,[UK_NARACUN])
     VALUES
           (@pib
           ,@accountNumberToCancel
           ,(SELECT CURRENT_TIMESTAMP)
           ,@accountNumberForTransfer);


  -- prebacivanje para na racun (slucaj 1. - debtor handle - skidamo mu pare)

  -- priprema podataka
  select  @debtorName = k.IME+' '+k.PREZIME from KLIJENT k  where JMBG = (select r.JMBG from RACUNI_PRAVNIH_LICA r where r.BAR_RACUN = @accountNumberToCancel);
  select  @creditorName= k.IME+' '+k.PREZIME from KLIJENT k  where JMBG = (select r.JMBG from RACUNI_PRAVNIH_LICA r where r.BAR_RACUN = @accountNumberForTransfer);
  
  select @valueId = r.VA_IFRA from RACUNI_PRAVNIH_LICA r where r.BAR_RACUN = @accountNumberToCancel;
  select @townId = NM_SIFRA, @countryId = DR_SIFRA from NASELJENO_MESTO where DR_SIFRA = (select DR_SIFRA from VALUTE where VA_IFRA= @valueId);

  select @transferAmount = dsr.DSR_NOVOSTANJE from DNEVNO_STANJE_RACUNA dsr where dsr.BAR_RACUN= @accountNumberToCancel and dsr.DSR_IZVOD = (select max(dr.DSR_IZVOD) from DNEVNO_STANJE_RACUNA dr where dr.BAR_RACUN=@accountNumberToCancel);
    
  -- resiti hardcodirane vrednosti.
  -- konverzija valute prilikom transfera para
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
           (@pib
       ,@accountNumberToCancel
           ,0 -- hardcoded al ne mora se promeniti vrednost jer ce trigger svakako da promeni.
           ,@valueId -- 1 --hardcoded
           ,@townId -- 1 --hardcoded
           ,'A' -- hardcoded
           ,@debtorName --'pero' -- hardcoded ... (select * from KLIJENT ...)
           ,'Auto money transfer due to bank account cancelment'
           ,@creditorName --'djuro' -- hardcoded ... izvuci na osnovu racuna
           ,(select CURRENT_TIMESTAMP)
           ,(select CURRENT_TIMESTAMP)
           ,@accountNumberToCancel
           ,@accountNumberModelToCancel--'97' -- hardcoded
           ,@accountNumberReferenceNumberToCancel--'11111' -- hardcoded
           ,@accountNumberForTransfer
           ,@accountNumberModelForTransfer --'97'
      ,@accountNumberReferenceNumberForCancel--'12121'
           ,0 -- hardcoded
           ,@transferAmount
           ,1
           ,'P');
  
  -- prebacivanje para na racun (slucaj 2. - creditor handle - dodajemo mu pare)
  -- priprema podataka za drugi insert (u korist racunu na koji se prebacuju pare)

  select @valueId = r.VA_IFRA from RACUNI_PRAVNIH_LICA r where r.BAR_RACUN = @accountNumberForTransfer;
  select @townId = NM_SIFRA, @countryId = DR_SIFRA from NASELJENO_MESTO where DR_SIFRA = (select DR_SIFRA from VALUTE where VA_IFRA= @valueId);

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
           (@pib
       ,@accountNumberForTransfer
           ,0 -- hardcoded al ne mora se promeniti vrednost jer ce trigger svakako da promeni.
           ,@valueId -- 1 --hardcoded
           ,@townId -- 1 --hardcoded
           ,'B' -- hardcoded
           ,@debtorName --'pero' -- hardcoded ... (select * from KLIJENT ...)
           ,'Auto money transfer due to bank account cancelment'
           ,@creditorName --'djuro' -- hardcoded ... izvuci na osnovu racuna
           ,(select CURRENT_TIMESTAMP)
           ,(select CURRENT_TIMESTAMP)
           ,@accountNumberToCancel
           ,@accountNumberModelToCancel--'97' -- hardcoded
           ,@accountNumberReferenceNumberToCancel--'11111' -- hardcoded
           ,@accountNumberForTransfer
           ,@accountNumberModelForTransfer --'97'
       ,@accountNumberReferenceNumberForCancel--'12121'
           ,0 -- hardcoded
           ,@transferAmount
           ,1
           ,'P');
END


