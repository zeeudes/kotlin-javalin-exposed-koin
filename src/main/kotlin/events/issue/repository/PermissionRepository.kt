package events.issue.repository

import events.issue.domain.Permission
import events.issue.domain.Repository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import javax.sql.DataSource

object Permissions: Table() {
    val cod: Column<Long> = long("cod").autoIncrement().primaryKey()
    val metadata: Column<String?> = varchar("metadata", 255).nullable()
    val contents: Column<String?> = varchar("contents", 255).nullable()
    val issues: Column<String?> = varchar("issues", 255).nullable()

    fun toDomain(row: ResultRow): Permission {
        return Permission(
                cod = row[cod],
                metadata = row[metadata],
                contents = row[contents],
                issues = row[issues]
        )
    }
}

class PermissionRepository(private val dataSource: DataSource) {
    init {
        transaction(Database.connect(dataSource)) {
            SchemaUtils.create(Permissions)
        }
    }

    fun create(permission: Permission) {
        transaction(Database.connect(dataSource)) {
            Permissions.insert { row ->
                row[cod] = permission.cod!!
                row[metadata] = permission.metadata
                row[contents] = permission.contents
                row[issues] = permission.issues
            }
        }
    }

    fun notExists(permission: Permission): Boolean {
        return transaction {
            Permissions.select { Permissions.contents eq permission.contents!! }
                    .count() == 0
        }
    }

    fun getById(id: Long?): Permission? {
        if (Objects.nonNull(id)) {
            return transaction(Database.connect(dataSource)) {
                Permissions.select { Permissions.cod eq id!! }
                        .map { Permissions.toDomain(it) }
                        .first()
            }
        }

        return null
    }
}