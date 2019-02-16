package events.issue.repository

import events.issue.domain.Label
import events.issue.domain.Member
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import javax.sql.DataSource

object Labels: Table() {
    val id: Column<Long> = long("id").primaryKey()
    val nodeID: Column<String?> = varchar("node_id", 255).nullable()
    val url: Column<String?> = varchar("url", 255).nullable()
    val name: Column<String?> = varchar("nome", 255).nullable()
    val color: Column<String?> = varchar("color", 255).nullable()
    val labelDefault: Column<Boolean?> = bool("label_default").nullable()
    val issue: Column<Long?> = long("issue").references(Issues.id).nullable()

    fun toDomain(row: ResultRow): Label {
        return Label(
                id = row[id],
                nodeID = row[nodeID],
                url = row[url],
                name = row[name],
                color = row[color],
                labelDefault = row[labelDefault]
        )
    }
}

class LabelRepository (private val dataSource: DataSource) {
    init {
        transaction(Database.connect(dataSource)) {
            SchemaUtils.create(Labels)
        }
    }

    fun create(label: Label){
        transaction(Database.connect(dataSource)) {
            Labels.insert { row ->
                row[id] = label.id!!
                row[nodeID] = label.nodeID
                row[url] = label.url
                row[name] = label.name
                row[color] = label.color
                row[labelDefault] = label.labelDefault
                row[issue] = label.issue?.id
            }
        }
    }

    fun createAll(labels: List<Label>) {
        transaction(Database.connect(dataSource)) {
            Labels.batchInsert(labels) { label ->
                this[Labels.id] = label.id!!
                this[Labels.color] = label.color
                this[Labels.nodeID] = label.nodeID
                this[Labels.url] = label.url
                this[Labels.name] = label.name
                this[Labels.labelDefault] = label.labelDefault
                this[Labels.issue] = label.issue?.id
            }
        }
    }

    fun notExists(label: Label): Boolean {
        return transaction {
            Labels.select { Labels.id eq label.id!! }
                    .count() == 0
        }
    }

    fun getAllByIssueId(id: Long): List<Label>? {
        if (Objects.nonNull(id)) {
            return transaction(Database.connect(dataSource)) {
                Labels.select { Labels.issue eq id }
                        .map { Labels.toDomain(it) }
            }
        }

        return null
    }
}