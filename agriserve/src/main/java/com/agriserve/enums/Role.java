package com.agriserve.enums;

/**
 * User roles in the AgriServe system.
 * Drives Spring Security authorization rules.
 */
public enum Role {
    FARMER,
    EXTENSION_OFFICER,
    PROGRAM_MANAGER,
    ADMIN,
    COMPLIANCE_OFFICER,
    GOVERNMENT_AUDITOR
}
