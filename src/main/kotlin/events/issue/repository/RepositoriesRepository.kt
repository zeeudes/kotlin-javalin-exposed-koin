package events.issue.repository

import events.issue.domain.Member
import events.issue.domain.Repository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import javax.sql.DataSource

object Repositories: Table() {
    val id: Column<Long> = long("id").primaryKey()
    val nodeID: Column<String?> = varchar("node_id", 255).nullable()
    val name: Column<String?> = varchar("name", 255).nullable()
    val fullName: Column<String?> = varchar("full_name", 255).nullable()
    val repositoryPrivate: Column<Boolean?> = bool("repository_private").nullable()
    val owner: Column<Long?> = long("owner_member_id").references(Members.id).nullable()
    val htmlURL: Column<String?> = varchar("html_url", 255).nullable()
    val description: Column<String?> = varchar("description", 255).nullable()
    val fork: Column<Boolean?> = bool("fork").nullable()
    val url: Column<String?> = varchar("url", 255).nullable()
    val forksURL: Column<String?> = varchar("forks_url", 255).nullable()
    val keysURL: Column<String?> = varchar("keys_url", 255).nullable()
    val collaboratorsURL: Column<String?> = varchar("collaborators_url", 255).nullable()
    val teamsURL: Column<String?> = varchar("teams_url", 255).nullable()
    val hooksURL: Column<String?> = varchar("hooks_url", 255).nullable()
    val issueEventsURL: Column<String?> = varchar("issue_events_url", 255).nullable()
    val eventsURL: Column<String?> = varchar("events_url", 255).nullable()
    val assigneesURL: Column<String?> = varchar("assignees_url", 255).nullable()
    val branchesURL: Column<String?> = varchar("branches_url", 255).nullable()
    val tagsURL: Column<String?> = varchar("tags_url", 255).nullable()
    val blobsURL: Column<String?> = varchar("blobs_url", 255).nullable()
    val git_tags_url: Column<String?> = varchar("git_tags_url", 255).nullable()
    val gitRefsURL: Column<String?> = varchar("git_refs_url", 255).nullable()
    val treesURL: Column<String?> = varchar("trees_url", 255).nullable()
    val statusesURL: Column<String?> = varchar("statuses_url", 255).nullable()
    val languagesURL: Column<String?> = varchar("languages_url", 255).nullable()
    val stargazersURL: Column<String?> = varchar("stargazers_url", 255).nullable()
    val contributorsURL: Column<String?> = varchar("contributors_url", 255).nullable()
    val subscribersURL: Column<String?> = varchar("subscribers_url", 255).nullable()
    val subscriptionURL: Column<String?> = varchar("subscription_url", 255).nullable()
    val commitsURL: Column<String?> = varchar("commits_url", 255).nullable()
    val gitCommitsURL: Column<String?> = varchar("git_commits_url", 255).nullable()
    val commentsURL: Column<String?> = varchar("comments_url", 255).nullable()
    val issueCommentURL: Column<String?> = varchar("issue_comment_url", 255).nullable()
    val contentsURL: Column<String?> = varchar("contents_url", 255).nullable()
    val compareURL: Column<String?> = varchar("compare_url", 255).nullable()
    val mergesURL: Column<String?> = varchar("merges_url", 255).nullable()
    val archiveURL: Column<String?> = varchar("archive_url", 255).nullable()
    val downloadsURL: Column<String?> = varchar("downloads_url", 255).nullable()
    val issuesURL: Column<String?> = varchar("issues_url", 255).nullable()
    val pullsURL: Column<String?> = varchar("pulls_url", 255).nullable()
    val milestonesURL: Column<String?> = varchar("milestones_url", 255).nullable()
    val notificationsURL: Column<String?> = varchar("notifications_url", 255).nullable()
    val labelsURL: Column<String?> = varchar("labels_url", 255).nullable()
    val releasesURL: Column<String?> = varchar("releases_url", 255).nullable()
    val deploymentsURL: Column<String?> = varchar("deployments_url", 255).nullable()
    val createdAt: Column<String?> = varchar("created_at", 255).nullable()
    val updatedAt: Column<String?> = varchar("updated_at", 255).nullable()
    val pushedAt: Column<String?> = varchar("pushed_at", 255).nullable()
    val gitURL: Column<String?> = varchar("git_url", 255).nullable()
    val sshURL: Column<String?> = varchar("ssh_url", 255).nullable()
    val cloneURL: Column<String?> = varchar("clone_url", 255).nullable()
    val svnURL: Column<String?> = varchar("svn_url", 255).nullable()
    val homepage: Column<String?> = varchar("homepage", 255).nullable()
    val size: Column<Long?> = long("size").nullable()
    val stargazersCount: Column<Long?> = long("stargazers_count").nullable()
    val watchersCount: Column<Long?> = long("watchers_count").nullable()
    val language: Column<String?> = varchar("language", 255).nullable()
    val hasIssues: Column<Boolean?> = bool("has_issues").nullable()
    val hasProjects: Column<Boolean?> = bool("has_projects").nullable()
    val hasDownloads: Column<Boolean?> = bool("has_downloads").nullable()
    val hasWiki: Column<Boolean?> = bool("has_wiki").nullable()
    val hasPages: Column<Boolean?> = bool("has_pages").nullable()
    val forksCount: Column<Long?> = long("forks_count").nullable()
    val mirrorURL: Column<String?> = varchar("mirror_url", 255).nullable()
    val archived: Column<Boolean?> = bool("archived").nullable()
    val openIssuesCount: Column<Long?> = long("open_issues_count").nullable()
    val license: Column<String?> = varchar("license", 255).nullable()
    val forks: Column<Long?> = long("forks").nullable()
    val openIssues: Column<Long?> = long("open_issues").nullable()
    val watchers: Column<Long?> = long("watchers").nullable()
    val defaultBranch: Column<String?> = varchar("default_branch", 255).nullable()

