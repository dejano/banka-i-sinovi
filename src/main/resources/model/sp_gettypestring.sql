create procedure sp_gettypestring @tabid int, @colid int, @typestring nvarchar(255) output
as
declare @coltypename sysname
declare @coltype  tinyint
declare @colvar   bit
declare @collen   smallint
declare @colprec  tinyint
declare @colscale tinyint

select @coltypename = st.name, @coltype = st.xtype, @colvar = st.variable, 
       @collen = sc.length, @colprec = sc.xprec, @colscale = sc.xscale
from systypes st, syscolumns sc 
where sc.id = @tabid
and sc.colid = @colid
and st.xtype = sc.xtype
and st.xtype = st.xusertype

select @typestring = @coltypename

if @coltypename in (N'char', N'varchar', N'binary', N'varbinary')
begin
    select @typestring = @typestring + N'(' + convert(nvarchar,@collen) + N')'
end
else if @coltypename in (N'nchar', N'nvarchar' )
begin
    select @typestring = @typestring + N'(' + convert(nvarchar,@collen/2) + N')'
end
else if @coltype = 108 or @coltype = 106
begin
    select @typestring = @typestring + N'(' + convert(nvarchar,@colprec) + N',' + convert(nvarchar,@colscale) + N')'
end
else if @coltype = 189
begin
    select @typestring = N'binary(8)'
end