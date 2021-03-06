import com.google.inject.AbstractModule
import java.time.Clock

import domains.issue.IssueRepository
import domains.issue_query.IssueQueryRepository
import domains.user.UserRepository
import infrastructure.issue.IssueRepositoryImpl
import infrastructure.issue_query.IssueQueryRepositoryImpl
import infrastructure.user.UserRepositoryImpl
import services.issue.{IssueService, IssueServiceImpl}
import services.issue_query.{IssueQueryService, IssueQueryServiceImpl}
import services.{ApplicationTimer, AtomicCounter, Counter}
import services.user.{UserService, UserServiceImpl}

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.

 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
class Module extends AbstractModule {

  override def configure() = {
    // Use the system clock as the default implementation of Clock
    bind(classOf[Clock]).toInstance(Clock.systemDefaultZone)
    // Ask Guice to create an instance of ApplicationTimer when the
    // application starts.
    bind(classOf[ApplicationTimer]).asEagerSingleton()
    // Set AtomicCounter as the implementation for Counter.
    bind(classOf[Counter]).to(classOf[AtomicCounter])

    configureInfrastructures()
    configureServices()
  }

  private def configureInfrastructures() = {
    bind(classOf[UserRepository]).to(classOf[UserRepositoryImpl])
    bind(classOf[IssueRepository]).to(classOf[IssueRepositoryImpl])
    bind(classOf[IssueQueryRepository]).to(classOf[IssueQueryRepositoryImpl])
  }

  private def configureServices() = {
    bind(classOf[UserService]).to(classOf[UserServiceImpl])
    bind(classOf[IssueService]).to(classOf[IssueServiceImpl])
    bind(classOf[IssueQueryService]).to(classOf[IssueQueryServiceImpl])
  }
}