    fun toDomain(row: ResultRow, member: Member?): Repository {
        return Repository(
                id = row[id],
                nodeId = row[nodeID],
                fullName = row[fullName],
                name = row[name],
                repositoryPrivate = row[repositoryPrivate],
                owner = member,
                htmlURL = row[htmlURL],
                description = row[description],
                fork = row[fork],
                url = row[url],
                forksURL = row[forksURL],
                keysURL = row[keysURL],
                collaboratorsURL = row[collaboratorsURL],
                teamsURL = row[teamsURL],
                hooksURL = row[hooksURL],
                issueEventsURL = row[issueEventsURL],
                eventsURL = row[eventsURL],
                assigneesURL = row[assigneesURL],
                branchesURL = row[branchesURL],
                tagsURL = row[tagsURL],
                blobsURL = row[blobsURL],
                gitTagsURL = row[git_tags_url],
                gitRefsURL = row[gitRefsURL],
                treesURL = row[treesURL],
                statusesURL = row[statusesURL],
                languagesURL = row[languagesURL],
                stargazersURL = row[stargazersURL],
                contributorsURL = row[contributorsURL],
                subscribersURL = row[subscribersURL],
                subscriptionURL = row[subscriptionURL],
                commitsURL = row[commitsURL],
                gitCommitsURL = row[gitCommitsURL],
                commentsURL = row[commentsURL],
                issueCommentURL = row[issueCommentURL],
                contentsURL = row[contentsURL],
                compareURL = row[compareURL],
                mergesURL = row[mergesURL],
                archiveURL = row[archiveURL],
                downloadsURL = row[downloadsURL],
                issuesURL = row[issuesURL],
                pullsURL = row[pullsURL],
                milestonesURL = row[milestonesURL],
                notificationsURL = row[notificationsURL],
                labelsURL = row[labelsURL],
                releasesURL = row[releasesURL],
                deploymentsURL = row[deploymentsURL],
                createdAt = row[createdAt],
                updatedAt = row[updatedAt],
                pushedAt = row[pushedAt],
                gitURL = row[gitURL],
                sshURL = row[sshURL],
                cloneURL = row[cloneURL],
                svnURL = row[svnURL],
                homepage = row[homepage],
                size = row[size],
                stargazersCount = row[stargazersCount],
                watchersCount = row[watchersCount],
                language = row[language],
                hasIssues = row[hasIssues],
                hasProjects = row[hasProjects],
                hasDownloads = row[hasDownloads],
                hasWiki = row[hasWiki],
                hasPages = row[hasPages],
                forksCount = row[forksCount],
                mirrorURL = row[mirrorURL],
                archived = row[archived],
                openIssuesCount = row[openIssuesCount],
                license = row[license],
                forks = row[forks],
                openIssues = row[openIssues],
                watchers = row[watchers],
                defaultBranch = row[defaultBranch]
        )
    }
}

