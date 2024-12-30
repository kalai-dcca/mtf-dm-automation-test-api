-- Table: price_ipay_name
-- This table stores the primary IPAY value, brand name, and active ingredient name.
-- Columns:
--   pk: Auto-incremented primary key.
--   ipay: IPAY value (double, not nullable).
--   ndc_brand_name: Selected Drug Name (not nullable).
--   ndc_active_ingredient_name: Active Ingredient Name (not nullable).
CREATE TABLE price_ipay_name (
    pk AUTOINCREMENT,
    ipay DOUBLE NOT NULL,
    ndc_brand_name TEXT NOT NULL,
    ndc_active_ingredient_name TEXT NOT NULL,
    PRIMARY KEY (pk)
);

-- Table: price_ndc_cd
-- This table stores NDC codes and references the price_ipay_name table.
-- Columns:
--   pk: Auto-incremented primary key.
--   fk_price_ipay_name: Foreign key referencing price_ipay_name(pk).
--   ndc_9: NDC-9 code (not nullable).
--   ndc_11: NDC-11 code (not nullable).
CREATE TABLE price_ndc_cd (
    pk AUTOINCREMENT,
    fk_price_ipay_name LONG NOT NULL,
    ndc_9 TEXT NOT NULL,
    ndc_11 TEXT NOT NULL,
    PRIMARY KEY (pk)
);

-- Table: price_eff_dt
-- This table stores pricing effective dates and references the price_ndc_cd table.
-- Columns:
--   pk: Auto-incremented primary key.
--   fk_price_ndc_cd: Foreign key referencing price_ndc_cd(pk).
--   mfp_eff_dt: Effective Date (not nullable).
--   mfp_end_dt: End Date (nullable).
--   mfp_per_30: Single MFP per 30 DES (double, not nullable).
--   mfp_per_unit: MFP per Unit Price (double, not nullable).
--   mfp_per_package: MFP per Package Price (double, not nullable).
--   asof_dt: As of Date (not nullable).
--   update_type: Type of Update (not nullable).
--   remark: Remarks (nullable).
CREATE TABLE price_eff_dt (
    pk AUTOINCREMENT,
    fk_price_ndc_cd LONG NOT NULL,
    mfp_eff_dt DATE NOT NULL,
    mfp_end_dt DATE,
    mfp_per_30 DOUBLE NOT NULL,
    mfp_per_unit DOUBLE NOT NULL,
    mfp_per_package DOUBLE NOT NULL,
    asof_dt DATE NOT NULL,
    update_type TEXT NOT NULL,
    remark TEXT,
    PRIMARY KEY (pk)
);

-- Add Foreign Key Constraints
-- Adding constraints after table creation to define relationships.
ALTER TABLE price_ndc_cd
ADD CONSTRAINT fk_price_ipay FOREIGN KEY (fk_price_ipay_name) REFERENCES price_ipay_name(pk);

ALTER TABLE price_eff_dt
ADD CONSTRAINT fk_price_ndc FOREIGN KEY (fk_price_ndc_cd) REFERENCES price_ndc_cd(pk);

-- Insert data into price_ipay_name
INSERT INTO price_ipay_name (ipay, ndc_brand_name, ndc_active_ingredient_name)
VALUES (2026.0, 'ELIQUIS', 'APIXABAN');

-- Insert data into price_ndc_cd
INSERT INTO price_ndc_cd (fk_price_ipay_name, ndc_9, ndc_11)
VALUES (1, '00003-0893', '00003-0893-21');

INSERT INTO price_ndc_cd (fk_price_ipay_name, ndc_9, ndc_11)
VALUES (1, '00003-0893', '00003-0893-31');

INSERT INTO price_ndc_cd (fk_price_ipay_name, ndc_9, ndc_11)
VALUES (1, '00003-0893', '00003-0893-41');

-- Insert data into price_eff_dt
INSERT INTO price_eff_dt (fk_price_ndc_cd, mfp_eff_dt, mfp_end_dt, mfp_per_30, mfp_per_unit, mfp_per_package, asof_dt, update_type, remark)
VALUES (1, #2026-01-01#, NULL, 231.0, 4.145072, 248.7, #2024-08-15#, 'Added', 'Added alongside IPAY2026 MFP announcement');

INSERT INTO price_eff_dt (fk_price_ndc_cd, mfp_eff_dt, mfp_end_dt, mfp_per_30, mfp_per_unit, mfp_per_package, asof_dt, update_type, remark)
VALUES (2, #2026-01-01#, NULL, 231.0, 4.145072, 414.51, #2024-08-15#, 'Added', 'Added alongside IPAY2026 MFP announcement');

INSERT INTO price_eff_dt (fk_price_ndc_cd, mfp_eff_dt, mfp_end_dt, mfp_per_30, mfp_per_unit, mfp_per_package, asof_dt, update_type, remark)
VALUES (3, #2026-01-01#, NULL, 231.0, 4.145072, 746.11, #2024-08-15#, 'Added', 'Added alongside IPAY2026 MFP announcement');
