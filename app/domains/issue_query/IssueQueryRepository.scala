package domains.issue_query

import domains.issue_query.IssueQueryEntity

trait IssueQueryRepository {
  def listAll(): List[IssueQueryEntity]
  def insert(issueQuery: IssueQueryEntity): Option[Long]
}
