package events.issue.repository

import events.issue.domain.Issue
import events.issue.domain.Label
import events.issue.domain.Member
import events.issue.domain.Milestone
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import javax.sql.DataSource

object Issues: Table() {
    val id: Column<Long> = long("id").primaryKey()
    val url: Column<String?> = varchar("url", 255).nullable()
    val repositoryURL: Column<String?> = varchar("repository_url", 255).nullable()
    val labelsURL: Column<String?> = varchar("labels_url", 255).nullable()
    val commentsURL: Column<String?> = varchar("comments_url", 255).nullable()
    val eventsURL: Column<String?> = varchar("events_url", 255).nullable()
    val htmlURL: Column<String?> = varchar("html_url", 255).nullable()
    val nodeID: Column<String?> = varchar("node_id", 255).nullable()
    val number: Column<Int?> = integer("number").nullable()
    val title: Column<String?> = varchar("title", 255).nullable()
    val user: Column<Long?> = long("user_member_id").nullable()
    val state: Column<String?> = varchar("state", 255).nullable()
    val locked: Column<Boolean?> = bool("locked").nullable()
    val assignee: Column<Long?> = long("assignee").nullable()
    val milestone: Column<Long?> = long("milestone").references(Milestones.id).nullable()
    val comments: Column<Int?> = integer("comments").nullable()
    val createdAt: Column<String?> = varchar("created_at", 255).nullable()
    val updatedAt: Column<String?> = varchar("updated_at", 255).nullable()
    val closedAt: Column<String?> = varchar("closed_at", 255).nullable()
    val authorAssociation: Column<String?> = varchar("author_association", 255).nullable()
    val body: Column<String?> = varchar("body", 255).nullable()

    fun toDomain(row: ResultRow, user: Member?, labels: List<Label>?, milestone: Milestone?,
                 assignee: Member?, assignees: List<Member>?): Issue {
        return Issue(
                id = row[id],
                url = row[url],
                repositoryURL = row[repositoryURL],
                labelsURL = row[labelsURL],
                commentsURL = row[commentsURL],
                eventsURL = row[eventsURL],
                htmlURL = row[htmlURL],
                nodeID = row[nodeID],
                number = row[number],
                title = row[title],
                user = user,
                labels = labels,
                state = row[state],
                locked = row[locked],
                assignee = assignee,
                assignees = assignees,
                milestone = milestone,
                comments = row[comments],
                createdAt = row[createdAt],
                updatedAt = row[updatedAt],
                closedAt = row[closedAt],
                authorAssociation = row[authorAssociation],
                body = row[body]
        )
    }
}

object Assignees: Table() {
    val issue: Column<Long> = long("issue").primaryKey().references(Issues.id)
    val member: Column<Long> = long("member").primaryKey().references(Members.id)
}

class IssueRepository(private val dataSource: DataSource,
                      private val labelRepository: LabelRepository,
                      private val memberRepository: MemberRepository,
                      private val milestoneReposiotry: MilestoneReposiotry) {
    init {
        transaction(Database.connect(dataSource)) {
            SchemaUtils.create(Issues)
            SchemaUtils.create(Assignees)
        }
    }

    fun create (issue: Issue) {
        transaction(Database.connect(dataSource)) {

            val labs: List<Label> = issue.labels!!.filter{ labelRepository.notExists(it) }

            Optional.ofNullable(labs)
                    .ifPresent(labelRepository::createAll)

            val ass: List<Member> = issue.assignees!!.filter{ notExistsAssignees(issue, it) }

            Optional.ofNullable(ass)
                    .ifPresent(memberRepository::createAll)

            createAssignees(issue.id, ass)

            Optional.ofNullable(issue.milestone)
                    .filter(milestoneReposiotry::notExists)
                    .ifPresent(milestoneReposiotry::create)

            Issues.insert { row ->
                row[id] = issue.id!!
                row[url] = issue.url
                row[repositoryURL] = issue.repositoryURL
                row[labelsURL] = issue.labelsURL
                row[commentsURL] = issue.commentsURL
                row[eventsURL] = issue.eventsURL
                row[htmlURL] = issue.htmlURL
                row[nodeID] = issue.nodeID
                row[number] = issue.number
                row[title] = issue.title
                row[user] = issue.user?.id
                row[state] = issue.state
                row[locked] = issue.locked
                row[assignee] = issue.assignee?.id
                row[milestone] = issue.milestone?.id
                row[comments] = issue.comments
                row[createdAt] = issue.createdAt
                row[updatedAt] = issue.updatedAt
                row[closedAt] = issue.closedAt
                row[authorAssociation] = issue.authorAssociation
                row[body] = issue.body
            }
        }
    }

    private fun createAssignees(issue: Long?, assignees: List<Member>?) {
        transaction(Database.connect(dataSource)) {
            Assignees.batchInsert(assignees!!) { ass ->
                this[Assignees.issue] = issue!!
                this[Assignees.member] = ass.id!!
            }
        }
    }

    fun notExists(issue: Issue): Boolean {
        return transaction {
            Issues.select { Issues.id eq issue.id!! }
                    .count() == 0
        }
    }

    private fun notExistsAssignees(issue: Issue, member: Member): Boolean {
        return transaction {
            Assignees.select { Assignees.issue eq issue.id!! }
                    .andWhere { Assignees.member eq member.id!! }
                    .count() == 0
        }
    }

    fun getById(id: Long?): Issue? {
        if(Objects.nonNull(id)) {
            return transaction(Database.connect(dataSource)) {
                Issues.select { Issues.id eq id!! }
                        .map { Issues.toDomain(it, memberRepository.getById(it[Issues.user]),
                                labelRepository.getAllByIssueId(it[Issues.id]),
                                milestoneReposiotry.getById(it[Issues.milestone]),
                                memberRepository.getById(it[Issues.assignee]),
                                getAllAssigneesById(it[Issues.id])) }
                        .firstOrNull()
            }
        }

        return null
    }

    private fun getAllAssigneesById(id: Long?): List<Member>? {
        if(Objects.nonNull(id)) {
            return transaction(Database.connect(dataSource)) {
                Assignees.join(Members, JoinType.INNER, additionalConstraint = { Members.id eq Assignees.member })
                        .select { Assignees.issue eq id!! }
                        .map { Members.toDomain(it) }
            }
        }

        return null
    }

}