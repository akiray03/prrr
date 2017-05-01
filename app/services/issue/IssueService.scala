package services.issue

import javax.inject.Inject

import domains.issue.{IssueEntity, IssueRepository}
import domains.issue_query.IssueQueryEntity

trait IssueService {
  def fetchIssues(issueQuery: IssueQueryEntity): String
}


class IssueServiceImpl @Inject()(issueRepository: IssueRepository) extends IssueService {
  override def fetchIssues(issueQuery: IssueQueryEntity): String = {
    issueRepository.fetchIssues(issueQuery)
  }
}
