package events.issue.repository

import events.issue.domain.Member
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import javax.sql.DataSource

object Members: Table() {
    val id: Column<Long> = long("id").primaryKey()
    val login: Column<String?> = varchar("login", 255).nullable()
    val nodeID: Column<String?> = varchar("node_id", 255).nullable()
    val avatarURL: Column<String?> = varchar("avatar_url", 255).nullable()
    val gravatarID: Column<String?> = varchar("gravatar_id", 255).nullable()
    val url: Column<String?> = varchar("url", 255).nullable()
    val htmlURL: Column<String?> = varchar("html_url", 255).nullable()
    val followersURL: Column<String?> = varchar("followers_url", 255).nullable()
    val followingURL: Column<String?> = varchar("following_url", 255).nullable()
    val gistsURL: Column<String?> = varchar("gists_url", 255).nullable()
    val starredURL: Column<String?> = varchar("starred_url", 255).nullable()
    val subscriptionsURL: Column<String?> = varchar("subscriptions_url", 255).nullable()
    val organizationsURL: Column<String?> = varchar("organizations_url", 255).nullable()
    val reposURL: Column<String?> = varchar("repos_url", 255).nullable()
    val eventsURL: Column<String?> = varchar("eventsURL", 255).nullable()
    val receivedEventsURL: Column<String?> = varchar("received_events_url", 255).nullable()
    val type: Column<String?> = varchar("type", 255).nullable()
    val siteAdmin: Column<Boolean?> = bool("site_admin").nullable()

    fun toDomain(row: ResultRow): Member {
        return Member(
                id = row[id],
                login = row[login],
                nodeId = row[nodeID],
                avatarURL = row[avatarURL],
                gravatarId = row[gravatarID],
                url = row[url],
                htmlURL = row[htmlURL],
                followersURL = row[followersURL],
                followingURL = row[followingURL],
                gistsURL = row[gistsURL],
                starredURL = row[starredURL],
                subscriptionsURL = row[subscriptionsURL],
                organizationsURL = row[organizationsURL],
                reposURL = row[reposURL],
                eventsURL = row[eventsURL],
                receivedEventsURL =  row[receivedEventsURL],
                type = row[type],
                siteAdmin = row[siteAdmin]
        )
    }
}

class MemberRepository(private val dataSource: DataSource){
    init{
        transaction(Database.connect(dataSource)) {
            SchemaUtils.create(Members)
        }
    }

    fun create(member: Member){
        transaction(Database.connect(dataSource)) {
            Members.insert { row ->
                row[id] = member.id!!
                row[login] = member.login
                row[nodeID] = member.nodeId
                row[avatarURL] = member.avatarURL
                row[gravatarID] = member.gravatarId
                row[url] = member.url
                row[htmlURL] = member.htmlURL
                row[followersURL] = member.followersURL
                row[followingURL] = member.followingURL
                row[gistsURL] = member.gistsURL
                row[starredURL] = member.starredURL
                row[subscriptionsURL] = member.subscriptionsURL
                row[organizationsURL] = member.organizationsURL
                row[reposURL] = member.reposURL
                row[eventsURL] = member.eventsURL
                row[receivedEventsURL] = member.receivedEventsURL
                row[type] = member.type
                row[siteAdmin] = member.siteAdmin
            }
        }
    }

    fun createAll(members: List<Member>) {
        transaction(Database.connect(dataSource)) {
            Members.batchInsert(members) { member ->
                this[Members.id] = member.id!!
                this[Members.login] = member.login
                this[Members.nodeID] = member.nodeId
                this[Members.avatarURL] = member.avatarURL
                this[Members.gravatarID] = member.gravatarId
                this[Members.url] = member.url
                this[Members.htmlURL] = member.htmlURL
                this[Members.followersURL] = member.followersURL
                this[Members.followingURL] = member.followingURL
                this[Members.gistsURL] = member.gistsURL
                this[Members.starredURL] = member.starredURL
                this[Members.subscriptionsURL] = member.subscriptionsURL
                this[Members.organizationsURL] = member.organizationsURL
                this[Members.reposURL] = member.reposURL
                this[Members.eventsURL] = member.eventsURL
                this[Members.receivedEventsURL] = member.receivedEventsURL
                this[Members.type] = member.type
                this[Members.siteAdmin] = member.siteAdmin
            }
        }
    }

    fun notExists(member: Member): Boolean {
        return transaction {
            Members.select { Members.id eq member.id!! }
                    .count() == 0
        }
    }

    fun getById(id: Long?): Member? {
        if(Objects.nonNull(id)) {
            return transaction(Database.connect(dataSource)) {
                Members.select { Members.id eq id!! }
                        .map { Members.toDomain(it) }
                        .firstOrNull()
            }
        }

        return null
    }

}