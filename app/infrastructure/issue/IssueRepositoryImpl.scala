package infrastructure.issue

import javax.inject.Inject

import domains.issue.IssueRepository
import skinny.http.{HTTP, Request, Response}

class IssueRepositoryImpl @Inject()(
                                     configuration: play.api.Configuration
                                   ) extends IssueRepository{
  override def listAll(): String = {
    val githubToken = configuration.getString("secrets.github_token")
    if (githubToken.isEmpty) {
      throw new Exception("GITHUB TOKEN が設定されてないぜ")
    }
    val authHeader = f"token ${githubToken.get}%s"
    println(githubToken)
    val req = Request("https://api.github.com").header("Authorization", authHeader)
    println(req)
    println(req.headers)
    val response: Response = HTTP.get(req)

    response.textBody
  }
}
