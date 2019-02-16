package events.issue.repository

import events.issue.domain.Change
import events.issue.domain.Permission
import events.issue.domain.Repository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import javax.sql.DataSource

object Changes: Table() {
    val cod: Column<Long> = long("cod").autoIncrement().primaryKey()
    val permission: Column<Long?> = long("permission").references(Permissions.cod).nullable()

    fun toDomain(row: ResultRow, permission: Permission?): Change {
        return Change(
            cod = row[cod],
            permission = permission
        )
    }
}

class ChangeRepository(private val dataSource: DataSource,
                       private val permissionRepository: PermissionRepository) {
    init {
        transaction(Database.connect(dataSource)) {
            SchemaUtils.create(Changes)
        }
    }

    fun create(change: Change){
        transaction(Database.connect(dataSource)) {

            Optional.ofNullable(change.permission)
                    .filter(permissionRepository::notExists)
                    .ifPresent(permissionRepository::create)

            Changes.insert { row ->
                row[cod] = change.cod!!
                row[permission] = change.permission?.cod
            }
        }
    }

    fun notExists(change: Change): Boolean {
        return transaction {
            Changes.select { Changes.permission eq change.permission?.cod!! }
                    .count() == 0
        }
    }

    fun getById(id: Long?): Change? {
        if(Objects.nonNull(id)) {
            return transaction(Database.connect(dataSource)) {
                Changes.select { Changes.cod eq id!! }
                        .map { Changes.toDomain(it, permissionRepository.getById(it[Changes.permission])) }
                        .first()
            }
        }

        return null
    }
}