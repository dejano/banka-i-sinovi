------------------------------------------------
--- Generisanje CRUD procedura
------------------------------------------------

DECLARE
        @db_name varchar(80),
        @table_name varchar(80),
        @table_id int,
        @pkey_name varchar(80),
        @col_name varchar(80),
        @data_type varchar(80),
        @pkey_field varchar(80),
        @proc_name varchar(80),
        @col_list varchar(8000),
        @column_id int,
        @param_list varchar(8000),
        @value_list varchar(8000),
        @dparam_list varchar(8000),
        @dvalue_list varchar(8000),
        @uparam_list varchar(8000),
        @upd_values varchar(8000),
        @uvalue_list varchar(8000)



------------------Kursor za tabele
DECLARE tabele CURSOR
FOR SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_TYPE = 'BASE TABLE' AND
INFORMATION_SCHEMA.TABLES.TABLE_SCHEMA = 'dbo' AND
NOT (TABLE_NAME LIKE 'conflict_%') AND   -- preskacemo tabele potrebne za replikaciju
    (TABLE_NAME <> 'RESULTS')
FOR READ ONLY
OPEN tabele

FETCH NEXT FROM tabele INTO @table_name

WHILE @@FETCH_STATUS = 0
BEGIN

  -- STORED PROCEDURA ZA INSERT NOVOG SLOGA
  SET @proc_name = 'c_' + @table_name
  --***********************************************************************
  PRINT 'IF EXISTS (SELECT name'
  PRINT '   FROM   sysobjects'
  PRINT '   WHERE  name = ''' + @proc_name + ''' AND type = ''P'')'
  PRINT '   DROP PROCEDURE [' + @proc_name + ']'
  PRINT 'GO'
  PRINT ' '
  PRINT '---------------------------------------------------------'
  PRINT '--   CREATE PROCEDURE FOR TABLE ' + @table_name
  PRINT '---------------------------------------------------------'
  PRINT 'CREATE PROCEDURE dbo.[' +  @proc_name + ']'
  --***********************************************************************

  --  GENERISANJE ULAZNIH PARAMETARA

  SET @table_id = OBJECT_ID(@table_name)

  DECLARE kolone CURSOR
  FOR SELECT name, colid FROM dbo.syscolumns
  WHERE @table_id = dbo.syscolumns.id
  ORDER BY colorder
  FOR READ ONLY
  OPEN kolone

  SET @value_list = ''
  SET @param_list = ''
  SET @col_list = ''
  SET @upd_values = ''
  FETCH NEXT FROM kolone INTO @col_name, @column_id
  WHILE @@FETCH_STATUS = 0
  BEGIN
      --  Kolonu razmatramo samo ako nije IDENTITY
      IF  COLUMNPROPERTY(@table_id, @col_name, 'isIdentity') = 0 AND (@col_name <> 'msrepl_tran_version')
      BEGIN
         IF @param_list <> '' SET @param_list = @param_list +  ',' + CHAR(10)
         EXEC dbo.sp_gettypestring @table_id, @column_id, @data_type OUTPUT

         IF @data_type IS NULL SET @data_type = ''

         SET @param_list = @param_list + '   @' + @col_name + ' ' + @data_type

         IF @value_list <> '' SET @value_list = @value_list +  ',' + CHAR(10)
         SET @value_list = @value_list + '   @' + @col_name

         IF @col_list <> '' SET @col_list = @col_list +  ',' + CHAR(10)
         SET @col_list = @col_list + '   ' + @col_name

         IF @upd_values <> '' SET @upd_values = @upd_values +  ',' + CHAR(10)
         SET @upd_values = @upd_values + '   ' + @col_name + ' = @' + @col_name
      END

      FETCH NEXT FROM kolone INTO @col_name, @column_id
  END
  CLOSE kolone
  DEALLOCATE kolone

  --***************************************************
  PRINT @param_list
  PRINT 'AS'
  PRINT 'INSERT INTO [' + @table_name + '] ('
  PRINT @col_list + ')'
  PRINT ' VALUES ( '
  PRINT @value_list + ')'
  PRINT 'GO'
  --**********************************************************
  PRINT ' '


  -- STORED PROCEDURA ZA BRISANJE SLOGA
  SET @proc_name = 'd_' + @table_name
  --***********************************************************************
  PRINT 'IF EXISTS (SELECT name'
  PRINT '   FROM   sysobjects'
  PRINT '   WHERE  name = ''' + @proc_name + ''' AND type = ''P'')'
  PRINT '   DROP PROCEDURE [' + @proc_name + ']'
  PRINT 'GO'
  PRINT ' '
  PRINT '---------------------------------------------------------'
  PRINT '--   DELETE PROCEDURE FOR TABLE ' + @table_name
  PRINT '---------------------------------------------------------'
  PRINT 'CREATE PROCEDURE dbo.[' +  @proc_name + ']'

  --***********************************************************************

  --  ULAZNI PARAMETRI - POLJA U KLJUCU SLOGA KOJI TREBA OBRISATI
  SELECT @pkey_name = CONSTRAINT_NAME FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
  WHERE TABLE_NAME = @table_name AND
  TABLE_SCHEMA = 'dbo' AND
  CONSTRAINT_TYPE = 'PRIMARY KEY'

  DECLARE polja_u_kljucu CURSOR
  FOR SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
  WHERE
    CONSTRAINT_NAME = @pkey_name AND
    TABLE_NAME = @table_name AND
    TABLE_SCHEMA = 'dbo'
  ORDER BY ORDINAL_POSITION
  FOR READ ONLY
  OPEN polja_u_kljucu

  SET @dvalue_list = ''
  SET @dparam_list = ''
  SET @uparam_list = ''
  SET @uvalue_list = ''
  FETCH NEXT FROM  polja_u_kljucu INTO @pkey_field
  WHILE @@FETCH_STATUS = 0
  BEGIN

     SELECT @column_id = colid FROM syscolumns WHERE
         @table_id = syscolumns.id AND
         @pkey_field = syscolumns.name

     EXEC dbo.sp_gettypestring @table_id, @column_id, @data_type OUTPUT

     IF @data_type IS NULL SET @data_type = ''

     IF @dparam_list <> '' SET @dparam_list = @dparam_list +  ',' + CHAR(10)
     IF @uparam_list <> '' SET @uparam_list = @uparam_list +  ',' + CHAR(10)

     SET @dparam_list = @dparam_list + '   @' + @pkey_field  + ' ' + @data_type
     SET @uparam_list = @uparam_list + '   @old_' + @pkey_field  + ' ' + @data_type

     IF @dvalue_list <> '' SET @dvalue_list = @dvalue_list +  ' AND ' + CHAR(10)
     SET @dvalue_list = @dvalue_list + '   ' + @pkey_field  + ' =  @' + @pkey_field

     IF @uvalue_list <> '' SET @uvalue_list = @uvalue_list +  ' AND ' + CHAR(10)
     SET @uvalue_list = @uvalue_list + '   ' + @pkey_field  + ' =  @old_' + @pkey_field

     FETCH NEXT FROM  polja_u_kljucu INTO @pkey_field
  END
  CLOSE polja_u_kljucu
  DEALLOCATE polja_u_kljucu


  --***************************************************
  PRINT @dparam_list
  PRINT 'AS'
  PRINT 'DELETE [' + @table_name + '] WHERE '
  PRINT @dvalue_list
  PRINT 'GO'
  --**********************************************************
  PRINT ' '

  -- STORED PROCEDURA ZA IZMENU SLOGA
  SET @proc_name = 'u_' + @table_name
  --***********************************************************************
  PRINT 'IF EXISTS (SELECT name'
  PRINT '   FROM   sysobjects'
  PRINT '   WHERE  name = ''' + @proc_name + ''' AND type = ''P'')'
  PRINT '   DROP PROCEDURE [' + @proc_name + ']'
  PRINT 'GO'
  PRINT ' '
  PRINT '---------------------------------------------------------'
  PRINT '--  UPDATE PROCEDURE FOR TABLE ' + @table_name
  PRINT '---------------------------------------------------------'
  PRINT 'CREATE PROCEDURE dbo.[' +  @proc_name + ']'

  --***********************************************************************

  --  ULAZNI PARAMETRI - POLJA U KLJUCU SLOGA KOJI TREBA IZMENITI (STARE VREDNOSTI) +
  --                        NOVE VREDNOSTI ZA SVA POLJA

  --***********************************************************
  PRINT @uparam_list + ',' + CHAR(10) + @param_list
  PRINT 'AS'
  PRINT 'UPDATE [' + @table_name + ']'
  PRINT 'SET'
  PRINT @upd_values
  PRINT 'WHERE '
  PRINT @uvalue_list
  PRINT 'GO'
  --***********************************************************
  PRINT ' '

  FETCH NEXT FROM tabele INTO @table_name
END

CLOSE tabele
DEALLOCATE tabele