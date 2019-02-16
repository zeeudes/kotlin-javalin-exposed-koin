package events.issue.repository

import events.issue.domain.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import javax.sql.DataSource

private object EventIssues: Table() {
    val cod: Column<Long> = long("cod").autoIncrement().primaryKey()
    val action: Column<String?> = varchar("action", 255).nullable()
    val issue: Column<Long?> = long("issue").nullable()
    val changes: Column<Long?> = long("changes").nullable()
    val repository: Column<Long?> = long("repository").nullable()
    val sender: Column<Long?> = long("sender").nullable()

    fun toDomain(row: ResultRow, issue: Issue?, change: Change?,
                 repository: Repository?, sender: Member?): EventIssue {
        return EventIssue(
                cod = row[cod],
                action = row[action],
                issue = issue,
                changes = change,
                repository = repository,
                sender = sender
        )
    }
}

class EventIssueRepository(private val dataSource: DataSource,
                           private val issueRepository: IssueRepository,
                           private val changeRepository: ChangeRepository,
                           private val repositoriesRepository: RepositoriesRepository,
                           private val memberRepository: MemberRepository) {
    init {
        transaction(Database.connect(dataSource)) {
            SchemaUtils.create(EventIssues)
        }
    }

    fun create(eventIssue: EventIssue) {
        transaction(Database.connect(dataSource)) {
            Optional.ofNullable(eventIssue.issue)
                    .filter(issueRepository::notExists)
                    .ifPresent(issueRepository::create)

            Optional.ofNullable(eventIssue.changes)
                    .filter(changeRepository::notExists)
                    .ifPresent(changeRepository::create)

            Optional.ofNullable(eventIssue.repository)
                    .filter(repositoriesRepository::notExists)
                    .ifPresent(repositoriesRepository::create)

            Optional.ofNullable(eventIssue.sender)
                    .filter(memberRepository::notExists)
                    .ifPresent(memberRepository::create)

            EventIssues.insert { row ->
                row[action] = eventIssue.action
                row[issue] = eventIssue.issue?.id
                row[changes] = eventIssue.changes?.cod
                row[repository] = eventIssue.repository?.id
                row[sender] = eventIssue.sender?.id
            }
        }
    }

    fun getByIssueNumber(number: Int): List<EventIssue> {
        return transaction(Database.connect(dataSource)) {
            EventIssues.join(Issues, JoinType.INNER, additionalConstraint = { EventIssues.issue eq Issues.id })
                    .select { Issues.number eq number }
                    .map {
                        EventIssues.toDomain(
                                it, issueRepository.getById(it[EventIssues.issue]),
                                changeRepository.getById(it[EventIssues.changes]),
                                repositoriesRepository.getById(it[EventIssues.repository]),
                                memberRepository.getById(it[EventIssues.sender]))
                    }
        }
    }


}