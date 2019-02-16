package events.issue.repository

import events.issue.domain.Member
import events.issue.domain.Milestone
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import javax.sql.DataSource
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

object Milestones: Table() {
    val id: Column<Long> = long("id").primaryKey()
    val url: Column<String?> = varchar("url", 255).nullable()
    val htmlURL: Column<String?> = varchar("html_url", 255).nullable()
    val labelsURL: Column<String?> = varchar("labels_url", 255).nullable()
    val nodeID: Column<String?> = varchar("node_id", 255).nullable()
    val number: Column<Long?> = long("number").nullable()
    val title: Column<String?> = varchar("title", 255).nullable()
    val description: Column<String?> = varchar("description", 255).nullable()
    val creator: Column<Long?> = long("creator_member_id").references(Members.id).nullable()
    val openIssues: Column<Long?> = long("open_issues").nullable()
    val closedIssues: Column<Long?> = long("closed_issues").nullable()
    val state: Column<String?> = varchar("state", 255).nullable()
    val createdAt: Column<String?> = varchar("created_at", 255).nullable()
    val updatedAt: Column<String?> = varchar("updated_at", 255).nullable()
    val dueOn: Column<String?> = varchar("due_on", 255).nullable()
    val closedAt: Column<Long?> = long("closed_at").references(Members.id).nullable()

    fun toDomain(row: ResultRow, creator: Member?, closedAt: Member?): Milestone {
        return Milestone(
                id = row[id],
                url = row[url],
                htmlURL = row[htmlURL],
                labelsURL = row[labelsURL],
                nodeID = row[nodeID],
                number = row[number],
                title = row[title],
                description = row[description],
                creator = creator,
                openIssues = row[openIssues],
                closedIssues = row[closedIssues],
                state = row[state],
                createdAt = row[createdAt],
                updatedAt = row[updatedAt],
                dueOn = row[dueOn],
                closedAt = closedAt
        )
    }
}

class MilestoneReposiotry(private val dataSource: DataSource,
                          private val memberRepository: MemberRepository) {
    init {
        transaction(Database.connect(dataSource)) {
            SchemaUtils.create(Milestones)
        }
    }

    fun create(milestone: Milestone) {
        transaction(Database.connect(dataSource)) {

            Optional.ofNullable(milestone.creator)
                    .filter(memberRepository::notExists)
                    .ifPresent(memberRepository::create)

            Optional.ofNullable(milestone.closedAt)
                    .filter(memberRepository::notExists)
                    .ifPresent(memberRepository::create)

            Milestones.insert { row ->
                row[id] = milestone.id!!
                row[url] = milestone.url
                row[htmlURL] = milestone.htmlURL
                row[labelsURL] = milestone.labelsURL
                row[nodeID] = milestone.nodeID
                row[number] = milestone.number
                row[title] = milestone.title
                row[description] = milestone.description
                row[creator] = milestone.creator?.id
                row[openIssues] = milestone.openIssues
                row[closedIssues] = milestone.closedIssues
                row[state] = milestone.state
                row[createdAt] = milestone.createdAt
                row[updatedAt] = milestone.updatedAt
                row[dueOn] = milestone.dueOn
                row[closedAt] = milestone.closedAt?.id
            }
        }
    }

    fun notExists(milestone: Milestone): Boolean {
        return transaction {
            Milestones.select { Milestones.id eq milestone.id!! }
                    .count() == 0
        }
    }

    fun getById(id: Long?): Milestone? {
        if(Objects.nonNull(id)){
            return transaction(Database.connect(dataSource)) {
                Milestones.select { Milestones.id eq id!! }
                        .map { Milestones.toDomain(
                                it, memberRepository.getById(it[Milestones.creator]!!),
                                memberRepository.getById(it[Milestones.closedAt]!!))
                        }
                        .first()
            }
        }

        return null
    }
}