-- This script should be run agains the FDDPMA database. 
-- JFDD uses it to map the keys between Jira and FDDPMA

--ALTER TABLE fddpma_feature DROP COLUMN JIRA_ID;
--ALTER TABLE fddpma_project DROP COLUMN JIRA_KEY;

-- Add JFDD Fields to FDDPMA
ALTER TABLE fddpma_feature ADD COLUMN (JIRA_ID varchar(12));
ALTER TABLE fddpma_project ADD COLUMN (JIRA_KEY varchar(12));

