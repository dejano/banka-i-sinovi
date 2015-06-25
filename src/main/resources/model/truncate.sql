-- disable all constraints
EXEC sp_MSforeachtable "ALTER TABLE ? NOCHECK CONSTRAINT all"

-- delete data in all tables
EXEC sp_MSforeachtable "DELETE FROM ?"

-- enable all constraints
exec sp_MSforeachtable "ALTER TABLE ? WITH CHECK CHECK CONSTRAINT all"