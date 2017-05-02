package infrastructure.issue

import javax.inject.Inject

import spray.json._
import DefaultJsonProtocol._
import library.json_protocol.ZonedDateTimeJsonProtocol._
import skinny.http.{HTTP, Request, Response}
import scala.collection.mutable
import play.api.Logger

import response.{IssueListResponse, IssueResponse}
import response.IssueResponseProtocol._

import domains.issue.{IssueEntity, IssueRepository}
import domains.issue_query.IssueQueryEntity

/**
  * GitHub Issue のレポジトリ実装
  *
  * GitHub API を呼び出し、IssueEntityを返す
  *
  * @param configuration
  */
class IssueRepositoryImpl @Inject()(
                                     configuration: play.api.Configuration
                                   ) extends IssueRepository{

  /**
    * GitHub API 認証用の HTTPヘッダを返す
    *
    * @return HTTP認証ヘッダ
    */
  private def authorizationHeader(): String = {
    val githubToken = configuration.getString("secrets.github_token")
    if (githubToken.isEmpty) {
      throw new Exception("GITHUB TOKEN が設定されてないぜ")
    }
    f"token ${githubToken.get}%s"
  }

  /**
    * IssueQueryの条件で GitHub API を呼び出し、その結果を IssueEntity に詰めて返す
    *
    * @param issueQuery Issue検索条件
    * @return IssueEntityのシーケンス
    */
  override def fetchIssues(issueQuery: IssueQueryEntity): List[IssueEntity] = {
    val req = queryBuilder(issueQuery).header("Authorization", authorizationHeader())
    Logger.info(s"request to ${req.url}")
    val response: Response = HTTP.get(req)
    // トップレベルが配列になっているJSONのParseに失敗するので苦肉の策...
    val body = """{"issues":""" + response.textBody + """}"""

    val jsonAst = JsonParser(body)
    val issues = jsonAst.convertTo[IssueListResponse]

    convertToIssueEntities(issues)
  }

  /**
    * IssueQueryの条件を GitHub API 呼び出しのクエリパラメータに変換する
    *
    * @param issueQuery　Issue検索条件
    * @return HTTPリクエストオブジェクト
    */
  private def queryBuilder(issueQuery: IssueQueryEntity): Request = {
    val url = f"https://api.github.com/repos/${issueQuery.ownerName}/${issueQuery.repositoryName}/issues"
    val req = Request(url)

    val state = issueQuery.state.getOrElse("open")
    req.queryParam("state", state)

    val assignees = issueQuery.assignee.getOrElse(List())
    for (assignee <- assignees) {
      req.queryParam("assignee", assignee)
    }

    val creators = issueQuery.creator.getOrElse(List())
    for (creator <- creators) {
      req.queryParam("creator", creator)
    }

    val mentioned = issueQuery.mentioned.getOrElse(List())
    for (m <- mentioned) {
      req.queryParam("mentioned", m)
    }

    val labels = issueQuery.labels.getOrElse(List())
    if (labels.nonEmpty) {
      req.queryParam("labels", labels.mkString(","))
    }

    val sort = issueQuery.sort.getOrElse("created")
    req.queryParam("sort", sort)

    val direction = issueQuery.direction.getOrElse("desc")
    req.queryParam("direction", direction)

    issueQuery.since match {
      case Some(since) => req.queryParam("since", since.toString)
      case None =>
    }

    req
  }

  /**
    * IssueResponse を IssueEntity に詰め直す
    *
    * @param issueList GitHub API から返されたIssue一覧
    * @return IssueEntityのリスト
    */
  private def convertToIssueEntities(issueList: IssueListResponse): List[IssueEntity] = {
    val repoNames = fetchRepositoryNames(issueList)

    issueList.issues.map(i =>
      IssueEntity(
        number = i.number,
        fullName = repoNames.getOrElse(i.repository_url, ""),
        title = i.title,
        body = i.body,
        html_url = i.html_url,
        created_at = i.created_at,
        updated_at = i.updated_at
      )
    )
  }

  /**
    * Issue に対応する Gitリポジトリの情報を取得し、 Map を返します
    *
    * @param issueList
    * @return
    */
  private def fetchRepositoryNames(issueList: IssueListResponse): Map[String, String] = {
    val repoNames = mutable.HashMap.empty[String, String]

    issueList.issues.foreach(issue => {
      if (!repoNames.contains(issue.repository_url)) {
        val repoName = fetchRepositoryName(issue.repository_url)
        repoNames.put(issue.repository_url, repoName)
      }
    })

    repoNames.toMap[String, String]
  }

  /**
    * Gitリポジトリの情報を取得し、 full_name を返します
    *
    *  https://github.com/akiray03/prrr の場合、 akiray03/prrr を返す
    *
    * @param repoURL Gitリポジトリの情報取得APIのURL
    * @return Gitリポジトリの full_name
    */
  private def fetchRepositoryName(repoURL: String): String = {
    val req = Request(repoURL).header("Authorization", authorizationHeader())
    val response: Response = HTTP.get(req)

    val jsonAst = JsonParser(response.textBody)
    jsonAst.asJsObject.getFields("full_name") match {
      case Seq(JsString(full_name)) => full_name
      case _ => ""
    }
  }
}