class RepositoriesRepository (private val dataSource: DataSource,
                              private val memberRepository: MemberRepository) {
    init {
        transaction(Database.connect(dataSource)) {
            SchemaUtils.create(Repositories)
        }
    }

    fun create(repository: Repository) {
        transaction(Database.connect(dataSource)) {

            Optional.ofNullable(repository.owner)
                    .filter(memberRepository::notExists)
                    .ifPresent(memberRepository::create)

            Repositories.insert { row ->
                row[id] = repository.id!!
                row[nodeID] = repository.nodeId
                row[fullName] = repository.fullName
                row[name] = repository.name
                row[repositoryPrivate] = repository.repositoryPrivate
                row[htmlURL] = repository.htmlURL
                row[description] = repository.description
                row[owner] = repository.owner?.id
                row[fork] = repository.fork
                row[url] = repository.url
                row[forksURL] = repository.forksURL
                row[keysURL] = repository.keysURL
                row[collaboratorsURL] = repository.collaboratorsURL
                row[teamsURL] = repository.teamsURL
                row[hooksURL] = repository.hooksURL
                row[issueEventsURL] = repository.issueEventsURL
                row[eventsURL] = repository.eventsURL
                row[assigneesURL] = repository.assigneesURL
                row[branchesURL] = repository.branchesURL
                row[tagsURL] = repository.tagsURL
                row[blobsURL] = repository.blobsURL
                row[git_tags_url] = repository.gitTagsURL
                row[gitRefsURL] = repository.gitRefsURL
                row[treesURL] = repository.treesURL
                row[statusesURL] = repository.statusesURL
                row[languagesURL] = repository.languagesURL
                row[stargazersURL] = repository.stargazersURL
                row[contributorsURL] = repository.contributorsURL
                row[subscribersURL] = repository.subscribersURL
                row[subscriptionURL] = repository.subscriptionURL
                row[commitsURL] = repository.commitsURL
                row[gitCommitsURL] = repository.gitCommitsURL
                row[commentsURL] = repository.commentsURL
                row[issueCommentURL] = repository.issueCommentURL
                row[contentsURL] = repository.contentsURL
                row[compareURL] = repository.compareURL
                row[mergesURL] = repository.mergesURL
                row[archiveURL] = repository.archiveURL
                row[downloadsURL] = repository.downloadsURL
                row[issuesURL] = repository.issuesURL
                row[pullsURL] = repository.pullsURL
                row[milestonesURL] = repository.milestonesURL
                row[notificationsURL] = repository.notificationsURL
                row[labelsURL] = repository.labelsURL
                row[releasesURL] = repository.releasesURL
                row[deploymentsURL] = repository.deploymentsURL
                row[createdAt] = repository.createdAt
                row[updatedAt] = repository.updatedAt
                row[pushedAt] = repository.pushedAt
                row[gitURL] = repository.gitURL
                row[sshURL] = repository.sshURL
                row[cloneURL] = repository.cloneURL
                row[svnURL] = repository.svnURL
                row[homepage] = repository.homepage
                row[size] = repository.size
                row[stargazersCount] = repository.stargazersCount
                row[watchersCount] = repository.watchersCount
                row[language] = repository.language
                row[hasIssues] = repository.hasIssues
                row[hasProjects] = repository.hasProjects
                row[hasDownloads] = repository.hasDownloads
                row[hasWiki] = repository.hasWiki
                row[hasPages] = repository.hasPages
                row[forksCount] = repository.forksCount
                row[mirrorURL] = repository.mirrorURL
                row[archived] = repository.archived
                row[openIssuesCount] = repository.openIssuesCount
                row[license] = repository.license
                row[forks] = repository.forks
                row[openIssues] = repository.openIssues
                row[watchers] = repository.watchers
                row[defaultBranch] = repository.defaultBranch
            }
        }
    }

    fun notExists(repository: Repository): Boolean {
        return transaction {
            Repositories.select { Repositories.id eq repository.id!! }
                    .count() == 0
        }
    }

    fun getById(id: Long?): Repository? {
        if (Objects.nonNull(id)) {
            return transaction(Database.connect(dataSource)) {
                Repositories.select { Repositories.id eq id!! }
                        .map { Repositories.toDomain(it, memberRepository.getById(it[Repositories.owner]!!)) }
                        .first()
            }
        }

        return null
    }
}