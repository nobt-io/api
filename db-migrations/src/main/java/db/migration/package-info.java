/**
 * It is important that every migration defines its own set of beans / entities if it needs any because we must
 * capture the state of the db in these classes at the time of the migration.
 *
 * Relying on the structure of the actual database entities is NOT safe!
 */
package db.migration;