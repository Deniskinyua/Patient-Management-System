
USE [master];
GO

IF NOT EXISTS (SELECT * FROM sys.sql_logins WHERE name = 'sa')
BEGIN
    CREATE LOGIN [newuser] WITH PASSWORD = 'Denis123', CHECK_POLICY = OFF;
    ALTER SERVER ROLE [sysadmin] ADD MEMBER [newuser];

    CREATE DATABASE pmsmodule-db;
END
GO